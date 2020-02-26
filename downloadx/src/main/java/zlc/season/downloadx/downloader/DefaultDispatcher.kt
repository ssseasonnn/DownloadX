package zlc.season.downloadx.downloader

import okhttp3.ResponseBody
import retrofit2.Response
import zlc.season.downloadx.utils.isSupportRange

object DefaultDispatcher : Dispatcher {

    override fun dispatch(response: Response<ResponseBody>): Downloader {
        return if (response.isSupportRange()) {
            RangeDownloader()
        } else {
            NormalDownloader()
        }
    }
}