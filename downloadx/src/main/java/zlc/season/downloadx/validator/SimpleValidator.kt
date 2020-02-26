package zlc.season.downloadx.validator

import okhttp3.ResponseBody
import retrofit2.Response
import zlc.season.downloadx.utils.contentLength
import java.io.File

object SimpleValidator : Validator {
    override fun validate(file: File, response: Response<ResponseBody>): Boolean {
        return file.length() == response.contentLength()
    }
}