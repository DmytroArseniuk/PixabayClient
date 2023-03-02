package com.arsars.pixabayclient.data

import android.util.Log
import androidx.paging.*
import androidx.room.withTransaction
import com.arsars.pixabayclient.data.source.local.PagingLocalDataSource
import com.arsars.pixabayclient.data.source.local.PhotosLocalDataSource
import com.arsars.pixabayclient.data.source.local.PixabayDB
import com.arsars.pixabayclient.data.source.local.QueriesLocalDataSource
import com.arsars.pixabayclient.data.source.local.paging.PagingKey
import com.arsars.pixabayclient.data.source.local.photos.Photo
import com.arsars.pixabayclient.data.source.local.queries.QueryPhotoCrossRef
import com.arsars.pixabayclient.data.source.local.queries.SearchQuery
import com.arsars.pixabayclient.data.source.remote.PhotosRemoteDataSource
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class PagingMediator(
    private val photosRemoteDataSource: PhotosRemoteDataSource,
    private val photosLocalDataSource: PhotosLocalDataSource,
    private val queriesLocalDataSource: QueriesLocalDataSource,
    private val pagingLocalDataSource: PagingLocalDataSource,
    private val database: PixabayDB,
    private val photosMapper: PhotosMapper,
    private val query: SearchQuery
) : RemoteMediator<Int, Photo>() {

    private val TAG = PagingMediator::class.java.simpleName

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Photo>
    ): MediatorResult {

        Log.d(TAG, "load() called with: loadType = $loadType, state = $state")
        val page = when (loadType) {
            LoadType.REFRESH -> getClosest(query, state)?.nextKey?.minus(1) ?: 1
            LoadType.PREPEND -> {
                val remoteKey = getFirst(query, state)
                remoteKey?.previousKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKey != null)
            }
            LoadType.APPEND -> {
                val remoteKey = getLast(query, state)
                remoteKey?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKey != null)
            }
        }

        Log.d(TAG, "for page $page")

        try {
            val photos: List<Photo> = photosRemoteDataSource.search(query.query, page).hits.map {
                photosMapper.map(it)
            }
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    Log.d(TAG, "clear all data for queryId ${query.queryId}")
                    query.queryId.let {
                        photosLocalDataSource.clearPhotosForQuery(it)
                        queriesLocalDataSource.clearCachedRefForQuery(it)
                        pagingLocalDataSource.clearPagingForQuery(it)
                    }
                }

                val previousPage = if (page > 1) page - 1 else null
                val nextPage = if (photos.isNotEmpty()) page + 1 else null

                Log.d(TAG, "calculated pages prev: $previousPage, next: $nextPage")

                val insertedPhotoIds = photosLocalDataSource.insert(photos)

                val pagingKeys = insertedPhotoIds.map {
                    PagingKey(
                        photoId = it,
                        queryId = query.queryId,
                        nextKey = nextPage,
                        previousKey = previousPage
                    )
                }
                pagingLocalDataSource.insert(pagingKeys)
                photosLocalDataSource.insertCrossRef(insertedPhotoIds.map {
                    QueryPhotoCrossRef(
                        queryId = query.queryId,
                        photoId = it
                    )
                })

            }
            return MediatorResult.Success(endOfPaginationReached = photos.isEmpty())
        } catch (exception: Exception) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getClosest(query: SearchQuery, state: PagingState<Int, Photo>): PagingKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let {
                pagingLocalDataSource.getCurrentPage(photoId = it, queryId = query.queryId)
            }
        }
    }

    private suspend fun getFirst(query: SearchQuery, state: PagingState<Int, Photo>): PagingKey? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let {
                pagingLocalDataSource.getCurrentPage(photoId = it.id, queryId = query.queryId)
            }
    }

    private suspend fun getLast(query: SearchQuery, state: PagingState<Int, Photo>): PagingKey? {
        return state.lastItemOrNull()?.let {
            pagingLocalDataSource.getCurrentPage(photoId = it.id, queryId = query.queryId)
        }
    }

}