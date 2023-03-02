package com.arsars.pixabayclient.data.source.local.paging

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "pagingKeys", primaryKeys = ["photoId"])
data class PagingKey(
    @ColumnInfo(name = "photoId")
    val photoId: Long,
    @ColumnInfo(name = "queryId")
    val queryId: String,
    @ColumnInfo(name = "nextKey")
    val nextKey: Int?,
    @ColumnInfo(name = "previousKey")
    val previousKey: Int?
)