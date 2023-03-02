package com.arsars.pixabayclient.ui.screens.photo.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arsars.pixabayclient.DestinationsArgs.PHOTO_ID_ARG
import com.arsars.pixabayclient.data.source.local.photos.Photo
import com.arsars.pixabayclient.domain.usecases.GetPhotoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoDetailsViewModel
@Inject
constructor(
    getPhotoUseCase: GetPhotoUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val photoId: Long = checkNotNull(savedStateHandle[PHOTO_ID_ARG])

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    init {
        viewModelScope.launch {
            getPhotoUseCase(photoId)
                .collect {
                    _uiState.emit(UiState(it))
                }
        }
    }

    data class UiState(
        val photo: Photo? = null
    )
}