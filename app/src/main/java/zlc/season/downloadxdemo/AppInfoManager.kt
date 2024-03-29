package zlc.season.downloadxdemo

import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object AppInfoManager {
    suspend fun getAppInfoList(): List<AppListResp.AppInfo> {
        return withContext(Dispatchers.IO) {
            val list = Gson().fromJson(appListJson, AppListResp::class.java)
            list.appList
        }
    }

    val appListJson = """
{
  "appList": [
    {
      "pkgName": "com.tencent.ggame",
      "channelId": "",
      "source": 52513621,
      "appId": 52513588,
      "apkId": 102432290,
      "appName": "腾讯广东麻将",
      "fileSize": 52489311,
      "versionCode": 174,
      "versionName": "1.7.4",
      "apkUrl": "http://imtt.dd.qq.com/sjy.40001/sjy.00001/16891/apk/69E7E4E87B798032925A2CA9D99F4F22.apk?fsname=com.tencent.ggame_1.7.4_174.apk&csr=81e7",
      "totalDownloadTimes": 1892987,
      "shortDesc": "正宗广东麻将手游",
      "apkMd5": "69E7E4E87B798032925A2CA9D99F4F22",
      "minSdkVersion": 16,
      "parentCategoryID": -2,
      "signatureMd5": "A8DF121F79960593B23A558E2154FFBA",
      "categoryId": 121,
      "categoryName": "网络游戏",
      "averageRating": 3,
      "publishTime": 0,
      "realTimeReportFlag": 0,
      "extData": "",
      "logoUrl": "https://pp.myapp.com/ma_icon/0/icon_52513588_1658376667/256",
      "iconUrl": "https://pp.myapp.com/ma_icon/0/icon_52513588_1658376667/96",
      "recommendId": "BwYCGQIAAw+ZEBUsRgxHTlhuZ3RpcGdYR3RRASxgGXYAiAyWAKyyYwNTsckACAoAATIAAxD5QAILCgACMgADEPxABwsKAAMyAAMQ/UAICwoABDIAAxD+QAgLCgAFMgADEP9ABgsKAAYyAAMRB0ACCwoABzIAAxEIQAILCgAIMgADEQlAAgvVQDlLKgNxdY7s/A/wEAP2EQ8GCeaWl+WcsOS4uyABugv2EjE1MjUxMzU4OF8xNjYwNzM0MzQ1ODE3NTI0NzU5M180MjA1NDk3Nzk1NjE4MjU2OTc2+BMADQYCMTAWEDQ3OjAuMDQxOTA4MjMxOjAGAjEzFm10dz0wOnRndz0wOnRhZ3c9MDpxaXM9MDpxaXI9MDpsZGFzPTAuMTU0MDc3NzQ1OmN2cj0wOnFtMnNpbT0wOm1hdGNoX3R5cGU9MDpzZWFyY2hfbHZsPTA6cnFfbWF0Y2g6NTpxY19tYXRjaDo1BgQyMDE4FgswLjAwMzEzMTA1NAYEMjAxORYJ5paX5Zyw5Li7BghhY2N1cmF0ZRYBMQYEcGN2chYLMC4wODQzMTEzNjYGBXF1ZXJ5FgnmlpflnLDkuLsGCnF1ZXJ5X2ZsYWcWATEGDHJld3JpdGVxdWVyeRYABgN0Z3cWBjAuMDAwMQYCdHcWBjAuMDAwMQYJdXNlcl90eXBlFgEwBgZ6cHJpY2UWATD1FAAAAAAAAAAA/f8ADH8ABBDg6x0="
    },
    {
      "pkgName": "com.tencent.qqgame.qqhlupwvga",
      "channelId": "",
      "source": 10103134,
      "appId": 10103101,
      "apkId": 104735997,
      "appName": "欢乐升级（腾讯）",
      "fileSize": 130472533,
      "versionCode": 43020,
      "versionName": "4.3.2",
      "apkUrl": "http://imtt.dd.qq.com/sjy.40001/sjy.00001/16891/apk/C2F17F4006AB308BAF0B29D102DD6566.apk?fsname=com.tencent.qqgame.qqhlupwvga_4.3.2_43020.apk&csr=81e7",
      "totalDownloadTimes": 26475921,
      "shortDesc": "腾讯官方出品的欢乐升级",
      "apkMd5": "C2F17F4006AB308BAF0B29D102DD6566",
      "minSdkVersion": 16,
      "parentCategoryID": -2,
      "signatureMd5": "F6A0BB7245074B9F080D03796F8919DB",
      "categoryId": 148,
      "categoryName": "棋牌中心",
      "averageRating": 4,
      "publishTime": 0,
      "realTimeReportFlag": 0,
      "extData": "",
      "logoUrl": "https://pp.myapp.com/ma_icon/0/icon_10103101_1657014288/256",
      "iconUrl": "https://pp.myapp.com/ma_icon/0/icon_10103101_1657014288/96",
      "recommendId": "BwYCGQIAAw+ZEBUsRgxHTlhuZ3RpcGdYR3RRASxgGnYAiAyWAKyyYwNTsckACAoAATIAAxD5QAILCgACMgADEPxABwsKAAMyAAMQ/UAICwoABDIAAxD+QAgLCgAFMgADEP9ABgsKAAYyAAMRB0ACCwoABzIAAxEIQAILCgAIMgADEQlAAgvVQDkAbDlxdY7s/A/wEAP2EQ8GCeaWl+WcsOS4uyABugv2EjExMDEwMzEwMV8xNjYwNzM0MzQ1ODE3NTI0NzU5M180MjA1NDk3Nzk1NjE4MjU2OTc2+BMADQYCMTAWEDQ3OjAuMTAyMDg3Mjc5OjAGAjEzFm10dz0wOnRndz0wOnRhZ3c9MDpxaXM9MDpxaXI9MDpsZGFzPTAuODk3NjI3NTY0OmN2cj0wOnFtMnNpbT0wOm1hdGNoX3R5cGU9MDpzZWFyY2hfbHZsPTA6cnFfbWF0Y2g6NTpxY19tYXRjaDo1BgQyMDE4FgswLjAwOTE3ODkyNgYEMjAxORYJ5paX5Zyw5Li7BghhY2N1cmF0ZRYBMQYEcGN2chYLMC4wODMzMzgxNzEGBXF1ZXJ5FgnmlpflnLDkuLsGCnF1ZXJ5X2ZsYWcWATEGDHJld3JpdGVxdWVyeRYABgN0Z3cWBjAuMDAwMQYCdHcWBjAuMDAwMQYJdXNlcl90eXBlFgEwBgZ6cHJpY2UWATD1FAAAAAAAAAAA/f8ADH8ABBDg6x0="
    },
    {
      "pkgName": "com.ourgame.mahjong.danji",
      "channelId": "",
      "source": 273687,
      "appId": 273654,
      "apkId": 105063525,
      "appName": "单机麻将-开心版",
      "fileSize": 60238101,
      "versionCode": 31927,
      "versionName": "7.3.19.27",
      "apkUrl": "http://imtt.dd.qq.com/sjy.40001/sjy.00002/16891/apk/49B44FE0E45A7FF1AB02AF9B348022DC.apk?fsname=com.ourgame.mahjong.danji_7.3.19.27_31927.apk&csr=81e7",
      "totalDownloadTimes": 13671289,
      "shortDesc": "上桌论英雄，争先当雀神！",
      "apkMd5": "49B44FE0E45A7FF1AB02AF9B348022DC",
      "minSdkVersion": 17,
      "parentCategoryID": -2,
      "signatureMd5": "7F49616F29A5888427DA005028176EEE",
      "categoryId": 148,
      "categoryName": "棋牌中心",
      "averageRating": 4,
      "publishTime": 0,
      "realTimeReportFlag": 0,
      "extData": "",
      "logoUrl": "https://pp.myapp.com/ma_icon/0/icon_273654_1658307138/256",
      "iconUrl": "https://pp.myapp.com/ma_icon/0/icon_273654_1658307138/96",
      "recommendId": "BwYCFgIAAw+ZEBUsRgxHTlhuZ3RpcGdYR3RRASxgG3YAiAyWAKyyYwNTsckACAoAATIAAxD5QAILCgACMgADEPxABwsKAAMyAAMQ/UAICwoABDIAAxD+QAgLCgAFMgADEP9ABgsKAAYyAAMRB0ACCwoABzIAAxEIQAILCgAIMgADEQlAAgvVQDhXbPFxdY7s/A/wEAP2EQ8GCeaWl+WcsOS4uyABugv2Ei8yNzM2NTRfMTY2MDczNDM0NTgxNzUyNDc1OTNfNDIwNTQ5Nzc5NTYxODI1Njk3NvgTAA0GAjEwFhA0NzowLjA0NTU4OTUwOTowBgIxMxZsdHc9MDp0Z3c9MDp0YWd3PTA6cWlzPTA6cWlyPTA6bGRhcz0wLjE1Nzk0NTYyOmN2cj0wOnFtMnNpbT0wOm1hdGNoX3R5cGU9MDpzZWFyY2hfbHZsPTA6cnFfbWF0Y2g6NTpxY19tYXRjaDo1BgQyMDE4FgswLjAwMzkzMTA2NgYEMjAxORYJ5paX5Zyw5Li7BghhY2N1cmF0ZRYBMQYEcGN2chYLMC4wODExMzc2ODcGBXF1ZXJ5FgnmlpflnLDkuLsGCnF1ZXJ5X2ZsYWcWATEGDHJld3JpdGVxdWVyeRYABgN0Z3cWBjAuMDAwMQYCdHcWBjAuMDAwMQYJdXNlcl90eXBlFgEwBgZ6cHJpY2UWATD1FAAAAAAAAAAA/f8ADH8ABBDg6x0="
    },
    {
      "pkgName": "com.gdy.yyb",
      "channelId": "",
      "source": 52637269,
      "appId": 52637236,
      "apkId": 104990247,
      "appName": "干瞪眼",
      "fileSize": 45351985,
      "versionCode": 70309,
      "versionName": "7.3.9",
      "apkUrl": "http://imtt.dd.qq.com/sjy.40001/sjy.00002/16891/apk/74043F69111880A873832E75B1535AC3.apk?fsname=com.gdy.yyb_7.3.9_70309.apk&csr=81e7",
      "totalDownloadTimes": 92229,
      "shortDesc": "一轮出完让他干瞪眼",
      "apkMd5": "74043F69111880A873832E75B1535AC3",
      "minSdkVersion": 14,
      "parentCategoryID": -2,
      "signatureMd5": "172C769C70E4586A9359A2ECEA2649B4",
      "categoryId": 148,
      "categoryName": "棋牌中心",
      "averageRating": 3,
      "publishTime": 0,
      "realTimeReportFlag": 0,
      "extData": "",
      "logoUrl": "https://pp.myapp.com/ma_icon/0/icon_52637236_1658109485/256",
      "iconUrl": "https://pp.myapp.com/ma_icon/0/icon_52637236_1658109485/96",
      "recommendId": "BwYCGQIAAw+ZEBUsRgxHTlhuZ3RpcGdYR3RRASxgHHYAiAyWAKyyYwNTsckACAoAATIAAxD5QAILCgACMgADEPxABwsKAAMyAAMQ/UAICwoABDIAAxD+QAgLCgAFMgADEP9ABgsKAAYyAAMRB0ACCwoABzIAAxEIQAILCgAIMgADEQlAAgvVQDVkkGlxdY7s/A/wEAP2EQ8GCeaWl+WcsOS4uyABugv2EjE1MjYzNzIzNl8xNjYwNzM0MzQ1ODE3NTI0NzU5M180MjA1NDk3Nzk1NjE4MjU2OTc2+BMADQYCMTAWEDQ3OjAuMDM4OTE1MDU4OjAGAjEzFm10dz0wOnRndz0wOnRhZ3c9MDpxaXM9MDpxaXI9MDpsZGFzPTAuOTkwNzQ3OTU2OmN2cj0wOnFtMnNpbT0wOm1hdGNoX3R5cGU9MDpzZWFyY2hfbHZsPTA6cnFfbWF0Y2g6NTpxY19tYXRjaDo1BgQyMDE4FgswLjAwMjIxNzkzNQYEMjAxORYJ5paX5Zyw5Li7BghhY2N1cmF0ZRYBMQYEcGN2chYLMC4wNzEzMDg3NjIGBXF1ZXJ5FgnmlpflnLDkuLsGCnF1ZXJ5X2ZsYWcWATEGDHJld3JpdGVxdWVyeRYABgN0Z3cWBjAuMDAwMQYCdHcWBjAuMDAwMQYJdXNlcl90eXBlFgEwBgZ6cHJpY2UWATD1FAAAAAAAAAAA/f8ADH8ABBDg6x0="
    },
    {
      "pkgName": "com.tencent.tmgp.jinxiuddz",
      "channelId": "",
      "source": 54172738,
      "appId": 54172705,
      "apkId": 98945691,
      "appName": "英雄斗地主",
      "fileSize": 84962197,
      "versionCode": 1,
      "versionName": "1.27",
      "apkUrl": "http://imtt.dd.qq.com/sjy.40001/sjy.00002/16891/apk/E9C56679E0F72140578F8B7AC5E3C536.apk?fsname=com.tencent.tmgp.jinxiuddz_1.27_1.apk&csr=81e7",
      "totalDownloadTimes": 4370,
      "shortDesc": "",
      "apkMd5": "E9C56679E0F72140578F8B7AC5E3C536",
      "minSdkVersion": 16,
      "parentCategoryID": -2,
      "signatureMd5": "B08E94AB2AE6AD9E8696B32F1AD823F7",
      "categoryId": 148,
      "categoryName": "棋牌中心",
      "averageRating": 3,
      "publishTime": 0,
      "realTimeReportFlag": 0,
      "extData": "",
      "logoUrl": "https://pp.myapp.com/ma_icon/0/icon_54172705_1635823131/256",
      "iconUrl": "https://pp.myapp.com/ma_icon/0/icon_54172705_1635823131/96",
      "recommendId": "BwYCHAIAAw+ZEBUsRgxHTlhuZ3RpcGdYR3RRASxgHXYAiAyWAKyyYwNTsckACAoAATIAAxD5QAILCgACMgADEPxABwsKAAMyAAMQ/UAICwoABDIAAxD+QAgLCgAFMgADEP9ABgsKAAYyAAMRB0ACCwoABzIAAxEIQAILCgAIMgADEQlAAgvVQDKlCMAfIS3s/A/wEAP2EQ8GCeaWl+WcsOS4uyABugv2EjE1NDE3MjcwNV8xNjYwNzM0MzQ1ODE3NTI0NzU5M180MjA1NDk3Nzk1NjE4MjU2OTc2+BMADQYCMTAWGjc5OjAuMjE2MzM1MjU0OjAuODQzMjY2MTc0BgIxMxZodHc9MC42NTp0Z3c9MDp0YWd3PTA6cWlzPTM6cWlyPTAuNjpsZGFzPTA6Y3ZyPTA6cW0yc2ltPTA6bWF0Y2hfdHlwZT0yOnNlYXJjaF9sdmw9MjpycV9tYXRjaDo1OnFjX21hdGNoOjUGBDIwMTgWCzAuMDMwMTA1NDcyBgQyMDE5FgnmlpflnLDkuLsGCGFjY3VyYXRlFgExBgRwY3ZyFgswLjA1OTk4MTg4MwYFcXVlcnkWCeaWl+WcsOS4uwYKcXVlcnlfZmxhZxYBMQYMcmV3cml0ZXF1ZXJ5FgAGA3RndxYGMC4wMDAxBgJ0dxYEMC42NQYJdXNlcl90eXBlFgEwBgZ6cHJpY2UWATD1FAAAAAAAAAAA/f8ADH8ABBDg6x0="
    },
    {
      "pkgName": "com.tencent.tmgp.lljjyyb2",
      "channelId": "",
      "source": 54205905,
      "appId": 54205872,
      "apkId": 105434988,
      "appName": "乐乐竞技斗地主",
      "fileSize": 60127573,
      "versionCode": 2200,
      "versionName": "2.2.0",
      "apkUrl": "http://imtt.dd.qq.com/sjy.40001/sjy.00002/16891/apk/22EF8602EB5EB244D9B60F872FDCD91C.apk?fsname=com.tencent.tmgp.lljjyyb2_2.2.0_2200.apk&csr=81e7",
      "totalDownloadTimes": 1735,
      "shortDesc": "",
      "apkMd5": "22EF8602EB5EB244D9B60F872FDCD91C",
      "minSdkVersion": 19,
      "parentCategoryID": -2,
      "signatureMd5": "D6AE8A16AB9B034460663473405385E1",
      "categoryId": 148,
      "categoryName": "棋牌中心",
      "averageRating": 5,
      "publishTime": 0,
      "realTimeReportFlag": 0,
      "extData": "",
      "logoUrl": "https://pp.myapp.com/ma_icon/0/icon_54205872_1658914399/256",
      "iconUrl": "https://pp.myapp.com/ma_icon/0/icon_54205872_1658914399/96",
      "recommendId": "BwYCGwIAAw+ZEBUsRgxHTlhuZ3RpcGdYR3RRASxgHnYAiAyWAKyyYwNTsckACAoAATIAAxD5QAILCgACMgADEPxABwsKAAMyAAMQ/UAICwoABDIAAxD+QAgLCgAFMgADEP9ABgsKAAYyAAMRB0ACCwoABzIAAxEIQAILCgAIMgADEQlAAgvVQDIx1z+fIS3s/A/wEAP2EQ8GCeaWl+WcsOS4uyABugv2EjE1NDIwNTg3Ml8xNjYwNzM0MzQ1ODE3NTI0NzU5M180MjA1NDk3Nzk1NjE4MjU2OTc2+BMADQYCMTAWGTc5OjAuMjIxNzQzMTM6MC44NjY1MjQ1NTIGAjEzFmh0dz0wLjY1OnRndz0wOnRhZ3c9MDpxaXM9MzpxaXI9MC41OmxkYXM9MDpjdnI9MDpxbTJzaW09MDptYXRjaF90eXBlPTI6c2VhcmNoX2x2bD0yOnJxX21hdGNoOjU6cWNfbWF0Y2g6NQYEMjAxOBYLMC4wMzIwMTc1MDEGBDIwMTkWCeaWl+WcsOS4uwYIYWNjdXJhdGUWATEGBHBjdnIWCzAuMDU4NDgxOTY5BgVxdWVyeRYJ5paX5Zyw5Li7BgpxdWVyeV9mbGFnFgExBgxyZXdyaXRlcXVlcnkWAAYDdGd3FgYwLjAwMDEGAnR3FgQwLjY1Bgl1c2VyX3R5cGUWATAGBnpwcmljZRYBMPUUAAAAAAAAAAD9/wAMfwAEEODrHQ=="
    },
    {
      "pkgName": "com.cbfq.srddz",
      "channelId": "",
      "source": 52515798,
      "appId": 52515765,
      "apkId": 105830041,
      "appName": "乐享四人斗地主",
      "fileSize": 74401217,
      "versionCode": 929,
      "versionName": "9.2.9",
      "apkUrl": "http://imtt.dd.qq.com/sjy.40001/sjy.00002/16891/apk/DEC4815412BFEDFDD02308835FF74999.apk?fsname=com.cbfq.srddz_9.2.9_929.apk&csr=81e7",
      "totalDownloadTimes": 356940,
      "shortDesc": "新用户登录即领可领取5元话费哦",
      "apkMd5": "DEC4815412BFEDFDD02308835FF74999",
      "minSdkVersion": 21,
      "parentCategoryID": -2,
      "signatureMd5": "1DAF2C9DECC930C11D4ADB45C6B69F82",
      "categoryId": 148,
      "categoryName": "棋牌中心",
      "averageRating": 4,
      "publishTime": 0,
      "realTimeReportFlag": 0,
      "extData": "",
      "logoUrl": "https://pp.myapp.com/ma_icon/0/icon_52515765_1660287617/256",
      "iconUrl": "https://pp.myapp.com/ma_icon/0/icon_52515765_1660287617/96",
      "recommendId": "BwYCGAIAAw+ZEBUsRgxHTlhuZ3RpcGdYR3RRASxgH3YAiAyWAKyyYwNTsckACAoAATIAAxD5QAILCgACMgADEPxABwsKAAMyAAMQ/UAICwoABDIAAxD+QAgLCgAFMgADEP9ABgsKAAYyAAMRB0ACCwoABzIAAxEIQAILCgAIMgADEQlAAgvVQDF5pP/xdY7s/A/wEAP2EQ8GCeaWl+WcsOS4uyABugv2EjE1MjUxNTc2NV8xNjYwNzM0MzQ1ODE3NTI0NzU5M180MjA1NDk3Nzk1NjE4MjU2OTc2+BMADQYCMTAWEDQ3OjAuMDUyMDQyODQxOjAGAjEzFm10dz0wOnRndz0wOnRhZ3c9MDpxaXM9MDpxaXI9MDpsZGFzPTAuOTg1Nzc1NjE5OmN2cj0wOnFtMnNpbT0wOm1hdGNoX3R5cGU9MDpzZWFyY2hfbHZsPTA6cnFfbWF0Y2g6NTpxY19tYXRjaDo1BgQyMDE4FgowLjAwMzQ3MzEyBgQyMDE5FgnmlpflnLDkuLsGCGFjY3VyYXRlFgExBgRwY3ZyFgswLjA1ODI0OTkxMwYFcXVlcnkWCeaWl+WcsOS4uwYKcXVlcnlfZmxhZxYBMQYMcmV3cml0ZXF1ZXJ5FgAGA3RndxYGMC4wMDAxBgJ0dxYGMC4wMDAxBgl1c2VyX3R5cGUWATAGBnpwcmljZRYBMPUUAAAAAAAAAAD9/wAMfwAEEODrHQ=="
    },
    {
      "pkgName": "com.tencent.tmgp.speedmobile",
      "channelId": "",
      "source": 52488608,
      "appId": 52488575,
      "apkId": 104549788,
      "appName": "QQ飞车手游",
      "fileSize": 2096029603,
      "versionCode": 1320002188,
      "versionName": "1.32.0.2188",
      "apkUrl": "http://imtt.dd.qq.com/sjy.40001/sjy.00001/16891/apk/D09201BF06A2EAFA68833DDAEEB38A5F.apk?fsname=com.tencent.tmgp.speedmobile_1.32.0.2188_1320002188.apk&csr=81e7",
      "totalDownloadTimes": 113372898,
      "shortDesc": "经典国民竞速手游",
      "apkMd5": "D09201BF06A2EAFA68833DDAEEB38A5F",
      "minSdkVersion": 21,
      "parentCategoryID": -2,
      "signatureMd5": "9BCBAFE32AE8382CC224F5AAB0EE8383",
      "categoryId": 151,
      "categoryName": "体育竞速",
      "averageRating": 4,
      "publishTime": 0,
      "realTimeReportFlag": 0,
      "extData": "",
      "logoUrl": "https://pp.myapp.com/ma_icon/0/icon_52488575_1658377825/256",
      "iconUrl": "https://pp.myapp.com/ma_icon/0/icon_52488575_1658377825/96",
      "recommendId": "BwYCGQIAAw+ZEBUsRgxHTlhuZ3RpcGdYR3RRASxgIHYAiAyWAKyyYwNTsckACAoAATIAAxD5QAILCgACMgADEPxABwsKAAMyAAMQ/UAICwoABDIAAxD+QAgLCgAFMgADEP9ABgsKAAYyAAMRB0ACCwoABzIAAxEIQAILCgAIMgADEQlAAgvVQCxOKZ7i6xzs/A/wEAP2EQ8GCeaWl+WcsOS4uyABugv2EjE1MjQ4ODU3NV8xNjYwNzM0MzQ1ODE3NTI0NzU5M180MjA1NDk3Nzk1NjE4MjU2OTc2+BMADQYCMTAWEDQ3OjAuMDY5MTU0MjczOjAGAjEzFm10dz0wOnRndz0wOnRhZ3c9MDpxaXM9MDpxaXI9MDpsZGFzPTAuMDQxNzU1ODYxOmN2cj0wOnFtMnNpbT0wOm1hdGNoX3R5cGU9MDpzZWFyY2hfbHZsPTA6cnFfbWF0Y2g6NTpxY19tYXRjaDo1BgQyMDE4FgswLjAwNjc4MzA4OQYEMjAxORYJ5paX5Zyw5Li7BghhY2N1cmF0ZRYBMQYEcGN2chYLMC4wNDcxNzQ4NzEGBXF1ZXJ5FgnmlpflnLDkuLsGCnF1ZXJ5X2ZsYWcWATEGDHJld3JpdGVxdWVyeRYABgN0Z3cWBjAuMDAwMQYCdHcWBjAuMDAwMQYJdXNlcl90eXBlFgEwBgZ6cHJpY2UWATD1FAAAAAAAAAAA/f8ADH8ABBDg6x0="
    },
    {
      "pkgName": "com.qqgame.happymj",
      "channelId": "",
      "source": 10125401,
      "appId": 10125368,
      "apkId": 105120675,
      "appName": "腾讯欢乐麻将全集",
      "fileSize": 270064595,
      "versionCode": 77630,
      "versionName": "7.7.63",
      "apkUrl": "http://imtt.dd.qq.com/sjy.40001/sjy.00001/16891/apk/D830417642DF843242A0BD78601D1B14.apk?fsname=com.qqgame.happymj_7.7.63_77630.apk&csr=81e7",
      "totalDownloadTimes": 327695044,
      "shortDesc": "全国各地麻将玩法合集",
      "apkMd5": "D830417642DF843242A0BD78601D1B14",
      "minSdkVersion": 16,
      "parentCategoryID": -2,
      "signatureMd5": "F6A0BB7245074B9F080D03796F8919DB",
      "categoryId": 148,
      "categoryName": "棋牌中心",
      "averageRating": 4,
      "publishTime": 0,
      "realTimeReportFlag": 0,
      "extData": "",
      "logoUrl": "https://pp.myapp.com/ma_icon/0/icon_10125368_1658475371/256",
      "iconUrl": "https://pp.myapp.com/ma_icon/0/icon_10125368_1658475371/96",
      "recommendId": "BwYCGQIAAw+ZEBUsRgxHTlhuZ3RpcGdYR3RRASxgIXYAiAyWAKyyYwNTsckACAoAATIAAxD5QAILCgACMgADEPxABwsKAAMyAAMQ/UAICwoABDIAAxD+QAgLCgAFMgADEP9ABgsKAAYyAAMRB0ACCwoABzIAAxEIQAILCgAIMgADEQlAAgvVQCaIMK7i6xzs/A/wEAP2EQ8GCeaWl+WcsOS4uyABugv2EjExMDEyNTM2OF8xNjYwNzM0MzQ1ODE3NTI0NzU5M180MjA1NDk3Nzk1NjE4MjU2OTc2+BMADQYCMTAWEDQ3OjAuMTczNTE3MjM1OjAGAjEzFm10dz0wOnRndz0wOnRhZ3c9MDpxaXM9MDpxaXI9MDpsZGFzPTAuMTc3NTYxMjgzOmN2cj0wOnFtMnNpbT0wOm1hdGNoX3R5cGU9MDpzZWFyY2hfbHZsPTA6cnFfbWF0Y2g6NTpxY19tYXRjaDo1BgQyMDE4FgswLjAxODIyNDE0MQYEMjAxORYJ5paX5Zyw5Li7BghhY2N1cmF0ZRYBMQYEcGN2chYLMC4wMzc1NTI2NTUGBXF1ZXJ5FgnmlpflnLDkuLsGCnF1ZXJ5X2ZsYWcWATEGDHJld3JpdGVxdWVyeRYABgN0Z3cWBjAuMDAwMQYCdHcWBjAuMDAwMQYJdXNlcl90eXBlFgEwBgZ6cHJpY2UWATD1FAAAAAAAAAAA/f8ADH8ABBDg6x0="
    },
    {
      "pkgName": "com.tencent.tmgp.ibirdgame.doudizhu",
      "channelId": "",
      "source": 54211549,
      "appId": 54211516,
      "apkId": 100771090,
      "appName": "笨鸟斗地主",
      "fileSize": 111990879,
      "versionCode": 3,
      "versionName": "1.3",
      "apkUrl": "http://imtt.dd.qq.com/sjy.40001/sjy.00002/16891/apk/A7120AEB0DE5C59E3415C33149F5F6BA.apk?fsname=com.tencent.tmgp.ibirdgame.doudizhu_1.3_3.apk&csr=81e7",
      "totalDownloadTimes": 1030,
      "shortDesc": "",
      "apkMd5": "A7120AEB0DE5C59E3415C33149F5F6BA",
      "minSdkVersion": 21,
      "parentCategoryID": -2,
      "signatureMd5": "B86815FCC30CFAE5EFCCAA07FD9F6C52",
      "categoryId": 148,
      "categoryName": "棋牌中心",
      "averageRating": 0,
      "publishTime": 0,
      "realTimeReportFlag": 0,
      "extData": "",
      "logoUrl": "https://pp.myapp.com/ma_icon/0/icon_54211516_1640055072/256",
      "iconUrl": "https://pp.myapp.com/ma_icon/0/icon_54211516_1640055072/96",
      "recommendId": "BwYCGwIAAw+ZEBUsRgxHTlhuZ3RpcGdYR3RRASxgInYAiAyWAKyyYwNTsckACAoAATIAAxD5QAILCgACMgADEPxABwsKAAMyAAMQ/UAICwoABDIAAxD+QAgLCgAFMgADEP9ABgsKAAYyAAMRB0ACCwoABzIAAxEIQAILCgAIMgADEQlAAgvVQCH2J+A+Qlvs/A/wEAP2EQ8GCeaWl+WcsOS4uyABugv2EjE1NDIxMTUxNl8xNjYwNzM0MzQ1ODE3NTI0NzU5M180MjA1NDk3Nzk1NjE4MjU2OTc2+BMADQYCMTAWGjc5OjAuMjIyNzk5MzU5OjAuOTAzOTI4ODg1BgIxMxZodHc9MC42NTp0Z3c9MDp0YWd3PTA6cWlzPTM6cWlyPTAuNjpsZGFzPTA6Y3ZyPTA6cW0yc2ltPTA6bWF0Y2hfdHlwZT0yOnNlYXJjaF9sdmw9MjpycV9tYXRjaDo1OnFjX21hdGNoOjUGBDIwMTgWCzAuMDI1MjEzNDI1BgQyMDE5FgnmlpflnLDkuLsGCGFjY3VyYXRlFgExBgRwY3ZyFgowLjAyNzc2ODkxBgVxdWVyeRYJ5paX5Zyw5Li7BgpxdWVyeV9mbGFnFgExBgxyZXdyaXRlcXVlcnkWAAYDdGd3FgYwLjAwMDEGAnR3FgQwLjY1Bgl1c2VyX3R5cGUWATAGBnpwcmljZRYBMPUUAAAAAAAAAAD9/wAMfwAEEODrHQ=="
    }
  ]
}        
    """.trimIndent()
}