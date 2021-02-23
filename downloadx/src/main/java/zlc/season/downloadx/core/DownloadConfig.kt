package zlc.season.downloadx.core

import zlc.season.downloadx.helper.Default.DEFAULT_RANGE_CURRENCY
import zlc.season.downloadx.helper.Default.DEFAULT_RANGE_SIZE
import zlc.season.downloadx.helper.Default.RANGE_CHECK_HEADER

class DownloadConfig(
    /**
     * 下载队列
     */
    val queue: DownloadQueue = DefaultDownloadQueue.get(),

    /**
     * 自定义header
     */
    customHeader: Map<String, String> = emptyMap(),

    /**
     * 分片下载每片的大小
     */
    val rangeSize: Long = DEFAULT_RANGE_SIZE,
    /**
     * 分片下载并行数量
     */
    val rangeCurrency: Int = DEFAULT_RANGE_CURRENCY,

    /**
     * 下载器分发
     */
    val dispatcher: DownloadDispatcher = DefaultDownloadDispatcher,

    /**
     * 文件校验
     */
    val validator: FileValidator = DefaultFileValidator,
) {

    val header = mutableMapOf<String, String>().also {
        it.putAll(RANGE_CHECK_HEADER)
        it.putAll(customHeader)
    }
}