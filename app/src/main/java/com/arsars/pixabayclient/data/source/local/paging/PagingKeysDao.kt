package com.arsars.pixabayclient.data.source.local.paging

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PagingKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pagingKeys: List<PagingKey>)

    @Query("SELECT * FROM pagingKeys WHERE photoId = :photoId AND queryId = :queryId")
    suspend fun getCurrentPage(photoId: Long, queryId: String): PagingKey?

    @Query("DELETE FROM pagingKeys")
    suspend fun clear()

    @Query("DELETE FROM pagingKeys WHERE queryId = :queryId")
    suspend fun clearPagingKeysForQuery(queryId: String)
}