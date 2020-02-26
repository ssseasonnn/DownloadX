package zlc.season.downloadx.downloader

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import okio.buffer
import okio.sink
import retrofit2.Response
import zlc.season.downloadx.Progress
import zlc.season.downloadx.task.TaskInfo
import zlc.season.downloadx.utils.*
import java.io.File

@UseExperimental(ExperimentalCoroutinesApi::class)
class NormalDownloader : Downloader {
    private var alreadyDownloaded = false

    private lateinit var file: File
    private lateinit var shadowFile: File

    override suspend fun ProducerScope<Progress>.download(
        taskInfo: TaskInfo,
        response: Response<ResponseBody>
    ) {
        val body = response.body() ?: throw RuntimeException("Response body is NULL")

        file = taskInfo.task.getFile()
        shadowFile = file.shadow()

        beforeDownload(taskInfo, response)

        if (alreadyDownloaded) {
            send(Progress(response.contentLength(), response.contentLength()))
        } else {
            startDownload(
                body, Progress(
                    totalSize = response.contentLength(),
                    isChunked = response.isChunked()
                )
            )
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
                shadowFile.recreate()
            }
        } else {
            shadowFile.recreate()
        }
    }

    private fun ProducerScope<Progress>.startDownload(
        body: ResponseBody,
        progress: Progress
    ) = launch(Dispatchers.IO) {
        val source = body.source()
        val sink = shadowFile.sink().buffer()
        val buffer = sink.buffer

        var readLen = source.read(buffer, 8192L)
        while (readLen != -1L) {
            sink.emit()
            send(progress.apply {
                downloadSize += readLen
            })
            readLen = source.read(buffer, 8192L)
        }
        sink.flush()
        shadowFile.renameTo(file)
        close()
    }
}