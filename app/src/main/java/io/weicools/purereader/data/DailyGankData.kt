package io.weicools.purereader.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author Weicools Create on 2018.08.12
 *
 * desc:
 */
data class DailyGankData(
    @SerializedName("category") @Expose val category: List<String>,
    @SerializedName("error") @Expose val error: Boolean,
    @SerializedName("results") @Expose val results: Results
)

data class Results(
    @SerializedName("Android") @Expose val Android: List<GankContent>,
    @SerializedName("App") @Expose val App: List<GankContent>,
    @SerializedName("iOS") @Expose val iOS: List<GankContent>,
    @SerializedName("休息视频") @Expose val video: List<GankContent>,
    @SerializedName("前端") @Expose val webFont: List<GankContent>,
    @SerializedName("拓展资源") @Expose val resource: List<GankContent>,
    @SerializedName("瞎推荐") @Expose val recommend: List<GankContent>,
    @SerializedName("福利") @Expose val welfare: List<GankContent>
)

/*
https://gank.io/api/day/2018/08/09

{
  "category": [
    "前端",
    "iOS",
    "瞎推荐",
    "App",
    "拓展资源",
    "Android",
    "福利",
    "休息视频"
  ],
  "error": false,
  "results": {
    "Android": [],
    "App": [],
    "iOS": [],
    "休息视频": [],
    "前端": [],
    "拓展资源": [],
    "瞎推荐": [],
    "福利": []
  }
}
 */