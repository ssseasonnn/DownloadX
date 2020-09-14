package zlc.season.downloadx.downloader

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.consumeEach
import okhttp3.ResponseBody
import retrofit2.Response
import zlc.season.downloadx.core.request
import zlc.season.downloadx.downloader.Range.Companion.RANGE_SIZE
import zlc.season.downloadx.task.DownloadConfig
import zlc.season.downloadx.task.DownloadParams
import zlc.season.downloadx.utils.*
import java.io.File

@ObsoleteCoroutinesApi
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
        file = downloadParams.file()
        shadowFile = file.shadow()
        tmpFile = file.tmp()

        val alreadyDownloaded = checkFiles(downloadParams, downloadConfig, response)

        if (alreadyDownloaded) {
            downloadSize = response.contentLength()
            totalSize = response.contentLength()
        } else {
            totalSize = response.contentLength()
            startDownload(downloadParams, downloadConfig, response)
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

    private suspend fun startDownload(
        downloadParams: DownloadParams,
        downloadConfig: DownloadConfig,
        response: Response<ResponseBody>
    ) {
        val last = rangeTmpFile.lastProgress()
        downloadSize = last.downloadSize
        totalSize = last.totalSize

        val progressChannel = coroutineScope.actor<RangeMsg> {
            for (msg in channel) {
                downloadSize += msg.readLen
            }

            shadowFile.renameTo(file)
            tmpFile.delete()
            response.body()?.closeQuietly()
        }

        val rangeChannel = Channel<InnerDownloader>(5)

        val job = coroutineScope.launch {
            repeat(5) {
                coroutineScope.launch {
                    rangeChannel.consumeEach {
                        it.start()
                    }
                }
            }
        }

        val job1 = coroutineScope.launch {
            rangeTmpFile.undoneRanges()
                .forEach {
                    val innerDownloader =
                        InnerDownloader(downloadParams, downloadConfig, it, progressChannel)
                    rangeChannel.send(innerDownloader)
                }
        }
        job.join()
        job1.join()
    }


    inner class InnerDownloader(
        private val downloadParams: DownloadParams,
        private val downloadConfig: DownloadConfig,
        private val range: Range,
        private val channel: SendChannel<RangeMsg>
    ) {

        suspend fun start() = withContext(Dispatchers.IO) {
            val url = downloadParams.url
            val rangeHeader = mapOf("Range" to "bytes=${range.current}-${range.end}")

            val response = request(url, rangeHeader)
            val body = response.body() ?: throw RuntimeException("Response body is NULL")
            val source = body.byteStream()

            val tmpFileBuffer = tmpFile.mappedByteBuffer(range.startByte(), RANGE_SIZE)
            val shadowFileBuffer = shadowFile.mappedByteBuffer(range.current, range.remainSize())

            val buffer = ByteArray(8192)
            var readLen = source.read(buffer)

            while (isActive && readLen != -1) {
                shadowFileBuffer.put(buffer, 0, readLen)
                range.current += readLen

                tmpFileBuffer.putLong(16, range.current)

                channel.send(RangeMsg(readLen))

                readLen = source.read(buffer)
            }
            source.closeQuietly()
        }
    }
}

class RangeMsg(val readLen: Int)