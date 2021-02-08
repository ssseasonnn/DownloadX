package zlc.season.downloadx

import kotlinx.coroutines.CoroutineScope
import zlc.season.downloadx.helper.Default
import zlc.season.downloadx.core.DownloadTask
import zlc.season.downloadx.core.DownloadParams
import zlc.season.downloadx.core.DownloadConfig

fun CoroutineScope.download(url: String): DownloadTask {
    val downloadParams = DownloadParams(url)
    val downloadConfig = DownloadConfig()
    return DownloadTask(this, downloadParams, downloadConfig)
}

fun CoroutineScope.download(
    url: String,
    saveName: String = "",
    savePath: String = Default.DEFAULT_SAVE_PATH,
    downloadConfig: DownloadConfig = DownloadConfig()
): DownloadTask {
    val downloadParams = DownloadParams(url, saveName, savePath)
    return DownloadTask(this, downloadParams, downloadConfig)
}