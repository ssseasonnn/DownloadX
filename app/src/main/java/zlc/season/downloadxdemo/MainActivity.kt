package zlc.season.downloadxdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.flow.*
import zlc.season.downloadx.download
import zlc.season.downloadxdemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val url = "https://dldir1.qq.com/weixin/android/weixin7011android1600.apk"

        val task = url.download(this)

        task.progress(1000)
            .onEach {
                println(it.percentStr())
            }.launchIn(this@MainActivity)

        binding.button.setOnClickListener {
            task.start()
        }

        binding.button1.setOnClickListener {
            task.stop()
        }
    }

    override fun onPause() {
        super.onPause()
        cancel()
    }
}

