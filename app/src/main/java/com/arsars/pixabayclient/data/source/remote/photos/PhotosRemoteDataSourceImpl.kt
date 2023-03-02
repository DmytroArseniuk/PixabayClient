package com.arsars.pixabayclient.data.source.remote.photos

import com.arsars.pixabayclient.data.source.remote.PhotosRemoteDataSource
import com.arsars.pixabayclient.di.DispatcherIo
import com.arsars.pixabayclient.di.PerPageSetting
import com.arsars.pixabayclient.di.RexabayApiKey
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhotosRemoteDataSourceImpl
@Inject
constructor(
    @RexabayApiKey
    private val apiKey: String,
    @PerPageSetting
    private val perPageSetting: Int,
    private val pixabayApi: PixabayApi,
    @DispatcherIo
    private val dispatcher: CoroutineDispatcher
) : PhotosRemoteDataSource {

    override suspend fun search(query: String, page: Int): PhotoResponse {
        return withContext(dispatcher) {
            return@withContext pixabayApi.searchImages(
                apiKey = apiKey,
                query = query,
                page = page,
                perPage = perPageSetting
            )
        }
    }
}