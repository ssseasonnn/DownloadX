package zlc.season.downloadx.core

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import zlc.season.downloadx.Progress
import zlc.season.downloadx.State
import zlc.season.downloadx.helper.Default
import zlc.season.downloadx.helper.request
import zlc.season.downloadx.utils.closeQuietly
import zlc.season.downloadx.utils.fileName
import zlc.season.downloadx.utils.log

@OptIn(ObsoleteCoroutinesApi::class, FlowPreview::class, ExperimentalCoroutinesApi::class)
open class DownloadTask(
    val coroutineScope: CoroutineScope,
    val params: DownloadParams,
    val config: DownloadConfig
) {
    private var downloadJob: Job? = null
    private var downloader: Downloader? = null

    private val downloadProgressFlow = MutableStateFlow(0)
    private val downloadStateFlow = MutableStateFlow<State>(State.Waiting)

    private var currentState: State = State.Waiting

    fun start() {
        coroutineScope.launch {
            notifyWaiting()
            try {
                config.queue.enqueue(this@DownloadTask)
            } catch (e: Exception) {
                e.message.log()
                notifyFailed()
            }
        }
    }

    fun startNow() {
        coroutineScope.launch {
            realStart()
        }
    }

    suspend fun realStart() {
        downloadJob = coroutineScope.launch(Dispatchers.IO) {
            try {
                val response = request(params.url, config.header)
                if (!response.isSuccessful || response.body() == null) {
                    response.closeQuietly()
                    throw RuntimeException("request failed")
                }

                if (params.saveName.isEmpty()) {
                    params.saveName = response.fileName()
                }
                if (params.savePath.isEmpty()) {
                    params.savePath = Default.DEFAULT_SAVE_PATH
                }

                downloader = config.dispatcher.dispatch(this@DownloadTask, response)

                notifyStarted()

                val deferred = async(Dispatchers.IO) { downloader?.download(params, config, response) }
                deferred.await()

                notifySucceed()
            } catch (e: Exception) {
                e.printStackTrace()
                e.message.log()
                notifyFailed()
            }
        }
        downloadJob?.join()
    }

    fun stop() {
        downloadJob?.cancel()
        notifyStopped()
    }

    fun progress(interval: Long = 200): Flow<Progress> {
        return downloadProgressFlow.flatMapConcat {
            if (it == 0) return@flatMapConcat emptyFlow()

            channelFlow {
                while (currentCoroutineContext().isActive) {
                    val progress = getProgress()
                    send(progress)
                    "url ${params.url} progress ${progress.percentStr()}".log()

                    if (currentState.isEnd() || progress.isComplete()) {
                        break
                    }

                    delay(interval)
                }
            }
        }
    }

    fun state(): Flow<State> = downloadStateFlow

    suspend fun getProgress(): Progress {
        return downloader?.queryProgress() ?: Progress()
    }

    fun getState() = currentState

    private fun notifyWaiting() {
        currentState = State.Waiting
        downloadStateFlow.value = State.Waiting
        "url ${params.url} download task waiting.".log()
    }

    private fun notifyStarted() {
        currentState = State.Started
        downloadStateFlow.value = State.Started
        downloadProgressFlow.value = downloadProgressFlow.value + 1
        "url ${params.url} download task start.".log()
    }

    private fun notifyStopped() {
        currentState = State.Stopped
        downloadStateFlow.value = State.Stopped
        "url ${params.url} download task stop.".log()
    }

    private fun notifyFailed() {
        currentState = State.Failed
        downloadStateFlow.value = State.Failed
        "url ${params.url} download task failed.".log()
    }

    private fun notifySucceed() {
        currentState = State.Succeed
        downloadStateFlow.value = State.Succeed
        "url ${params.url} download task complete.".log()
    }

    private fun Progress.isComplete(): Boolean {
        return totalSize > 0 && totalSize == downloadSize
    }
}