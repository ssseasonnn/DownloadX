package zlc.season.downloadx.core

import android.os.Environment
import android.os.Environment.getExternalStoragePublicDirectory

object Default {
    const val FAKE_BASE_URL = "http://www.example.com"

    val DEFAULT_SAVE_PATH = getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path

    val RANGE_CHECK_HEADER = mapOf("Range" to "bytes=0-")

    const val DEFAULT_RANGE_SIZE = 5L * 1024 * 1024 //5M

    const val DEFAULT_MAX_CONCURRENCY = 3
}