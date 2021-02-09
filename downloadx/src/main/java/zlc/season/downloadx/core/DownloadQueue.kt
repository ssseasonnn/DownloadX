package zlc.season.downloadx.core

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import zlc.season.downloadx.State
import zlc.season.downloadx.helper.Default
import java.util.concurrent.ConcurrentHashMap

interface DownloadQueue {
    val channel: Channel<DownloadTask>

    suspend fun enqueue(downloadTask: DownloadTask) {}
}

object DefaultDownloadQueue : DownloadQueue {

    init {
        GlobalScope.launch {
            repeat(Default.DEFAULT_TASK_CURRENCY) {
                async(Dispatchers.IO) {
                    consume()
                }
            }
        }
    }

    // save task
    private val map = ConcurrentHashMap<String, DownloadTask>()

    override val channel: Channel<DownloadTask> = Channel()

    override suspend fun enqueue(downloadTask: DownloadTask) {
        if (contain(downloadTask)) throw RuntimeException("Task already exists!")
        map[downloadTask.params.tag()] = downloadTask
        channel.send(downloadTask)
    }

    private suspend fun consume() {
        for (task in channel) {
            map.remove(task.params.tag())
            if (task.getState() == State.Waiting) {
                task.realStart()
            }
        }
    }

    private fun contain(task: DownloadTask): Boolean {
        return map[task.params.tag()] != null
    }
}