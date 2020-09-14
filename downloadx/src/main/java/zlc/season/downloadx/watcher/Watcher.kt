package zlc.season.downloadx.watcher

import zlc.season.downloadx.task.DownloadParams

interface Watcher {
    fun watch(downloadParams: DownloadParams)

    fun unwatch(downloadParams: DownloadParams)
}