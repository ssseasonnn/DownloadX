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

    fun isStarted(): Boolean {
        return stateHolder.isStarted()
    }

    fun canStart(): Boolean {
        return stateHolder.canStart()
    }

    private fun checkJob() = downloadJob?.isActive == true

    /**
     * 开始下载
     */
    fun start() {
        coroutineScope.launch {
            if (checkJob()) return@launch

            notifyWaiting()
            try {
                config.queue.enqueue(this@DownloadTask)
            } catch (e: Exception) {
                e.log()
                notifyFailed()
            }
        }
    }

    /**
     * 开始下载并等待下载完成
     */
    suspend fun suspendStart() {
        if (checkJob()) return

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

                if (downloader == null) {
                    downloader = config.dispatcher.dispatch(this@DownloadTask, response)
                }

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

    /**
     * 停止下载
     */
    fun stop() {
        coroutineScope.launch {
            if (!checkJob()) return@launch
            downloadJob?.cancel()
            notifyStopped()
        }
    }

    /**
     * @param interval 更新进度间隔时间，单位ms
     * @param ensureLast 能否收到最后一个进度
     */
    fun progress(interval: Long = 200, ensureLast: Boolean = true): Flow<Progress> {
        return downloadProgressFlow.flatMapConcat {
            if (it == 0) return@flatMapConcat emptyFlow()

            // make sure send once
            var hasSend = false
            channelFlow {
                while (currentCoroutineContext().isActive) {
                    val progress = getProgress()

                    if (hasSend && stateHolder.isEnd()) {
                        if (!ensureLast) {
                            break
                        }
                    }

                    send(progress)
                    "url ${param.url} progress ${progress.percentStr()}".log()
                    hasSend = true

                    if (progress.isComplete()) break

                    delay(interval)
                }
            }
        }
    }

    /**
     * @param interval 更新进度间隔时间，单位ms
     */
    fun state(interval: Long = 200): Flow<State> {
        return downloadStateFlow.combine(progress(interval, ensureLast = false)) { l, r -> l.apply { progress = r } }
    }

    suspend fun getProgress(): Progress {
        return downloader?.queryProgress() ?: Progress()
    }

    fun getState() = stateHolder.currentState

    private suspend fun notifyWaiting() {
        stateHolder.updateState(stateHolder.waiting, getProgress())
        downloadStateFlow.value = stateHolder.currentState
        "url ${param.url} download task waiting.".log()
    }

    private suspend fun notifyStarted() {
        stateHolder.updateState(stateHolder.downloading, getProgress())
        downloadStateFlow.value = stateHolder.currentState
        downloadProgressFlow.value = downloadProgressFlow.value + 1
        "url ${param.url} download task start.".log()
    }

    private suspend fun notifyStopped() {
        stateHolder.updateState(stateHolder.stopped, getProgress())
        downloadStateFlow.value = stateHolder.currentState
        "url ${param.url} download task stopped.".log()
    }

    private suspend fun notifyFailed() {
        stateHolder.updateState(stateHolder.failed, getProgress())
        downloadStateFlow.value = stateHolder.currentState
        "url ${param.url} download task failed.".log()
    }

    private suspend fun notifySucceed() {
        stateHolder.updateState(stateHolder.succeed, getProgress())
        downloadStateFlow.value = stateHolder.currentState
        "url ${param.url} download task succeed.".log()
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

        var currentState: State = none

        fun isStarted(): Boolean {
            return currentState is State.Waiting || currentState is State.Downloading
        }

        fun canStart(): Boolean {
            return currentState is State.None || currentState is State.Failed || currentState is State.Stopped
        }

        fun isEnd(): Boolean {
            return currentState is State.Stopped || currentState is State.Failed || currentState is State.Succeed
        }

        fun updateState(new: State, progress: Progress): State {
            currentState = new.apply { this.progress = progress }
            return currentState
        }
    }
}