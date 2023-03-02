package com.arsars.pixabayclient.domain.usecases

import com.arsars.pixabayclient.data.Stubs
import com.arsars.pixabayclient.data.repo.PhotosRepository
import com.arsars.pixabayclient.data.repo.QueriesRepository
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetPhotosPagingDataUseCaseTests {

    private val photosRepository: PhotosRepository = mockk()
    private val queriesRepository: QueriesRepository = mockk()

    val getPhotosPagingDataUseCase = GetPhotosPagingDataUseCase(
        Dispatchers.Main,
        photosRepository,
        queriesRepository
    )

    @Test
    fun invoke_PhotosByQuery_queryExists() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        val searchQuery1 = Stubs.searchQuery1

        coEvery {
            queriesRepository.findQuery(searchQuery1.query)
        } returns searchQuery1

        coEvery {
            photosRepository.getPhotosPaged(any())
        } returns mockk()

        getPhotosPagingDataUseCase(searchQuery1.query)

        coVerify(exactly = 1) {
            queriesRepository.findQuery(searchQuery1.query)
            photosRepository.getPhotosPaged(searchQuery1)
        }
        coVerify(exactly = 0) {
            queriesRepository.insertSearchQuery(searchQuery1)
        }
    }

    @Test
    fun invoke_PhotosByQuery_queryDoesNotExist() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        val searchQuery1 = Stubs.searchQuery1

        coEvery {
            queriesRepository.findQuery(any())
        } returns null

        coEvery {
            queriesRepository.insertSearchQuery(any())
        } just Runs

        coEvery {
            photosRepository.getPhotosPaged(any())
        } returns mockk()

        getPhotosPagingDataUseCase(searchQuery1.query)

        coVerify(exactly = 1) {
            queriesRepository.findQuery(searchQuery1.query)
            queriesRepository.insertSearchQuery(any())
            photosRepository.getPhotosPaged(any())
        }
    }

}