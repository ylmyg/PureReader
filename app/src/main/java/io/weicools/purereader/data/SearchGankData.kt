package io.weicools.purereader.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author Weicools Create on 2018.08.12
 *
 * desc:
 */
data class SearchGankData(
    @SerializedName("count") @Expose val count: Int,
    @SerializedName("error") @Expose val error: Boolean,
    @SerializedName("results") @Expose val searchResults: List<SearchResult>?
)

data class SearchResult(
    @SerializedName("desc") @Expose val desc: String,
    @SerializedName("ganhuo_id") @Expose val ganId: String,
    @SerializedName("publishedAt") @Expose var publishedAt: String,
    @SerializedName("type") @Expose val type: String,
    @SerializedName("url") @Expose val url: String,
    @SerializedName("who") @Expose val who: String
)