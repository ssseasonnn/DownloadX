![](usage.png)

# DownloadX

[![](https://jitpack.io/v/ssseasonnn/DownloadX.svg)](https://jitpack.io/#ssseasonnn/DownloadX)

A multi-threaded download tool written with Coroutine and Kotlin

*Read this in other languages: [中文](README.ch.md), [English](README.md), [Changelog](CHANGELOG.md)* 

## Prepare

- Add jitpack repo:

```gradle
maven { url 'https://jitpack.io' }
```
    
- Add RxDownload dependency:

```gradle
implementation "com.github.ssseasonnn:DownloadX:1.0.0"
```

## Basic Usage

```kotlin
// create download task
val downloadTask = coroutineScope.download("url")

// listen download progress
downloadTask.progress()
    .onEach { binding.button.setProgress(it)  }
    .launchIn(lifecycleScope)

// or listen download state
downloadTask.state()
    .onEach { binding.button.setState(it)  }
    .launchIn(lifecycleScope)

// start download
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
