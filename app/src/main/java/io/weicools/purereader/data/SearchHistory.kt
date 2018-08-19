package io.weicools.purereader.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull

/**
 * @author Weicools Create on 2018.08.17
 *
 * desc:
 */
@Entity(tableName = "search_history")
data class SearchHistory(
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "keyword") val keyword: String,
    @ColumnInfo(name = "search_time") val searchTime: Long
)