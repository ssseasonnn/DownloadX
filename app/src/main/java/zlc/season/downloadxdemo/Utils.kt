package zlc.season.downloadxdemo

import android.content.Context
import android.content.Intent
import android.content.Intent.*
import android.net.Uri.fromFile
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.N
import androidx.core.content.FileProvider.getUriForFile
import java.io.File

fun Context.installApk(file: File) {
    val intent = Intent(ACTION_VIEW)
    val authority = "$packageName.provider"
    val uri = if (SDK_INT >= N) {
        getUriForFile(this, authority, file)
    } else {
        fromFile(file)
    }
    intent.setDataAndType(uri, "application/vnd.android.package-archive")
    intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
    intent.addFlags(FLAG_GRANT_READ_URI_PERMISSION)
    startActivity(intent)
}