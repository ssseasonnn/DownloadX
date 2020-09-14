package zlc.season.downloadx.storage

import zlc.season.downloadx.task.DownloadParams

interface Storage {
    fun load(downloadParams: DownloadParams)

    fun save(downloadParams: DownloadParams)

    fun delete(downloadParams: DownloadParams)
}