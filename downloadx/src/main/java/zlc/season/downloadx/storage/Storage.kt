package zlc.season.downloadx.storage

import zlc.season.downloadx.task.Task

interface Storage {
    fun load(task: Task)

    fun save(task: Task)

    fun delete(task: Task)
}