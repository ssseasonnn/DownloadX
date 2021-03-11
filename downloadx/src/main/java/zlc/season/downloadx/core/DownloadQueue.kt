package zlc.season.downloadx.core

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import zlc.season.downloadx.helper.Default.MAX_TASK_NUMBER
import java.util.concurrent.ConcurrentHashMap

interface DownloadQueue {
    suspend fun enqueue(task: DownloadTask)

    fun contain(tag: String): Boolean

    fun get(tag: String): DownloadTask

    fun add(task: DownloadTask)
}

class DefaultDownloadQueue private constructor(private val maxTask: Int) : DownloadQueue {
    companion object {
        private val lock = Any()
        private var instance: DefaultDownloadQueue? = null

        fun get(maxTask: Int = MAX_TASK_NUMBER): DefaultDownloadQueue {
            if (instance == null) {
                synchronized(lock) {
                    if (instance == null) {
                        instance = DefaultDownloadQueue(maxTask)
                    }
                }
            }
            return instance!!
        }
    }

    private val channel = Channel<DownloadTask>()
    private val taskMap = ConcurrentHashMap<String, DownloadTask>()

    init {
        GlobalScope.launch {
            repeat(maxTask) {
                launch {
                    channel.consumeEach { it.suspendStart() }
                }
            }
        }
    }

    override suspend fun enqueue(task: DownloadTask) {
        channel.send(task)
    }

    override fun contain(tag: String): Boolean {
        return taskMap[tag] != null
    }

    override fun get(tag: String): DownloadTask {
        return taskMap[tag]!!
    }

    override fun add(task: DownloadTask) {
        if (!contain(task.param.tag())) {
            taskMap[task.param.tag()] = task
        }
    }
}