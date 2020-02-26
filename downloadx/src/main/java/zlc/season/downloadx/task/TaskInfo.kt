package zlc.season.downloadx.task

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.coroutineScope
import zlc.season.downloadx.DEFAULT_SAVE_PATH
import zlc.season.downloadx.Progress
import zlc.season.downloadx.downloader.Dispatcher
import zlc.season.downloadx.request.Request
import zlc.season.downloadx.storage.Storage
import zlc.season.downloadx.utils.fileName
import zlc.season.downloadx.validator.Validator
import zlc.season.downloadx.watcher.Watcher

class TaskInfo(
    val task: Task,
    val header: Map<String, String>,
    val maxConCurrency: Int,
    val rangeSize: Long,
    val dispatcher: Dispatcher,
    val validator: Validator,
    val storage: Storage,
    val request: Request,
    val watcher: Watcher
) {
    suspend fun ProducerScope<Progress>.start() {
        val response = request.get(task.url, header)
        check(response.isSuccessful) { "Request failed!" }

        if (task.saveName.isEmpty()) {
            task.saveName = response.fileName()
        }
        if (task.savePath.isEmpty()) {
            task.savePath = DEFAULT_SAVE_PATH
        }

        val downloader = dispatcher.dispatch(response)
        with(downloader) {
            download(this@TaskInfo, response)
        }
    }
}