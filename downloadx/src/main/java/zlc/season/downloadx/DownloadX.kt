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
    return if (downloadConfig.queue.contain(downloadParam.tag())) {
        downloadConfig.queue.get(downloadParam.tag())
    } else {
        val task = DownloadTask(this, downloadParam, downloadConfig)
        downloadConfig.queue.add(task)
        task
    }
}

fun CoroutineScope.download(
    downloadParam: DownloadParam,
    downloadConfig: DownloadConfig = DownloadConfig()
): DownloadTask {
    return if (downloadConfig.queue.contain(downloadParam.tag())) {
        downloadConfig.queue.get(downloadParam.tag())
    } else {
        val task = DownloadTask(this, downloadParam, downloadConfig)
        downloadConfig.queue.add(task)
        task
    }
}