package zlc.season.downloadxdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import zlc.season.downloadxdemo.databinding.ActivityHistoryBinding

class HistoryActivity : AppCompatActivity() {
    val binding by lazy { ActivityHistoryBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}