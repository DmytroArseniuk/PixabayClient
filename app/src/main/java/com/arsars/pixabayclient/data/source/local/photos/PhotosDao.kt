package com.arsars.pixabayclient.data.source.local.photos

import androidx.paging.PagingSource
import androidx.room.*
import com.arsars.pixabayclient.data.source.local.queries.QueryPhotoCrossRef
import kotlinx.coroutines.flow.Flow


@Dao
abstract class PhotosDao {

    @Query("SELECT * FROM photos WHERE id IN (SELECT photoId FROM QueryPhotoCrossRef WHERE queryId = :queryId)")
    abstract fun getQueryWithPhotosByQueryId(queryId: String): PagingSource<Int, Photo>

    @Query("SELECT * FROM photos WHERE id = :photoId")
    abstract fun getPhoto(photoId: Long): Flow<Photo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(deliveries: List<Photo>) : LongArray

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertCrossRef(crossRef: List<QueryPhotoCrossRef>)

    @Query("DELETE FROM photos WHERE id IN (SELECT photoId FROM QueryPhotoCrossRef WHERE queryId = :queryId)")
    abstract suspend fun clearPhotosForQuery(queryId: String)

    @Query("DELETE FROM photos WHERE id NOT IN (SELECT photoId FROM QueryPhotoCrossRef)")
    abstract suspend fun clearPhotosNotConnectedToQueries()
}