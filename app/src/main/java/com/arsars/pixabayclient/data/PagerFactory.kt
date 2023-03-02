package com.arsars.pixabayclient.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.arsars.pixabayclient.data.source.local.PagingLocalDataSource
import com.arsars.pixabayclient.data.source.local.PhotosLocalDataSource
import com.arsars.pixabayclient.data.source.local.PixabayDB
import com.arsars.pixabayclient.data.source.local.QueriesLocalDataSource
import com.arsars.pixabayclient.data.source.local.photos.Photo
import com.arsars.pixabayclient.data.source.local.queries.SearchQuery
import com.arsars.pixabayclient.data.source.remote.PhotosRemoteDataSource
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class PagerFactory
@Inject
constructor(
    private val photosRemoteDataSource: PhotosRemoteDataSource,
    private val photosLocalDataSource: PhotosLocalDataSource,
    private val queriesLocalDataSource: QueriesLocalDataSource,
    private val pagingLocalDataSource: PagingLocalDataSource,
    private val database: PixabayDB,
    private val mapper: PhotosMapper
) {

    fun getPager(
        pageSize: Int,
        searchQuery: SearchQuery
    ): Pager<Int, Photo> {
        return Pager(
            config = PagingConfig(
                pageSize = pageSize,
                enablePlaceholders = true
            ),
            remoteMediator = PagingMediator(
                photosRemoteDataSource,
                photosLocalDataSource,
                queriesLocalDataSource,
                pagingLocalDataSource,
                database,
                mapper,
                searchQuery
            ),
            pagingSourceFactory = { photosLocalDataSource.getPhotosByQueryId(searchQuery.queryId) }
        )
    }


}