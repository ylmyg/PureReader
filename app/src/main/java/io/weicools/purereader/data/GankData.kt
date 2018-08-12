package io.weicools.purereader.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author Weicools Create on 2018.08.12
 *
 * desc:
 */
data class GankData(
    @SerializedName("error") @Expose val error: Boolean,
    @SerializedName("results") @Expose val results: List<GankContent>
)

/*
https://gank.io/api/data/Android/1/1

{
  "error": false,
  "results": []
}
 */