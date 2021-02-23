package zlc.season.downloadx

import kotlinx.coroutines.CoroutineScope
import zlc.season.downloadx.helper.Default
import zlc.season.downloadx.core.DownloadTask
import zlc.season.downloadx.core.DownloadParam
import zlc.season.downloadx.core.DownloadConfig

fun CoroutineScope.download(url: String): DownloadTask {
    return download(url,"")
}

fun CoroutineScope.download(
    url: String,
    saveName: String = "",
    savePath: String = Default.DEFAULT_SAVE_PATH,
    downloadConfig: DownloadConfig = DownloadConfig()
): DownloadTask {
    val downloadParams = DownloadParam(url, saveName, savePath)
    return if (downloadConfig.queue.contain(downloadParams.tag())) {
        downloadConfig.queue.get(downloadParams.tag())
    } else {
        DownloadTask(this, downloadParams, downloadConfig)
    }
}