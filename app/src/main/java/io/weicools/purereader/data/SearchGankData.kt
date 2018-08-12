package io.weicools.purereader.data

/**
 * @author Weicools Create on 2018.08.12
 *
 * desc:
 */
data class SearchGankData(
    val count: Int,
    val error: Boolean,
    val results: List<Result>
)

data class Result(
    val desc: String,
    val ganhuo_id: String,
    val publishedAt: String,
    val readability: String,
    val type: String,
    val url: String,
    val who: String
)