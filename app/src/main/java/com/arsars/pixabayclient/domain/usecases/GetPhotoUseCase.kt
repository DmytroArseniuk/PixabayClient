package com.arsars.pixabayclient.domain.usecases

import com.arsars.pixabayclient.data.repo.PhotosRepository
import com.arsars.pixabayclient.data.source.local.photos.Photo
import com.arsars.pixabayclient.di.DispatcherIo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPhotoUseCase
@Inject
constructor(
    @DispatcherIo
    dispatcher: CoroutineDispatcher,
    private val photosRepository: PhotosRepository,
) : UseCase<Long, Flow<Photo>>(dispatcher) {

    override suspend fun doWork(photoId: Long): Flow<Photo> {
        return photosRepository.getPhoto(photoId)
    }

}