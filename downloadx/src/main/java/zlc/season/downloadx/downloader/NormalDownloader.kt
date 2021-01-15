package zlc.season.downloadx.downloader

import kotlinx.coroutines.*
import okhttp3.ResponseBody
import okio.IOException
import okio.buffer
import okio.sink
import retrofit2.Response
import zlc.season.downloadx.task.DownloadConfig
import zlc.season.downloadx.task.DownloadParams
import zlc.season.downloadx.utils.*
import java.io.File
import java.io.FileOutputStream


class NormalDownloader(coroutineScope: CoroutineScope) : BaseDownloader(coroutineScope) {
    private var alreadyDownloaded = false

    private lateinit var file: File
    private lateinit var shadowFile: File

    override suspend fun download(
        downloadParams: DownloadParams,
        downloadConfig: DownloadConfig,
        response: Response<ResponseBody>
    ) {
        val body = response.body()
        body.use {
            if (body == null) {
                "Url [${downloadParams.url}] response body is NULL!".log()
                return
            }
            file = downloadParams.file()
            shadowFile = file.shadow()

            val contentLength = response.contentLength()
            val isChunked = response.isChunked()

            downloadPrepare(downloadParams, contentLength)

            if (alreadyDownloaded) {
                this.downloadSize = contentLength
                this.totalSize = contentLength
                this.isChunked = isChunked
            } else {
                this.totalSize = contentLength
                this.downloadSize = 0
                this.isChunked = isChunked
                startDownload(body)
            }
        }
    }

    private fun downloadPrepare(downloadParams: DownloadParams, contentLength: Long) {
        //make sure dir is exists
        val fileDir = downloadParams.dir()
        if (!fileDir.exists() || !fileDir.isDirectory) {
            fileDir.mkdirs()
        }

        if (file.exists()) {
            if (file.length() == contentLength) {
                alreadyDownloaded = true
            } else {
                file.delete()
                shadowFile.recreate()
            }
        } else {
            shadowFile.recreate()
        }
    }

    private suspend fun startDownload(body: ResponseBody) = withContext(Dispatchers.IO) {
        val source = body.source()
        val sink = shadowFile.sink().buffer()
        val buffer = sink.buffer

        var readLen = source.read(buffer, 8192L)
        while (isActive && readLen != -1L) {
            downloadSize += readLen
            readLen = source.read(buffer, 8192L)
        }
        shadowFile.renameTo(file)
        totalSize = downloadSize
    }
}