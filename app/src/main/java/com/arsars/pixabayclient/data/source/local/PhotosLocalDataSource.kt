package com.arsars.pixabayclient.data.source.local

import androidx.paging.PagingSource
import com.arsars.pixabayclient.data.source.local.photos.Photo
import com.arsars.pixabayclient.data.source.local.queries.QueryPhotoCrossRef
import kotlinx.coroutines.flow.Flow

interface PhotosLocalDataSource {
    fun getPhotosByQueryId(queryId: String): PagingSource<Int, Photo>

    fun getPhoto(photoId: Long): Flow<Photo>

    suspend fun insertCrossRef(refs: List<QueryPhotoCrossRef>)

    suspend fun insert(photos: List<Photo>) : LongArray

    suspend fun clearPhotosForQuery(queryId: String)
}