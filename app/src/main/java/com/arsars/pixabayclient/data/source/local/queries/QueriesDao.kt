package com.arsars.pixabayclient.data.source.local.queries

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
abstract class QueriesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertQuery(query: SearchQuery)

    @Insert()
    abstract suspend fun insertQueryPhotoCrossRef(crossRef: QueryPhotoCrossRef)

    @Query("SELECT * FROM SearchQueries WHERE query = :query")
    abstract suspend fun findQuery(query: String): SearchQuery?

    @Query("DELETE FROM QueryPhotoCrossRef")
    abstract suspend fun clearCrossRef()

    @Query("DELETE FROM QueryPhotoCrossRef WHERE queryId = :queryId")
    abstract suspend fun clearCrossRefForQuery(queryId: String)

    @Query("DELETE FROM SearchQueries")
    abstract suspend fun clearQueries()
}