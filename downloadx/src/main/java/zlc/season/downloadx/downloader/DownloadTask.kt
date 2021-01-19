package zlc.season.downloadx.downloader

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.flow.*
import zlc.season.downloadx.Progress
import zlc.season.downloadx.State
import zlc.season.downloadx.core.Default
import zlc.season.downloadx.core.request
import zlc.season.downloadx.task.DownloadConfig
import zlc.season.downloadx.task.DownloadParams
import zlc.season.downloadx.utils.fileName
import zlc.season.downloadx.utils.isSupportRange
import zlc.season.downloadx.utils.log

@OptIn(ObsoleteCoroutinesApi::class, FlowPreview::class, ExperimentalCoroutinesApi::class)
open class DownloadTask(
    private val downloadParams: DownloadParams,
    private val downloadConfig: DownloadConfig
) {
    private var downloadJob: Job? = null
    private var downloader: Downloader? = null

    private val coroutineScope = downloadConfig.coroutineScope
    private val downloadProgressFlow = MutableStateFlow(0)
    private val downloadStateFlow = MutableStateFlow<State>(State.Waiting)

    private var progressProducer: ProducerScope<Progress>? = null

    fun start() {
        if (downloadJob != null) {
            downloadJob?.cancel()
        }
        val errorHandler = CoroutineExceptionHandler { _, throwable ->
            throwable.message.log()
            notifyFailed()
        }
        downloadJob = coroutineScope.launch(errorHandler) {
            "url ${downloadParams.url} download task start.".log()
            val response = request(downloadParams.url, downloadConfig.header)
            if (!response.isSuccessful) {
                "url ${downloadParams.url} request failed.".log()
                throw RuntimeException()
            }

            if (downloadParams.saveName.isEmpty()) {
                downloadParams.saveName = response.fileName()
            }
            if (downloadParams.savePath.isEmpty()) {
                downloadParams.savePath = Default.DEFAULT_SAVE_PATH
            }

            downloader = if (response.isSupportRange()) {
                NormalDownloader(coroutineScope)
            } else {
                NormalDownloader(coroutineScope)
            }
            val downloadDeferred = async { downloader?.download(downloadParams, downloadConfig, response) }
            val notifyDeferred = async { notifyStarted() }
            downloadDeferred.await()
            notifyDeferred.await()

            notifySucceed()
        }
    }

    fun stop() {
        downloadJob?.cancel()
        notifyStopped()
    }

    fun progress(interval: Long = 200): Flow<Progress> {
        return downloadProgressFlow.flatMapConcat {
            if (it == 0) return@flatMapConcat emptyFlow()

            channelFlow {
                progressProducer = this
                var progress = getProgress()
                if (progress.isComplete()) {
                    send(progress)
                    "url ${downloadParams.url} progress ${progress.percentStr()}".log()
                } else {
                    while (currentCoroutineContext().isActive && isDownloading()) {
                        progress = getProgress()
                        send(progress)
                        "url ${downloadParams.url} progress ${progress.percentStr()}".log()

                        delay(interval)
                    }
                }
            }
        }
    }

    fun state(): Flow<State> = downloadStateFlow

    suspend fun getProgress(): Progress {
        return downloader?.queryProgress() ?: Progress()
    }

    private fun isDownloading(): Boolean {
        return downloadJob?.isActive == true
    }

    private fun notifyStarted() {
        downloadStateFlow.value = State.Started
        downloadProgressFlow.value = downloadProgressFlow.value + 1
    }

    private fun notifyStopped() {
        downloadStateFlow.value = State.Stopped
        "url ${downloadParams.url} download task stop.".log()
    }

    private fun notifyFailed() {
        downloadStateFlow.value = State.Failed
        "url ${downloadParams.url} download task failed.".log()
    }

    private suspend fun notifySucceed() {
        val progress = getProgress()
        progressProducer?.send(progress)
        progressProducer = null
        "url ${downloadParams.url} progress ${progress.percentStr()}".log()
        downloadStateFlow.value = State.Succeed
        "url ${downloadParams.url} download task complete.".log()
    }

    private fun Progress.isComplete(): Boolean {
        return totalSize > 0 && totalSize == downloadSize
    }
}