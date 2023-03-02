package com.arsars.pixabayclient.data.source.local.photos

import androidx.paging.PagingSource
import com.arsars.pixabayclient.data.source.local.PhotosLocalDataSource
import com.arsars.pixabayclient.data.source.local.queries.QueryPhotoCrossRef
import com.arsars.pixabayclient.data.source.local.queries.SearchQuery
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhotosLocalDataSourceImpl
@Inject
constructor(
    private val photosDao: PhotosDao,
) : PhotosLocalDataSource {

    override fun getPhotosByQueryId(queryId: String): PagingSource<Int, Photo> {
        return photosDao.getQueryWithPhotosByQueryId(queryId)
    }

    override fun getPhoto(photoId: Long): Flow<Photo> {
        return photosDao.getPhoto(photoId)
    }

    override suspend fun insertCrossRef(refs: List<QueryPhotoCrossRef>) {
        photosDao.insertCrossRef(refs)
    }

    override suspend fun insert(photos: List<Photo>) : LongArray {
        return photosDao.insert(photos)
    }

    override suspend fun clearPhotosForQuery(queryId: String) {
        photosDao.clearPhotosForQuery(queryId)
    }

}