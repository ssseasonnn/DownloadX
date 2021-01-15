package zlc.season.downloadx

import kotlinx.coroutines.CoroutineScope
import zlc.season.downloadx.downloader.DownloadTask
import zlc.season.downloadx.task.DownloadParams
import zlc.season.downloadx.task.DownloadConfig


fun String.download(coroutineScope: CoroutineScope): DownloadTask {
    val downloadParams = DownloadParams(this)
    val downloadConfig = DownloadConfig(coroutineScope)
    return DownloadTask(downloadParams, downloadConfig)
}


fun CoroutineScope.download(url: String): DownloadTask {
    val downloadParams = DownloadParams(url)
    val downloadConfig = DownloadConfig(this)
    return DownloadTask(downloadParams, downloadConfig)
}