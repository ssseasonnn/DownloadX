package zlc.season.downloadx.downloader

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor
import okhttp3.ResponseBody
import retrofit2.Response
import zlc.season.downloadx.Progress
import zlc.season.downloadx.downloader.Range.Companion.RANGE_SIZE
import zlc.season.downloadx.task.TaskInfo
import zlc.season.downloadx.utils.*
import java.io.File

@UseExperimental(ExperimentalCoroutinesApi::class, ObsoleteCoroutinesApi::class)
class RangeDownloader : Downloader {
    private var alreadyDownloaded = false

    private lateinit var file: File
    private lateinit var shadowFile: File
    private lateinit var tmpFile: File
    private lateinit var rangeTmpFile: RangeTmpFile

    override suspend fun ProducerScope<Progress>.download(
        taskInfo: TaskInfo,
        response: Response<ResponseBody>
    ) {
        file = taskInfo.task.getFile()
        shadowFile = file.shadow()
        tmpFile = file.tmp()

        beforeDownload(taskInfo, response)
        send(Progress(response.contentLength(), response.contentLength()))
        if (alreadyDownloaded) {
            send(Progress(response.contentLength(), response.contentLength()))
        } else {
            startDownload(taskInfo, response)
        }
    }

    private fun beforeDownload(taskInfo: TaskInfo, response: Response<ResponseBody>) {
        //make sure dir is exists
        val fileDir = taskInfo.task.getDir()
        if (!fileDir.exists() || !fileDir.isDirectory) {
            fileDir.mkdirs()
        }

        if (file.exists()) {
            if (taskInfo.validator.validate(file, response)) {
                alreadyDownloaded = true
            } else {
                file.delete()
                createFiles(response, taskInfo)
            }
        } else {
            if (shadowFile.exists() && tmpFile.exists()) {

                rangeTmpFile = RangeTmpFile(tmpFile)

                if (!rangeTmpFile.read(response, taskInfo)) {
                    createFiles(response, taskInfo)
                }
            } else {
                createFiles(response, taskInfo)
            }
        }
    }

    private fun createFiles(response: Response<ResponseBody>, taskInfo: TaskInfo) {
        tmpFile.recreate {
            shadowFile.recreate(response.contentLength()) {
                rangeTmpFile = RangeTmpFile(tmpFile)
                rangeTmpFile.write(response, taskInfo)
            }
        }
    }

    private suspend fun ProducerScope<Progress>.startDownload(
        taskInfo: TaskInfo,
        response: Response<ResponseBody>
    ) {
        val progress = rangeTmpFile.lastProgress()

        val sendChannel = actor<RangeMsg> {
            for (msg in channel) {
                progress.apply { this.downloadSize += msg.readLen }
                send(progress)
            }

            shadowFile.renameTo(file)
            tmpFile.delete()
            response.body()?.closeQuietly()
            close()
        }

        rangeTmpFile.undoneRanges()
            .forEach {
                InnerDownloader(taskInfo, it, sendChannel).download()
            }
    }


    inner class InnerDownloader(
        private val taskInfo: TaskInfo,
        private val range: Range,
        private val channel: SendChannel<RangeMsg>
    ) {

        suspend fun download() = coroutineScope {
            launch(Dispatchers.IO) {
                val url = taskInfo.task.url
                val request = taskInfo.request
                val rangeHeader = mapOf("Range" to "bytes=${range.current}-${range.end}")
                val response = request.get(url, rangeHeader)

                val body = response.body() ?: throw RuntimeException("Response body is NULL")

                val source = body.byteStream()

                val tmpFileBuffer = tmpFile.mappedByteBuffer(range.startByte(), RANGE_SIZE)
                val shadowFileBuffer =
                    shadowFile.mappedByteBuffer(range.current, range.remainSize())

                val buffer = ByteArray(8192)
                var readLen = source.read(buffer)

                while (readLen != -1) {
                    shadowFileBuffer.put(buffer, 0, readLen)
                    range.current += readLen

                    tmpFileBuffer.putLong(16, range.current)

                    channel.send(RangeMsg(readLen))
                    readLen.log("InnerDownloader send")

                    readLen = source.read(buffer)
                }
                source.closeQuietly()
            }
        }
    }
}

class RangeMsg(val readLen: Int)