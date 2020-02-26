package zlc.season.downloadx.watcher

import zlc.season.downloadx.task.Task

interface Watcher {
    fun watch(task: Task)

    fun unwatch(task: Task)
}