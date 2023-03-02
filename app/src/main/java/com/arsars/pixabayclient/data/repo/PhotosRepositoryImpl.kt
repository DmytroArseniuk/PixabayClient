package com.arsars.pixabayclient.data.repo

import androidx.paging.PagingData
import com.arsars.pixabayclient.data.PagerFactory
import com.arsars.pixabayclient.data.source.local.PhotosLocalDataSource
import com.arsars.pixabayclient.data.source.local.photos.Photo
import com.arsars.pixabayclient.data.source.local.queries.SearchQuery
import com.arsars.pixabayclient.di.PerPageSetting
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhotosRepositoryImpl
@Inject
constructor(
    @PerPageSetting
    private val perPage: Int,
    private val pagerFactory: PagerFactory,
    private val photosLocalDataSource: PhotosLocalDataSource,
) : PhotosRepository {

    override fun getPhotosPaged(searchQuery: SearchQuery): Flow<PagingData<Photo>> {
        return pagerFactory.getPager(perPage, searchQuery).flow
    }

    override fun getPhoto(photoId: Long): Flow<Photo> {
        return photosLocalDataSource.getPhoto(photoId)
    }
}
