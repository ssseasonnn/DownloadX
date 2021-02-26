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
    val param: DownloadParam,
    val config: DownloadConfig
) {
    private val stateHolder by lazy { StateHolder() }
    private var downloadJob: Job? = null
    private var downloader: Downloader? = null

    private val downloadProgressFlow = MutableStateFlow(0)
    private val downloadStateFlow = MutableStateFlow<State>(stateHolder.none)

    private var currentState: State = stateHolder.none

    fun isStarted(): Boolean {
        return currentState is State.Waiting || currentState is State.Downloading
    }

    fun canStart(): Boolean {
        return currentState is State.None || currentState is State.Failed || currentState is State.Stopped
    }

    fun start() {
        coroutineScope.launch {
            if (isStarted()) return@launch
            notifyWaiting()
            try {
                config.queue.enqueue(this@DownloadTask)
            } catch (e: Exception) {
                e.log()
                notifyFailed()
            }
        }
    }

    suspend fun suspendStart() {
        if (downloadJob?.isActive == true) return

        downloadJob?.cancel()
        downloadJob = coroutineScope.launch(Dispatchers.IO) {
            val response = request(param.url, config.header)
            try {
                if (!response.isSuccessful || response.body() == null) {
                    throw RuntimeException("request failed")
                }

                if (param.saveName.isEmpty()) {
                    param.saveName = response.fileName()
                }
                if (param.savePath.isEmpty()) {
                    param.savePath = Default.DEFAULT_SAVE_PATH
                }

                downloader = config.dispatcher.dispatch(this@DownloadTask, response)

                notifyStarted()

                val deferred = async(Dispatchers.IO) { downloader?.download(param, config, response) }
                deferred.await()

                notifySucceed()
            } catch (e: Exception) {
                if (e !is CancellationException) {
                    notifyFailed()
                }
                e.log()
            } finally {
                response.closeQuietly()
            }
        }
        downloadJob?.join()
    }

    fun stop() {
        coroutineScope.launch {
            if (currentState.isEnd()) return@launch
            downloadJob?.cancel()
            notifyStopped()
        }
    }

    fun progress(interval: Long = 200, ensureLast: Boolean = false): Flow<Progress> {
        return downloadProgressFlow.flatMapConcat {
            if (it == 0) return@flatMapConcat emptyFlow()

            channelFlow {
                while (currentCoroutineContext().isActive) {
                    val progress = getProgress()
                    if (!ensureLast && currentState.isEnd()) break

                    send(progress)
                    "url ${param.url} progress ${progress.percentStr()}".log()

                    if (progress.isComplete()) break

                    delay(interval)
                }
            }
        }
    }

    fun state(): Flow<State> {
        return downloadStateFlow.combine(progress()) { l, r -> l.apply { progress = r } }
    }

    suspend fun getProgress(): Progress {
        val progress = downloader?.queryProgress() ?: Progress()
        println(progress.percent())
        return progress
    }

    fun getState() = currentState

    private suspend fun notifyWaiting() {
        currentState = stateHolder.waiting.apply { progress = getProgress() }
        downloadStateFlow.value = currentState
        "url ${param.url} download task waiting.".log()
    }

    private suspend fun notifyStarted() {
        currentState = stateHolder.downloading.apply { progress = getProgress() }
        downloadStateFlow.value = currentState
        downloadProgressFlow.value = downloadProgressFlow.value + 1
        "url ${param.url} download task start.".log()
    }

    private suspend fun notifyStopped() {
        currentState = stateHolder.stopped.apply { progress = getProgress() }
        downloadStateFlow.value = currentState
        "url ${param.url} download task stop.".log()
    }

    private suspend fun notifyFailed() {
        currentState = stateHolder.failed.apply { progress = getProgress() }
        downloadStateFlow.value = currentState
        "url ${param.url} download task failed.".log()
    }

    private suspend fun notifySucceed() {
        currentState = stateHolder.succeed.apply { progress = getProgress() }
        downloadStateFlow.value = currentState
        "url ${param.url} download task complete.".log()
    }

    private fun Progress.isComplete(): Boolean {
        return totalSize > 0 && totalSize == downloadSize
    }

    class StateHolder {
        val none by lazy { State.None() }
        val waiting by lazy { State.Waiting() }
        val downloading by lazy { State.Downloading() }
        val stopped by lazy { State.Stopped() }
        val failed by lazy { State.Failed() }
        val succeed by lazy { State.Succeed() }
    }
}