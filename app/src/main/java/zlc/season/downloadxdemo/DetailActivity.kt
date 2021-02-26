package zlc.season.downloadxdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import coil.load
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import zlc.season.bracer.mutableParams
import zlc.season.bracer.params
import zlc.season.downloadx.State
import zlc.season.downloadx.download
import zlc.season.downloadxdemo.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    val binding by lazy { ActivityDetailBinding.inflate(layoutInflater) }
    var appInfo by mutableParams<AppListResp.AppInfo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.icon.load(appInfo.iconUrl)
        binding.title.text = appInfo.appName
        binding.desc.text = appInfo.editorIntro

        val downloadTask = GlobalScope.download(appInfo.apkUrl)

        downloadTask.state()
            .onEach { binding.button.setState(it) }
            .launchIn(lifecycleScope)

        binding.button.setOnClickListener {
            if (downloadTask.isStarted()) {
                downloadTask.stop()
            } else {
                downloadTask.start()
            }
        }
    }
}