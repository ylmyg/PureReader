package io.weicools.purereader.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import android.support.annotation.NonNull
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.weicools.purereader.database.converter.StringTypeConverter
import java.io.Serializable

/**
 * @author Weicools Create on 2018.08.12
 *
 * desc:
 */
@Entity(tableName = "gank_content")
@TypeConverters(StringTypeConverter::class)
data class GankContent(
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "_id") @SerializedName("_id") @Expose val id: String,
    @ColumnInfo(name = "desc") @SerializedName("desc") @Expose val desc: String,
    @ColumnInfo(name = "images") @SerializedName("images") @Expose val images: List<String>?,
    @ColumnInfo(name = "publishedAt") @SerializedName("publishedAt") @Expose var publishedAt: String,
    @ColumnInfo(name = "type") @SerializedName("type") @Expose val type: String,
    @ColumnInfo(name = "url") @SerializedName("url") @Expose val url: String,
    @ColumnInfo(name = "who") @SerializedName("who") @Expose val who: String
) : Serializable

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