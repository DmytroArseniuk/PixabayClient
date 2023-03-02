package com.arsars.pixabayclient.domain.usecases

import androidx.paging.PagingData
import com.arsars.pixabayclient.data.repo.PhotosRepository
import com.arsars.pixabayclient.data.repo.QueriesRepository
import com.arsars.pixabayclient.data.source.local.photos.Photo
import com.arsars.pixabayclient.data.source.local.queries.SearchQuery
import com.arsars.pixabayclient.di.DispatcherIo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPhotosPagingDataUseCase
@Inject
constructor(
    @DispatcherIo
    dispatcher: CoroutineDispatcher,
    private val photosRepository: PhotosRepository,
    private val queriesRepository: QueriesRepository
) : UseCase<String, Flow<PagingData<Photo>>>(dispatcher) {

    override suspend fun doWork(query: String): Flow<PagingData<Photo>> {
        var searchQuery = queriesRepository.findQuery(query)
        if (searchQuery == null) {
            searchQuery = SearchQuery(query = query)
            queriesRepository.insertSearchQuery(searchQuery)
        }

        return photosRepository.getPhotosPaged(searchQuery)
    }

}