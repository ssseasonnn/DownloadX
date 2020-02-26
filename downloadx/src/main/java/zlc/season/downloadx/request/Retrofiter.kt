package zlc.season.downloadx.request

import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


const val FAKE_BASE_URL = "http://www.example.com"

val okHttpClient: OkHttpClient = OkHttpClient().newBuilder()
    .connectTimeout(15, TimeUnit.SECONDS)
    .readTimeout(120, TimeUnit.SECONDS)
    .writeTimeout(120, TimeUnit.SECONDS)
    .build()


inline fun <reified T> request(
    baseUrl: String = FAKE_BASE_URL,
    client: OkHttpClient = okHttpClient,
    converterFactory: Converter.Factory = GsonConverterFactory.create()
): T {
    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addConverterFactory(converterFactory)
        .build()

    return retrofit.create(T::class.java)
}