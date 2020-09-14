package zlc.season.downloadx.storage

import zlc.season.downloadx.task.DownloadParams

open class MemoryStorage : Storage {
    companion object {
        //memory cache
        private val taskPool = mutableMapOf<DownloadParams, DownloadParams>()
    }

    @Synchronized
    override fun load(downloadParams: DownloadParams) {
        val result = taskPool[downloadParams]
        if (result != null) {
            downloadParams.saveName = result.saveName
            downloadParams.savePath = result.savePath
        }
    }

    @Synchronized
    override fun save(downloadParams: DownloadParams) {
        taskPool[downloadParams] = downloadParams
    }

    @Synchronized
    override fun delete(downloadParams: DownloadParams) {
        taskPool.remove(downloadParams)
    }
}