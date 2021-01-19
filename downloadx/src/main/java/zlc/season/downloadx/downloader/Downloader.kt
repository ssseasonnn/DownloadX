package zlc.season.downloadx.downloader

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor
import okhttp3.ResponseBody
import retrofit2.Response
import zlc.season.downloadx.Progress
import zlc.season.downloadx.task.DownloadConfig
import zlc.season.downloadx.task.DownloadParams

sealed class Action {
    class QueryProgress(val completableDeferred: CompletableDeferred<Progress>) : Action()
}

interface Downloader {
    var actor: SendChannel<Action>

    suspend fun queryProgress(): Progress

    suspend fun download(
        downloadParams: DownloadParams,
        downloadConfig: DownloadConfig,
        response: Response<ResponseBody>
    )
}

@OptIn(ObsoleteCoroutinesApi::class)
abstract class BaseDownloader(protected val coroutineScope: CoroutineScope) : Downloader {
    protected var totalSize: Long = 0L
    protected var downloadSize: Long = 0L
    protected var isChunked: Boolean = false

    private val progress = Progress()

    override var actor = coroutineScope.actor<Action> {
        for (action in channel) {
            if (action is Action.QueryProgress) {
                action.completableDeferred.complete(progress.also {
                    it.downloadSize = downloadSize
                    it.totalSize = totalSize
                    it.isChunked = isChunked
                })
            }
        }
    }

    override suspend fun queryProgress(): Progress {
        val ack = CompletableDeferred<Progress>()
        val queryProgress = Action.QueryProgress(ack)
        actor.send(queryProgress)
        return ack.await()
    }
}