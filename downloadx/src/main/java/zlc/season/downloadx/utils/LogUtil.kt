package zlc.season.downloadx.utils

import android.util.Log

var LOG_ENABLE = true

const val LOG_TAG = "DownloadX"

fun <T> T.log(prefix: String = ""): T {
    if (LOG_ENABLE) {
        if (this is Throwable) {
            Log.w(LOG_TAG, prefix + this.message, this)
        } else {
            Log.d(LOG_TAG, prefix + toString())
        }
    }
    return this
}