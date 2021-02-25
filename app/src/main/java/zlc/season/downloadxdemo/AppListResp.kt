package zlc.season.downloadxdemo


import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.Job
import zlc.season.downloadx.core.DownloadTask
import zlc.season.yasha.YashaItem

data class AppListResp(
    @SerializedName("obj")
    val appList: List<AppInfo> = listOf(),
) {
    data class AppInfo(
        @SerializedName("appId")
        val appId: Int = 0,

        @SerializedName("apkMd5")
        val apkMd5: String = "",

        @SerializedName("apkUrl")
        val apkUrl: String = "",

        @SerializedName("appDownCount")
        val appDownCount: Long = 0,

        @SerializedName("appName")
        val appName: String = "",

        @SerializedName("averageRating")
        val averageRating: Double = 0.0,

        @SerializedName("categoryId")
        val categoryId: Int = 0,

        @SerializedName("categoryName")
        val categoryName: String = "",

        @SerializedName("editorIntro")
        val editorIntro: String = "",

        @SerializedName("fileSize")
        val fileSize: Int = 0,

        @SerializedName("iconUrl")
        val iconUrl: String = "",

        @SerializedName("images")
        val images: List<String> = listOf(),

        @SerializedName("pkgName")
        val pkgName: String = "",

        @SerializedName("versionCode")
        val versionCode: Int = 0,

        @SerializedName("versionName")
        val versionName: String = ""
    ) : YashaItem {

        @Transient
        var downloadTask: DownloadTask? = null
        @Transient
        var progressJob: Job? = null
    }
}