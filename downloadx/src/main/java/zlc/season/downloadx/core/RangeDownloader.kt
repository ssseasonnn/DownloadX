package zlc.season.downloadx.core

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.consumeEach
import okhttp3.ResponseBody
import retrofit2.Response
import zlc.season.downloadx.core.Range.Companion.RANGE_SIZE
import zlc.season.downloadx.helper.Default
import zlc.season.downloadx.helper.request
import zlc.season.downloadx.utils.*
import java.io.File

@OptIn(ObsoleteCoroutinesApi::class)
class RangeDownloader(coroutineScope: CoroutineScope) : BaseDownloader(coroutineScope) {
    private lateinit var file: File
    private lateinit var shadowFile: File
    private lateinit var tmpFile: File
    private lateinit var rangeTmpFile: RangeTmpFile

    override suspend fun download(
        downloadParams: DownloadParams,
        downloadConfig: DownloadConfig,
        response: Response<ResponseBody>
    ) {
        try {
            file = downloadParams.file()
            shadowFile = file.shadow()
            tmpFile = file.tmp()

            val alreadyDownloaded = checkFiles(downloadParams, downloadConfig, response)

            if (alreadyDownloaded) {
                downloadSize = response.contentLength()
                totalSize = response.contentLength()
            } else {
                val last = rangeTmpFile.lastProgress()
                downloadSize = last.downloadSize
                totalSize = last.totalSize
                startDownload(downloadParams, downloadConfig)
            }
        } finally {
            response.closeQuietly()
        }
    }

    private fun checkFiles(
        downloadParams: DownloadParams,
        downloadConfig: DownloadConfig,
        response: Response<ResponseBody>
    ): Boolean {
        var alreadyDownloaded = false

        //make sure dir is exists
        val fileDir = downloadParams.dir()
        if (!fileDir.exists() || !fileDir.isDirectory) {
            fileDir.mkdirs()
        }

        val contentLength = response.contentLength()
        val rangeSize = downloadConfig.rangeSize
        val totalRanges = response.calcRanges(rangeSize)

        if (file.exists()) {
            if (file.length() == contentLength) {
                alreadyDownloaded = true
            } else {
                file.delete()
                recreateFiles(contentLength, totalRanges, rangeSize)
            }
        } else {
            if (shadowFile.exists() && tmpFile.exists()) {
                rangeTmpFile = RangeTmpFile(tmpFile)
                rangeTmpFile.read()

                if (!rangeTmpFile.isValid(contentLength, totalRanges)) {
                    recreateFiles(contentLength, totalRanges, rangeSize)
                }
            } else {
                recreateFiles(contentLength, totalRanges, rangeSize)
            }
        }

        return alreadyDownloaded
    }

    private fun recreateFiles(contentLength: Long, totalRanges: Long, rangeSize: Long) {
        tmpFile.recreate()
        shadowFile.recreate(contentLength)
        rangeTmpFile = RangeTmpFile(tmpFile)
        rangeTmpFile.write(contentLength, totalRanges, rangeSize)
    }

    private suspend fun startDownload(params: DownloadParams, config: DownloadConfig) {
        val endChannel = Channel<Boolean>()

        val progressChannel = coroutineScope.actor<RangeMsg> {
            channel.consumeEach { downloadSize += it.readLen }

            endChannel.send(true)
            endChannel.close()
        }

        val rangeChannel = coroutineScope.actor<Range> {
            repeat(Default.DEFAULT_RANGE_CURRENCY) {
                val deferred = async(Dispatchers.IO) {
                    channel.consumeEach {
                        it.download(params, config, progressChannel)
                    }
                }
                deferred.await()
            }

            progressChannel.close()
        }

        rangeTmpFile.undoneRanges().forEach {
            rangeChannel.send(it)
        }
        rangeChannel.close()

        endChannel.consumeEach {
            shadowFile.renameTo(file)
            tmpFile.delete()
        }
    }


    private suspend fun Range.download(
        params: DownloadParams,
        config: DownloadConfig,
        channel: SendChannel<RangeMsg>
    ) = coroutineScope {
        val deferred = async(Dispatchers.IO) {
            val url = params.url
            val rangeHeader = mapOf("Range" to "bytes=${current}-${end}")

            val response = request(url, rangeHeader)
            if (!response.isSuccessful || response.body() == null) {
                throw RuntimeException("Request failed!")
            }

            response.body()?.use {
                it.byteStream().use { source ->
                    val tmpFileBuffer = tmpFile.mappedByteBuffer(startByte(), RANGE_SIZE)
                    val shadowFileBuffer = shadowFile.mappedByteBuffer(current, remainSize())

                    val buffer = ByteArray(8192)
                    var readLen = source.read(buffer)

                    while (isActive && readLen != -1) {
                        shadowFileBuffer.put(buffer, 0, readLen)
                        current += readLen

                        tmpFileBuffer.putLong(16, current)

                        channel.send(RangeMsg(readLen))

                        readLen = source.read(buffer)
                    }
                }
            }
        }
        deferred.await()
    }
}

class RangeMsg(val readLen: Int)