package zlc.season.downloadx.core


open class DownloadParams(
    var url: String,
    var saveName: String = "",
    var savePath: String = "",
) {

    /**
     * Each task with unique tag.
     */
    open fun tag() = url


    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (this === other) return true

        return if (other is DownloadParams) {
            tag() == other.tag()
        } else {
            false
        }
    }

    override fun hashCode(): Int {
        return tag().hashCode()
    }
}