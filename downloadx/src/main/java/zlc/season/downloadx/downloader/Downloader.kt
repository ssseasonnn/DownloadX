package zlc.season.downloadx.downloader

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ProducerScope
import okhttp3.ResponseBody
import retrofit2.Response
import zlc.season.downloadx.Progress
import zlc.season.downloadx.task.TaskInfo

@UseExperimental(ExperimentalCoroutinesApi::class)
interface Downloader {
    suspend fun ProducerScope<Progress>.download(
        taskInfo: TaskInfo,
        response: Response<ResponseBody>
    )
}