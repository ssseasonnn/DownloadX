package zlc.season.downloadxdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.flow.*
import zlc.season.downloadx.download
import zlc.season.downloadx.utils.log
import zlc.season.downloadxdemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val task = download(RE)
        task.state()
            .onEach { "Main state: $it".log() }
            .launchIn(this)

        task.progress()
            .onEach { "Main progress: ${it.percentStr()}".log() }
            .launchIn(this)

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

