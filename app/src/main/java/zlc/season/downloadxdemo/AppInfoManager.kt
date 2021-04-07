package zlc.season.downloadxdemo

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url

object AppInfoManager {
    interface Api {
        @GET
        suspend fun get(@Url url: String): AppListResp
    }

    private const val url = "https://android.myapp.com/myapp/union/apps.htm?unionId=12"

    private fun apiCreator(client: OkHttpClient): Api {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://www.example.com")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(Api::class.java)
    }

    suspend fun getAppInfoList(): List<AppListResp.AppInfo> {
        return apiCreator(OkHttpClient().newBuilder().build()).get(url).appList
    }
}