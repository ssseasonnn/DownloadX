package zlc.season.downloadx

import kotlinx.coroutines.CoroutineScope
import zlc.season.downloadx.helper.Default
import zlc.season.downloadx.core.DownloadTask
import zlc.season.downloadx.core.DownloadParam
import zlc.season.downloadx.core.DownloadConfig

fun CoroutineScope.download(
    url: String,
    saveName: String = "",
    savePath: String = Default.DEFAULT_SAVE_PATH,
    downloadConfig: DownloadConfig = DownloadConfig()
): DownloadTask {
    val downloadParam = DownloadParam(url, saveName, savePath)
    val task = DownloadTask(this, downloadParam, downloadConfig)
    return downloadConfig.taskManager.add(task)
}

fun CoroutineScope.download(
    downloadParam: DownloadParam,
    downloadConfig: DownloadConfig = DownloadConfig()
): DownloadTask {
    val task = DownloadTask(this, downloadParam, downloadConfig)
    return downloadConfig.taskManager.add(task)
}