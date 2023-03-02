package com.arsars.pixabayclient.data.source.local.queries

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "QueryPhotoCrossRef", primaryKeys = ["queryId", "photoId"])
data class QueryPhotoCrossRef(
    @ColumnInfo(name = "queryId")
    val queryId: String,
    @ColumnInfo(name = "photoId")
    val photoId: Long
)