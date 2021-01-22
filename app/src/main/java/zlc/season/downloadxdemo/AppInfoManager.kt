package zlc.season.downloadxdemo

import retrofit2.http.GET
import retrofit2.http.Url
import zlc.season.downloadx.core.apiCreator

object AppInfoManager {
    interface Api {
        @GET
        suspend fun get(@Url url: String): AppListResp
    }

    private const val url = "https://android.myapp.com/myapp/union/apps.htm?unionId=12"

    suspend fun getAppInfoList(): List<AppListResp.AppInfo> {
        return apiCreator<Api>().get(url).appList
    }
}