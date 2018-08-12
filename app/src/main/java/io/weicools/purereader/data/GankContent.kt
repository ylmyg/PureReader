package io.weicools.purereader.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author Weicools Create on 2018.08.12
 *
 * desc:
 */
data class GankContent(
    @SerializedName("_id") @Expose val _id: String,
    @SerializedName("createdAt") @Expose val createdAt: String,
    @SerializedName("desc") @Expose val desc: String,
    @SerializedName("images") @Expose val images: List<String>,
    @SerializedName("publishedAt") @Expose val publishedAt: String,
    @SerializedName("source") @Expose val source: String,
    @SerializedName("type") @Expose val type: String,
    @SerializedName("url") @Expose val url: String,
    @SerializedName("used") @Expose val used: Boolean,
    @SerializedName("who") @Expose val who: String
)

/*
{
  "_id": "5861d11f421aa97240ef9f42",
  "createdAt": "2016-12-27T10:25:35.867Z",
  "desc": "Material Design 风格的 About 页面设计。",
  "images": [
    "http://img.gank.io/ec2f4772-6b7e-4c35-ad87-323db69694c4"
  ],
  "publishedAt": "2016-12-27T12:06:15.638Z",
  "source": "chrome",
  "type": "Android",
  "url": "https://github.com/daniel-stoneuk/material-about-library",
  "used": true,
  "who": "代码家"
}
 */