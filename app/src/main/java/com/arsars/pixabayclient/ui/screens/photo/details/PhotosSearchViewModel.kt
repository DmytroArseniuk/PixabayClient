package com.arsars.pixabayclient.ui.screens.photo.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.arsars.pixabayclient.data.source.local.photos.Photo
import com.arsars.pixabayclient.domain.usecases.GetPhotosPagingDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotosSearchViewModel
@Inject
constructor(
    private val getPhotosPagingDataUseCase: GetPhotosPagingDataUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val uiState: StateFlow<UiState>
    val photos: Flow<PagingData<Photo>>
    val accept: (UiAction) -> Unit

    init {
        val initialQuery: String = savedStateHandle[SAVED_QUERY] ?: DEFAULT_QUERY
        val actionStateFlow = MutableSharedFlow<UiAction>()
        val searchFlow = actionStateFlow.filterIsInstance<UiAction.Search>()
            .onStart { emit(UiAction.Search) }

        val queryFlow = actionStateFlow.filterIsInstance<UiAction.Input>()
            .distinctUntilChanged()
            .onStart { emit(UiAction.Input(initialQuery)) }

        uiState = queryFlow
            .map {
                UiState(
                    query = it.query
                )
            }
            .stateIn(
                scope = viewModelScope,
                initialValue = UiState(),
                started = SharingStarted.WhileSubscribed()
            )


        photos = searchFlow
            .transform {
                emit(uiState.value.query)
            }
            .flatMapLatest { getPhotosPagingDataUseCase(it) }

        accept = { action ->
            viewModelScope.launch { actionStateFlow.emit(action) }
        }
    }

    companion object {
        private const val SAVED_QUERY = "SAVED_QUERY"
        const val DEFAULT_QUERY = "fruits"
    }

    data class UiState(
        val query: String = "",
    )

    sealed class UiAction {
        data class Input(val query: String) : UiAction()
        object Search : UiAction()
    }

}