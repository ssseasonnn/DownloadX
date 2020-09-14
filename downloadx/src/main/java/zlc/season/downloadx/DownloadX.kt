package zlc.season.downloadx

import kotlinx.coroutines.CoroutineScope
import zlc.season.downloadx.downloader.Downloader
import zlc.season.downloadx.downloader.DownloadTask
import zlc.season.downloadx.task.DownloadParams
import zlc.season.downloadx.task.DownloadConfig


fun String.download(coroutineScope: CoroutineScope? = null): DownloadTask {
    val downloadParams = DownloadParams(this)
    val downloadConfig = DownloadConfig(coroutineScope)
    val downloadTask = DownloadTask(downloadParams, downloadConfig)
    downloadTask.start()
    return downloadTask
}