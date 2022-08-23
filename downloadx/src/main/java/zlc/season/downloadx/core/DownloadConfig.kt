package zlc.season.downloadx.core

import okhttp3.ResponseBody
import retrofit2.Response
import zlc.season.downloadx.helper.Default.DEFAULT_RANGE_CURRENCY
import zlc.season.downloadx.helper.Default.DEFAULT_RANGE_SIZE
import zlc.season.downloadx.helper.apiCreator

class DownloadConfig(
    /**
     * 禁用断点续传
     */
    val disableRangeDownload: Boolean = false,
    /**
     * 下载管理
     */
    val taskManager: TaskManager = DefaultTaskManager,
    /**
     * 下载队列
     */
    val queue: DownloadQueue = DefaultDownloadQueue.get(),

    /**
     * 自定义header
     */
    val customHeader: Map<String, String> = emptyMap(),

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

    /**
     * http client
     */
    httpClientFactory: HttpClientFactory = DefaultHttpClientFactory
) {
    private val api = apiCreator(httpClientFactory.create())

    suspend fun request(url: String, header: Map<String, String>): Response<ResponseBody> {
        val tempHeader = mutableMapOf<String, String>().also {
            it.putAll(customHeader)
            it.putAll(header)
        }
        return api.get(url, tempHeader)
    }
}