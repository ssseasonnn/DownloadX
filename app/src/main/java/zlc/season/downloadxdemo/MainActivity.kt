package zlc.season.downloadxdemo

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import coil.load
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import okhttp3.OkHttpClient
import zlc.season.bracer.start
import zlc.season.downloadx.download
import zlc.season.downloadxdemo.databinding.ActivityMainBinding
import zlc.season.downloadxdemo.databinding.AppInfoItemBinding
import zlc.season.yasha.YashaDataSource
import zlc.season.yasha.YashaItem
import zlc.season.yasha.linear
import java.util.logging.Level
import java.util.logging.Logger


class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val dataSource by lazy { AppListDataSource() }

    private val coroutineScope = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        Logger.getLogger(OkHttpClient::class.java.name).setLevel(Level.FINE)

        dataSource.retry.onEach {
            binding.btnRetry.visibility = if (it) View.VISIBLE else View.GONE
        }.launchIn(coroutineScope)

        binding.btnRetry.setOnClickListener {
            dataSource.invalidate()
        }

        binding.recyclerView.linear(dataSource) {
            renderBindingItem<AppListResp.AppInfo, AppInfoItemBinding> {
                onAttach {
                    if (data.downloadTask == null) {
                        val downloadTask = GlobalScope.download(data.apkUrl)
                        data.downloadTask = downloadTask
                    }

                    data.downloadTask?.let {
                        data.progressJob?.cancel()
                        data.progressJob = it.state()
                            .onEach {
                                itemBinding.button.setState(it)
                            }
                            .launchIn(coroutineScope)
                    }
                }
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
                        data.downloadTask?.start()
                    }
                }

                onDetach {
                    data.progressJob?.cancel()
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
                e.printStackTrace()
                retry.value = true
                emptyList()
            }
        }
    }
}

