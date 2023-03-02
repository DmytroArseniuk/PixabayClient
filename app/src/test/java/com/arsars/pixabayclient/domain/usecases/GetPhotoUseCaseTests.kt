package com.arsars.pixabayclient.domain.usecases

import com.arsars.pixabayclient.data.Stubs
import com.arsars.pixabayclient.data.repo.PhotosRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetPhotoUseCaseTests {

    private val photosRepository: PhotosRepository = mockk()

    val getPhotoUseCase = GetPhotoUseCase(
        Dispatchers.Main,
        photosRepository
    )

    @Test
    fun invoke_photoById_reposCalled() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))

        val photo = Stubs.photos.first()
        coEvery {
            photosRepository.getPhoto(any())
        } returns flowOf(photo)

        val photoFlow = getPhotoUseCase(photo.id)

        coVerify(exactly = 1) {
            photosRepository.getPhoto(photo.id)
        }

        assertEquals(photo, photoFlow.first())
    }

}