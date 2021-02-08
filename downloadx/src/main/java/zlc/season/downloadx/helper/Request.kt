package zlc.season.downloadx.helper

import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Streaming
import retrofit2.http.Url
import zlc.season.downloadx.helper.Default.FAKE_BASE_URL
import java.util.concurrent.TimeUnit

val okHttpClient: OkHttpClient by lazy {
    OkHttpClient().newBuilder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(120, TimeUnit.SECONDS)
        .writeTimeout(120, TimeUnit.SECONDS)
        .build()
}

inline fun <reified T> apiCreator(
    baseUrl: String = FAKE_BASE_URL,
    client: OkHttpClient = okHttpClient
): T {
    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    return retrofit.create(T::class.java)
}

interface Api {

    @GET
    @Streaming
    suspend fun get(
        @Url url: String,
        @HeaderMap headers: Map<String, String>
    ): Response<ResponseBody>
}


private val api = apiCreator<Api>()

suspend fun request(url: String, headers: Map<String, String>): Response<ResponseBody> {
    return api.get(url, headers)
}