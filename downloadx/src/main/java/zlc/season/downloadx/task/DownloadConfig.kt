package zlc.season.downloadx.task

import kotlinx.coroutines.CoroutineScope
import zlc.season.downloadx.core.Default.DEFAULT_RANGE_SIZE
import zlc.season.downloadx.core.Default.RANGE_CHECK_HEADER
import zlc.season.downloadx.core.Headers

class DownloadConfig(
    val coroutineScope: CoroutineScope,
    val header: Headers = RANGE_CHECK_HEADER,
    val rangeSize: Long = DEFAULT_RANGE_SIZE
)