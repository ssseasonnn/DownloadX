package zlc.season.downloadx.core

import okhttp3.ResponseBody
import retrofit2.Response
import zlc.season.downloadx.utils.contentLength
import zlc.season.downloadx.utils.isSupportRange
import java.io.File

interface DownloadDispatcher {
    fun dispatch(downloadTask: DownloadTask, resp: Response<ResponseBody>): Downloader
}

object DefaultDownloadDispatcher : DownloadDispatcher {
    override fun dispatch(downloadTask: DownloadTask, resp: Response<ResponseBody>): Downloader {
        return if (resp.isSupportRange()) {
            RangeDownloader(downloadTask.coroutineScope)
        } else {
            NormalDownloader(downloadTask.coroutineScope)
        }
    }
}

interface FileValidator {
    fun validate(
        file: File,
        param: DownloadParams,
        config: DownloadConfig,
        resp: Response<ResponseBody>
    ): Boolean
}

object DefaultFileValidator : FileValidator {
    override fun validate(
        file: File,
        param: DownloadParams,
        config: DownloadConfig,
        resp: Response<ResponseBody>
    ): Boolean {
        return file.length() == resp.contentLength()
    }
}