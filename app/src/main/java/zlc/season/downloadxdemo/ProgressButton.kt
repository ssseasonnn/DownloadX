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

    fun setProgress(progress: Progress) {
        if (progress.isChunked) {
            binding.progress.isIndeterminate = true
        } else {
            binding.progress.isIndeterminate = false
            binding.progress.max = progress.totalSize.toInt()
            binding.progress.progress = progress.downloadSize.toInt()
        }
    }

    fun setState(state: State) {
        when (state) {
            is State.Waiting -> {
                binding.button.text = "等待中"
            }
            is State.Started -> {
                binding.button.text = "下载中"
            }
            is State.Failed -> {
                binding.button.text = "重试"
            }
            is State.Stopped -> {
                binding.button.text = "下载"
            }
            is State.Succeed -> {
                binding.button.text = "安装"
            }
        }
    }
}