package zlc.season.downloadxdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.flow.*
import zlc.season.downloadx.Progress
import zlc.season.downloadx.download
import zlc.season.downloadx.utils.log
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    var i = 1
    var j = 0
    val stateFlow = MutableStateFlow(1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val url = "https://dldir1.qq.com/weixin/android/weixin7011android1600.apk"

        val task = url.download(this)


//        stateFlow
//            .flatMapConcat {
//                flow {
//                    while (i % 8 != 0) {
//                        emit(++i)
//                    }
//                }
//            }
//            .onEach {
//                println(it)
//            }.launchIn(this)

        button.setOnClickListener {
            i++
            stateFlow.value = j++

//            launch {
//                test()
//            }
//            launch {
//                test1()
//            }
            task.progress(1000)
                .onEach {
                    println(it.percentStr())
                }.launchIn(this@MainActivity)
            task.start()
        }

        button1.setOnClickListener {
            task.stop()
        }
    }

    val channel = Channel<A>(10)

    suspend fun test() {
        val list = mutableListOf<A>()
        for ((i, j) in (0 until 10).withIndex()) {
            list.add(A(i))
        }

        list.forEach {
            channel.send(it)
        }
    }

    suspend fun test1() {
        repeat(3) {
            launch {
                channel.consumeEach {
                    println("A: ${it.i} handing...")
                    delay(5000)
                    println("A: ${it.i} handed")
                }
            }
        }
    }

    class A(val i: Int)

    override fun onPause() {
        super.onPause()
        cancel()
    }
}

