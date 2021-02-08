package zlc.season.downloadx.core

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import zlc.season.downloadx.State

interface DownloadQueue {
    val channel: Channel<DownloadTask>

    suspend fun enqueue(downloadTask: DownloadTask) {}
}

object DefaultDownloadQueue : DownloadQueue {

    init {
        GlobalScope.launch {
            consume()
        }
    }

    override val channel: Channel<DownloadTask> = Channel(2)
    private val list = mutableListOf<DownloadTask>()

    override suspend fun enqueue(downloadTask: DownloadTask) {
        if (contain(downloadTask)) throw RuntimeException("Task already exists!")
        list.add(downloadTask)
        channel.send(downloadTask)
    }

    private suspend fun consume() {
        for (task in channel) {
            list.remove(task)
            if (task.getState() == State.Waiting) {
                task.realStart()
            }
        }
    }

    private fun contain(task: DownloadTask): Boolean {
        var flag = false
        list.forEach {
            if (it.params.tag() == task.params.tag()) {
                flag = true
            }
        }
        return flag
    }
}