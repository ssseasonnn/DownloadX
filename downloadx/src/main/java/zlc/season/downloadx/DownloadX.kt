package zlc.season.downloadx

import android.os.Environment.DIRECTORY_DOWNLOADS
import android.os.Environment.getExternalStoragePublicDirectory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import zlc.season.downloadx.downloader.DefaultDispatcher
import zlc.season.downloadx.downloader.Dispatcher
import zlc.season.downloadx.request.Request
import zlc.season.downloadx.request.RequestImpl
import zlc.season.downloadx.storage.SimpleStorage
import zlc.season.downloadx.storage.Storage
import zlc.season.downloadx.task.Task
import zlc.season.downloadx.task.TaskInfo
import zlc.season.downloadx.utils.clear
import zlc.season.downloadx.utils.log
import zlc.season.downloadx.validator.SimpleValidator
import zlc.season.downloadx.validator.Validator
import zlc.season.downloadx.watcher.Watcher
import zlc.season.downloadx.watcher.WatcherImpl
import java.io.File


val DEFAULT_SAVE_PATH: String = getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS).path

val RANGE_CHECK_HEADER = mapOf("Range" to "bytes=0-")

const val DEFAULT_RANGE_SIZE = 5L * 1024 * 1024 //5M

const val DEFAULT_MAX_CONCURRENCY = 3

fun CoroutineScope.download(url: String): ReceiveChannel<Progress> {
    return produce {
        download(url)
    }
}

/**
 * Returns a Download Flowable represent the current url.
 */
@JvmOverloads
suspend fun ProducerScope<Progress>.download(
    url: String,
    header: Map<String, String> = RANGE_CHECK_HEADER,
    maxConCurrency: Int = DEFAULT_MAX_CONCURRENCY,
    rangeSize: Long = DEFAULT_RANGE_SIZE,
    dispatcher: Dispatcher = DefaultDispatcher,
    validator: Validator = SimpleValidator,
    storage: Storage = SimpleStorage,
    request: Request = RequestImpl,
    watcher: Watcher = WatcherImpl
) {
    require(rangeSize > 1024 * 1024) { "rangeSize must be greater than 1M" }
    require(maxConCurrency > 0) { "maxConCurrency must be greater than 0" }

    download(
        Task(url),
        header = header,
        maxConCurrency = maxConCurrency,
        rangeSize = rangeSize,
        dispatcher = dispatcher,
        validator = validator,
        storage = storage,
        request = request,
        watcher = watcher
    )
}

@JvmOverloads
fun String.file(storage: Storage = SimpleStorage): File {
    return Task(this).file(storage)
}

@JvmOverloads
fun String.delete(storage: Storage = SimpleStorage) {
    Task(this).delete(storage)
}

/**
 * Returns a Download Flowable represent the current task.
 */
@JvmOverloads
suspend fun ProducerScope<Progress>.download(
    task: Task,
    header: Map<String, String> = RANGE_CHECK_HEADER,
    maxConCurrency: Int = DEFAULT_MAX_CONCURRENCY,
    rangeSize: Long = DEFAULT_RANGE_SIZE,
    dispatcher: Dispatcher = DefaultDispatcher,
    validator: Validator = SimpleValidator,
    storage: Storage = SimpleStorage,
    request: Request = RequestImpl,
    watcher: Watcher = WatcherImpl
) {
    require(rangeSize > 1024 * 1024) { "rangeSize must be greater than 1M" }
    require(maxConCurrency > 0) { "maxConCurrency must be greater than 0" }

    val taskInfo = TaskInfo(
        task = task,
        header = header,
        maxConCurrency = maxConCurrency,
        rangeSize = rangeSize,
        dispatcher = dispatcher,
        validator = validator,
        storage = storage,
        request = request,
        watcher = watcher
    )

    with(taskInfo) {
        start()
    }
}

@JvmOverloads
fun Task.file(storage: Storage = SimpleStorage): File {
    storage.load(this)
    if (isEmpty()) {
        "Task file not found".log()
    }
    return File(savePath, saveName)
}

@JvmOverloads
fun Task.delete(storage: Storage = SimpleStorage) {
    val file = file(storage)
    file.clear()
    storage.delete(this)
}
