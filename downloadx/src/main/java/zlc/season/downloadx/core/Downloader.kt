package zlc.season.downloadx.core

import kotlinx.coroutines.*
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor
import okhttp3.ResponseBody
import retrofit2.Response
import zlc.season.downloadx.Progress
import java.io.File

class QueryProgress(val completableDeferred: CompletableDeferred<Progress>)

interface Downloader {
    var actor: SendChannel<QueryProgress>

    suspend fun queryProgress(): Progress

    suspend fun download(
        downloadParam: DownloadParam,
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

    override var actor = GlobalScope.actor<QueryProgress>(Dispatchers.IO) {
        for (each in channel) {
            each.completableDeferred.complete(progress.also {
                it.downloadSize = downloadSize
                it.totalSize = totalSize
                it.isChunked = isChunked
            })
        }
    }

    override suspend fun queryProgress(): Progress {
        val ack = CompletableDeferred<Progress>()
        val queryProgress = QueryProgress(ack)
        actor.send(queryProgress)
        return ack.await()
    }

    fun DownloadParam.dir(): File {
        return File(savePath)
    }

    fun DownloadParam.file(): File {
        return File(savePath, saveName)
    }
}