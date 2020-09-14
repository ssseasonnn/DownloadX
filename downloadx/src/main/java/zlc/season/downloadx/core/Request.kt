package zlc.season.downloadx.core

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Streaming
import retrofit2.http.Url

private val api = apiCreator<Api>()

suspend fun request(url: String, headers: Headers): Response<ResponseBody> {
    return api.get(url, headers)
}



