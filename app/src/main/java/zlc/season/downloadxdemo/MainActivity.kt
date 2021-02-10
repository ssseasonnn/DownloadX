package zlc.season.downloadxdemo

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import coil.load
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import zlc.season.bracer.start
import zlc.season.downloadx.download
import zlc.season.downloadxdemo.databinding.ActivityMainBinding
import zlc.season.downloadxdemo.databinding.AppInfoItemBinding
import zlc.season.yasha.YashaDataSource
import zlc.season.yasha.YashaItem
import zlc.season.yasha.linear

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val dataSource by lazy { AppListDataSource() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        dataSource.retry.onEach {
            binding.btnRetry.visibility = if (it) View.VISIBLE else View.GONE
        }.launchIn(lifecycleScope)

        binding.btnRetry.setOnClickListener {
            dataSource.invalidate()
        }

        binding.recyclerView.linear(dataSource) {
            renderBindingItem<AppListResp.AppInfo, AppInfoItemBinding> {
                onBind {
                    itemBinding.title.text = data.appName
                    itemBinding.desc.text = data.editorIntro
                    itemBinding.icon.load(data.iconUrl)

                    itemBinding.root.setOnClickListener {
                        DetailActivity().apply {
                            appInfo = data
                        }.start(this@MainActivity)
                    }
                    itemBinding.button.setOnClickListener {
                        if (data.downloadTask == null) {
                            val downloadTask = lifecycleScope.download(data.apkUrl)
                            data.downloadTask = downloadTask
                        }

                        data.downloadTask!!.start()

                        data.downloadTask?.let {
                            it.state().combine(it.progress()) { l, r -> Pair(l, r) }
                                .onEach {
                                    itemBinding.button.setState(it.first)
                                    itemBinding.button.setProgress(it.second)
                                }
                                .launchIn(lifecycleScope)
                        }
                    }
                }
            }
        }
    }

    class AppListDataSource : YashaDataSource() {
        val retry = MutableStateFlow(false)

        override suspend fun loadInitial(): List<YashaItem> {
            return try {
                retry.value = false
                AppInfoManager.getAppInfoList()
            } catch (e: Exception) {
                retry.value = true
                emptyList()
            }
        }
    }
}

