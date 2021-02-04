package zlc.season.downloadxdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import coil.load
import zlc.season.bracer.mutableParams
import zlc.season.bracer.params
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

    }
}