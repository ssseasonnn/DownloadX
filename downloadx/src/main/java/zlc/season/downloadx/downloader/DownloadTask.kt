package zlc.season.downloadx.downloader

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import zlc.season.downloadx.Progress
import zlc.season.downloadx.core.Default
import zlc.season.downloadx.core.request
import zlc.season.downloadx.task.DownloadConfig
import zlc.season.downloadx.task.DownloadParams
import zlc.season.downloadx.utils.fileName
import zlc.season.downloadx.utils.isSupportRange
import zlc.season.downloadx.utils.log

@ExperimentalCoroutinesApi
open class DownloadTask(
    private val downloadParams: DownloadParams,
    private val downloadConfig: DownloadConfig
) {
    private var downloadJob: Job? = null
    private var downloader: Downloader? = null

    private val coroutineScope = downloadConfig.coroutineScope ?: GlobalScope

    private val progressStateFlow = MutableStateFlow(0)

    fun start() {
        downloadJob = coroutineScope.launch {
            val response = request(downloadParams.url, downloadConfig.header)
            if (!response.isSuccessful) {
                "Url [${downloadParams.url}] request failed!".log()
                return@launch
            }

            if (downloadParams.saveName.isEmpty()) {
                downloadParams.saveName = response.fileName()
            }
            if (downloadParams.savePath.isEmpty()) {
                downloadParams.savePath = Default.DEFAULT_SAVE_PATH
            }

            downloader = if (response.isSupportRange()) {
                RangeDownloader(coroutineScope)
            } else {
                NormalDownloader(coroutineScope)
            }
            downloader?.download(downloadParams, downloadConfig, response)

            stateTrigger()
        }
        downloadJob?.start()
    }

    fun stop() {
        downloadJob?.cancel()
    }

    fun progress(interval: Long = 100): Flow<Progress> {
//        return progressStateFlow.flatMapConcat {
        return flow {
            var progress = progress()
//                if (progress.isComplete()) {
//                    emit(progress)
//                } else {
            while (downloadJob?.isActive == true && !progress.isComplete()) {
                delay(interval)
                progress = progress()
                emit(progress)
            }
        }
//            }
//        }
    }

    suspend fun progress(): Progress {
        return downloader?.queryProgress() ?: Progress()
    }

    private fun stateTrigger() {
        progressStateFlow.value = progressStateFlow.value + 1
    }

    private fun Progress.isComplete(): Boolean {
        return totalSize > 0 && totalSize == downloadSize
    }
}