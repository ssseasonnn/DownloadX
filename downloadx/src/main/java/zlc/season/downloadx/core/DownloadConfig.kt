package zlc.season.downloadx.core

import zlc.season.downloadx.helper.Default.DEFAULT_RANGE_SIZE
import zlc.season.downloadx.helper.Default.RANGE_CHECK_HEADER

class DownloadConfig(
    val header: Map<String, String> = RANGE_CHECK_HEADER,
    val rangeSize: Long = DEFAULT_RANGE_SIZE,
    val dispatcher: DownloadDispatcher = DefaultDownloadDispatcher,
    val queue: DownloadQueue = DefaultDownloadQueue,
    val validator: FileValidator = DefaultFileValidator
)