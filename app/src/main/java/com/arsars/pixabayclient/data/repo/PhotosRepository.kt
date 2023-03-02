package com.arsars.pixabayclient.data.repo

import androidx.paging.PagingData
import com.arsars.pixabayclient.data.source.local.photos.Photo
import com.arsars.pixabayclient.data.source.local.queries.SearchQuery
import kotlinx.coroutines.flow.Flow


interface PhotosRepository {
    fun getPhotosPaged(searchQuery: SearchQuery): Flow<PagingData<Photo>>
    fun getPhoto(photoId: Long): Flow<Photo>
}
