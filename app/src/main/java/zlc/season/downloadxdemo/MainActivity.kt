package zlc.season.downloadxdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.broadcast
import kotlinx.coroutines.channels.produce
import zlc.season.downloadx.download
import zlc.season.downloadx.utils.log
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val url = "https://dldir1.qq.com/weixin/android/weixin7011android1600.apk"

        button.setOnClickListener {
            launch {

                //                val channel1 = get()
//
//                val result = channel1.receive()
//                textView.text = result.toString()

                val channel2 = download(url)

                for (progress in channel2) {
                    textView.text = progress.percentStr()
                }
            }

        }
//
//            launch {
//                val progress= channel.receive()
//                textView.text = progress.toString()
//            }
    }

    private suspend fun get(): ReceiveChannel<Int> =
        CoroutineScope(coroutineContext).produce { send(2) }


}

