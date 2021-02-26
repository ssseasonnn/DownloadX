package zlc.season.downloadxdemo

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import zlc.season.downloadx.Progress
import zlc.season.downloadx.State
import zlc.season.downloadxdemo.databinding.LayoutProgressButtonBinding

class ProgressButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    val binding = LayoutProgressButtonBinding.inflate(LayoutInflater.from(context), this, true)

    fun setState(state: State) {
        binding.progress.max = state.progress.totalSize.toInt()
        binding.progress.progress = state.progress.downloadSize.toInt()

        when (state) {
            is State.None -> {
                binding.button.text = "下载"
            }
            is State.Waiting -> {
                binding.button.text = "等待中"
            }
            is State.Downloading -> {
                binding.button.text = state.progress.percentStr()
            }
            is State.Failed -> {
                binding.button.text = "重试"
            }
            is State.Stopped -> {
                binding.button.text = "继续"
            }
            is State.Succeed -> {
                binding.button.text = "安装"
            }
        }
    }
}