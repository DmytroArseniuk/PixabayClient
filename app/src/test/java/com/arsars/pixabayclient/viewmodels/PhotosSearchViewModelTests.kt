package com.arsars.pixabayclient.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingData
import com.arsars.pixabayclient.data.Stubs
import com.arsars.pixabayclient.domain.usecases.GetPhotosPagingDataUseCase
import com.arsars.pixabayclient.ui.screens.photo.details.PhotosSearchViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PhotosSearchViewModelTests {

    private val getPhotosPagingDataUseCase: GetPhotosPagingDataUseCase = mockk()
    private val savedStateHandle: SavedStateHandle = mockk()

    @Test
    fun viewModel_autoloadPhoto_success() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        val query = Stubs.defaultQuery

        every { savedStateHandle.get<String>(any()) } returns null

        val flowOfPages = flowOf(PagingData.from(Stubs.photos))
        coEvery { getPhotosPagingDataUseCase(query.query) } returns flowOfPages

        val photosSearchViewModel =
            PhotosSearchViewModel(getPhotosPagingDataUseCase, savedStateHandle)

        val state = photosSearchViewModel.uiState.drop(1).first()
        val photosPagingDataFlow = photosSearchViewModel.photos
        assertEquals(PhotosSearchViewModel.UiState(query.query), state)
        assertEquals(flowOfPages.first(), photosPagingDataFlow.first())
    }
}