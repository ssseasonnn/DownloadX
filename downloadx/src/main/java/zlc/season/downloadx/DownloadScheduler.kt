package zlc.season.downloadx

import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.scheduling.ExperimentalCoroutineDispatcher

val DOWNLOAD_IO = DownloadScheduler.IO

@UseExperimental(InternalCoroutinesApi::class)
object DownloadScheduler : ExperimentalCoroutineDispatcher() {
    private const val SCHEDULER_NAME = "DownloadX"

    val IO = blocking(4)

    override fun close() {
        throw UnsupportedOperationException("$SCHEDULER_NAME cannot be closed")
    }

    override fun toString(): String = SCHEDULER_NAME

    @InternalCoroutinesApi
    @Suppress("UNUSED")
    public fun toDebugString(): String = super.toString()
}