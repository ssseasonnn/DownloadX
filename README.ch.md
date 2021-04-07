![](usage.png)

# DownloadX

[![](https://jitpack.io/v/ssseasonnn/DownloadX.svg)](https://jitpack.io/#ssseasonnn/DownloadX)

基于协程打造的下载工具, 支持多线程下载和断点续传

*Read this in other languages: [中文](README.ch.md), [English](README.md), [Changelog](CHANGELOG.md)* 

## Prepare

- 添加仓库:

```gradle
maven { url 'https://jitpack.io' }
```

- 添加依赖:

```gradle
implementation "com.github.ssseasonnn:DownloadX:1.0.2"
```

## Basic Usage

```kotlin
// 创建下载任务
val downloadTask = coroutineScope.download("url")

// 监听下载进度
downloadTask.progress()
    .onEach { binding.button.setProgress(it)  }
    .launchIn(lifecycleScope)

// 或者监听下载状态
downloadTask.state()
    .onEach { binding.button.setState(it)  }
    .launchIn(lifecycleScope)

// 开始下载
downloadTask.start()
```

## 创建任务

- 指定CoroutineScope

如果下载任务仅限于Activity或Fragment的生命周期内，那么可以直接使用Activity或Fragment的**lifecycleScope**，即可保证在Activity或Fragment销毁的时候自动结束下载任务

> lifecycleScope是androidX中的扩展，需要添加以下依赖：
> implementation 'androidx.lifecycle:lifecycle-runtime-ktx:2.2.0'

```kotlin
class DemoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        //activity销毁时，该下载任务自动停止
        val downloadTask = lifecycleScope.download("url")
        downloadTask.start()
    }
}
```

如果下载任务需要在多个Activity之间共享，或者进行后台下载，那么直接使用**GlobalScope**即可

```kotlin
class DemoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        //activity销毁时，该下载任务仍然继续下载
        val downloadTask = GlobalScope.download("url")
        downloadTask.start()
    }
}
```

- 设置保存文件名和保存路径

直接传给download方法:

```kotlin
val downloadTask = GlobalScope.download("url", "saveName", "savePath")
```

创建自定义DownloadParam:

```kotlin
val downloadParam = DownloadParam("url", "saveName", "savePath")
val downloadTask = lifecycleScope.download(downloadParam)
```

默认情况下，我们使用**url**作为**DownloadTask**的唯一标示，当需要改变这一默认行为时，可以自定义自己的**DownloadParam**：

```kotlin
class CustomDownloadParam(url: String, saveName: String, savePath: String) : DownloadParam(url, saveName, savePath) {
    override fun tag(): String {
        // 使用文件路径作为唯一标示
        return savePath + saveName
    }
}

val customDownloadParam = CustomDownloadParam("url", "saveName", "savePath")
val downloadTask = lifecycleScope.download(customDownloadParam)
```

在多个页面使用同样的标识（例如相同的url）创建下载任务时，将会返回同一个DownloadTask，例如：

```kotlin
// 同一个url
val url = "xxxx"

class DemoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //创建下载任务
        val downloadTask = GlobalScope.download(url)

        downloadTask.progress()
            .onEach { progress ->  /* 更新进度 */ }
            .launchIn(lifecycleScope)

        downloadTask.start()
    }
}

class OtherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //以相同的url创建下载任务，即可获取上一个页面创建的下载任务
        val downloadTask = GlobalScope.download(url)

        downloadTask.progress()
            .onEach { progress ->  /* 更新进度 */ }
            .launchIn(lifecycleScope)

        downloadTask.start()
    }
}
```

基于此，可以在任意多个页面中共享同一个下载进度和下载状态

## 进度和状态

- 只监听进度

在某些场景只需要下载的进度时，可使用这种方式

```kotlin
// 创建任务
val downloadTask = lifecycleScope.download("url")

downloadTask.progress()
    .onEach { progress ->  /* 更新进度 */ }
    .launchIn(lifecycleScope) // 使用lifecycleScope

//开始下载
downloadTask.start()
```

> 利用**lifecycleScope**可确保在Activity或Fragment销毁的时候自动解除监听


可以为progress()方法设置更新间隔，默认是200ms更新一次，如：

```kotlin
downloadTask.progress(500) // 设置为500ms更新一次进度
    .onEach { progress ->  
        // 更新进度
        setProgress(progress)
    }
    .launchIn(lifecycleScope)
```

- 监听下载状态和进度

当需要下载状态和下载进度的时候，使用这种方式获取

```kotlin
// 创建任务
val downloadTask = lifecycleScope.download("url")

downloadTask.state()
    .onEach { state ->  
        // 更新状态
        setState(state)
        // 更新进度
        setProgress(state.progress)
    }
    .launchIn(lifecycleScope)

//开始下载
downloadTask.start()
```

> state有以下值：**None,Waiting,Downloading,Stopped,Failed,Succeed**

同样的，可以为state()方法设置进度更新间隔


## 启动和停止

- 开始下载

```kotlin
downloadTask.start()
```

- 停止下载

```kotlin
downloadTask.stop()
```

- 删除下载

```kotlin
downloadTask.remove()
```

## License

> ```
> Copyright 2021 Season.Zlc
>
> Licensed under the Apache License, Version 2.0 (the "License");
> you may not use this file except in compliance with the License.
> You may obtain a copy of the License at
>
>    http://www.apache.org/licenses/LICENSE-2.0
>
> Unless required by applicable law or agreed to in writing, software
> distributed under the License is distributed on an "AS IS" BASIS,
> WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
> See the License for the specific language governing permissions and
> limitations under the License.
> ```
