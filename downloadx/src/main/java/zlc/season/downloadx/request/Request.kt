package zlc.season.downloadx.request

import okhttp3.ResponseBody
import retrofit2.Response

interface Request {
    suspend fun get(url: String, headers: Map<String, String>): Response<ResponseBody>
}