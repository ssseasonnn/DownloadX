package zlc.season.downloadxdemo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import zlc.season.downloadx.download
import zlc.season.downloadxdemo.databinding.ActivityTestBinding

class TestActivity : AppCompatActivity() {
    var stateJob: Job? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val downloadTask =
            GlobalScope.download("http://imtt.dd.qq.com/sjy.40001/sjy.00002/16891/apk/A7120AEB0DE5C59E3415C33149F5F6BA.apk?fsname=com.tencent.tmgp.ibirdgame.doudizhu_1.3_3.apk&csr=81e7")

        binding.btnTest.setOnClickListener {
            downloadTask.start()
            stateJob?.cancel()
            stateJob = downloadTask.state(1000)
                .onEach {
                    Log.e("Download", "state -> $it, progress -> ${it.progress.percentStr()}")
                }
                .launchIn(lifecycleScope)
        }

    }
}