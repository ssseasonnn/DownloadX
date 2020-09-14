package zlc.season.downloadx.storage

import android.content.Context.MODE_PRIVATE
import zlc.season.claritypotion.ClarityPotion.Companion.clarityPotion
import zlc.season.downloadx.task.DownloadParams

object SimpleStorage : MemoryStorage() {
    private val sp by lazy {
        clarityPotion.getSharedPreferences("rxdownload_simple_storage", MODE_PRIVATE)
    }

    @Synchronized
    override fun load(downloadParams: DownloadParams) {
        super.load(downloadParams)

//        if (downloadParams.isEmpty()) {
//            localLoad(downloadParams)
//            super.save(downloadParams)
//        }
    }

    @Synchronized
    override fun save(downloadParams: DownloadParams) {
        super.save(downloadParams)
        localSave(downloadParams)
    }

    @Synchronized
    override fun delete(downloadParams: DownloadParams) {
        super.delete(downloadParams)
        localDelete(downloadParams)
    }

    private fun localSave(downloadParams: DownloadParams) {
        val key = downloadParams.hashCode().toString()
//        val value = downloadParams.taskName + "\n" + downloadParams.saveName + "\n" + downloadParams.savePath

//        val editor = sp.edit()
//        editor.putString(key, value)
//        editor.apply()
    }

    private fun localLoad(downloadParams: DownloadParams) {
        val key = downloadParams.hashCode().toString()
        val value = sp.getString(key, "")

        if (!value.isNullOrEmpty()) {
            val splits = value.split("\n")
            if (splits.size == 3) {
//                downloadParams.taskName = splits[0]
//                downloadParams.saveName = splits[1]
//                downloadParams.savePath = splits[2]
            }
        }
    }

    private fun localDelete(downloadParams: DownloadParams) {
        val key = downloadParams.hashCode().toString()
        sp.edit().remove(key).apply()
    }
}