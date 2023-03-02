package com.arsars.pixabayclient.data.source.local.queries

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "SearchQueries" )
data class SearchQuery(
    @PrimaryKey
    @ColumnInfo(name = "queryId")
    val queryId: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "query")
    val query: String
)