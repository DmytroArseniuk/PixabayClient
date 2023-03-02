package com.arsars.pixabayclient.viewmodels

import androidx.lifecycle.SavedStateHandle
import com.arsars.pixabayclient.data.Stubs
import com.arsars.pixabayclient.domain.usecases.GetPhotoUseCase
import com.arsars.pixabayclient.ui.screens.photo.search.PhotoDetailsViewModel
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
class PhotoDetailsViewModelTests {

    private val getPhotoUseCase: GetPhotoUseCase = mockk()
    private val savedStateHandle: SavedStateHandle = mockk()

    @Test
    fun viewModel_autoloadPhoto_success() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))

        val photo = Stubs.photos.first()

        every { savedStateHandle.get<Long>(any()) } returns photo.id

        coEvery { getPhotoUseCase(photo.id) } returns flowOf(photo)

        val photoDetailsViewModel = PhotoDetailsViewModel(getPhotoUseCase, savedStateHandle)

        val state = photoDetailsViewModel.uiState.drop(1).first()

        assertEquals(PhotoDetailsViewModel.UiState(photo), state)

    }

}