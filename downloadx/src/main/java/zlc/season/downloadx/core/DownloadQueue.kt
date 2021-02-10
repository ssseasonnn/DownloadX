package zlc.season.downloadx.core

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import zlc.season.downloadx.State
import zlc.season.downloadx.helper.Default
import java.util.concurrent.ConcurrentHashMap

interface DownloadQueue {
    val channel: Channel<DownloadTask>

    suspend fun enqueue(downloadTask: DownloadTask) {}

    fun contain(tag: String): Boolean

    fun get(tag: String): DownloadTask
}

object EmptyDownloadQueue : DownloadQueue {
    override val channel: Channel<DownloadTask> = Channel()

    override fun contain(tag: String): Boolean {
        return false
    }

    override fun get(tag: String): DownloadTask {
        return DownloadTask(GlobalScope, DownloadParams(""), DownloadConfig())
    }
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
        if (!contain(downloadTask.params.tag())) {
            map[downloadTask.params.tag()] = downloadTask
        }
        channel.send(downloadTask)
    }

    override fun contain(tag: String): Boolean {
        return map[tag] != null
    }

    override fun get(tag: String): DownloadTask {
        return map[tag]!!
    }

    private suspend fun consume() {
        for (task in channel) {
            if (task.getState() == State.Waiting) {
                task.realStart()
            }
        }
    }
}