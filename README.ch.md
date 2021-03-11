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
implementation "com.github.ssseasonnn:DownloadX:1.0.0"
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
