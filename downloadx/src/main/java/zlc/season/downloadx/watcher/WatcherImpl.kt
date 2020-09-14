package zlc.season.downloadx.watcher

import zlc.season.downloadx.task.DownloadParams
import zlc.season.downloadx.utils.file

object WatcherImpl : Watcher {
    private val taskMap = mutableMapOf<String, String>()
    private val fileMap = mutableMapOf<String, String>()

    @Synchronized
    override fun watch(downloadParams: DownloadParams) {
        //check task
        check(taskMap[downloadParams.tag()] == null) { "Task [${downloadParams.tag()} is exists!" }

        val filePath = downloadParams.file().canonicalPath
        //check file
        check(fileMap[filePath] == null) { "File [$filePath] is occupied!" }

        taskMap[downloadParams.tag()] = downloadParams.tag()
        fileMap[filePath] = filePath
    }

    @Synchronized
    override fun unwatch(downloadParams: DownloadParams) {
        taskMap.remove(downloadParams.tag())
        fileMap.remove(downloadParams.file().canonicalPath)
    }
}