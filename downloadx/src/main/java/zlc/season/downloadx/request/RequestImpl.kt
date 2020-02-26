package zlc.season.downloadx.request

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Streaming
import retrofit2.http.Url

object RequestImpl : Request {
    private val api = request<Api>()

    override suspend fun get(
        url: String,
        headers: Map<String, String>
    ): Response<ResponseBody> {
        return api.get(url, headers)
    }

    interface Api {
        @GET
        @Streaming
        suspend fun get(
            @Url url: String,
            @HeaderMap headers: Map<String, String>
        ): Response<ResponseBody>
    }
}

