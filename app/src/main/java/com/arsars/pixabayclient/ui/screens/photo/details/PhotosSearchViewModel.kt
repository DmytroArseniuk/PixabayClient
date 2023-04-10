package com.arsars.pixabayclient.ui.screens.photo.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.arsars.pixabayclient.domain.usecases.GetPhotosPagingDataUseCase
import com.arsars.pixabayclient.ui.screens.photo.PhotoUI
import com.arsars.pixabayclient.ui.screens.photo.PhotoUIMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class PhotosSearchViewModel
@Inject
constructor(
    private val getPhotosPagingDataUseCase: GetPhotosPagingDataUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val mapper = PhotoUIMapper()
    private val _uiState = MutableStateFlow(UiState(""))
    val photos: Flow<PagingData<PhotoUI>>
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
    val accept: (UiAction) -> Unit

    init {
        val initialQuery: String = savedStateHandle[SAVED_QUERY] ?: DEFAULT_QUERY

        val actionFlow = MutableSharedFlow<UiAction>()
        val queryFlow = actionFlow.filterIsInstance<UiAction.Input>()
            .distinctUntilChanged()
            .stateIn(viewModelScope, SharingStarted.Lazily, UiAction.Input(initialQuery))
        val searchFlow = actionFlow.filterIsInstance<UiAction.Search>()
            .shareIn(viewModelScope, SharingStarted.Eagerly, 1)

        accept = { action ->
            viewModelScope.launch { actionFlow.emit(action) }
        }

        viewModelScope.launch {
            queryFlow.collect {
                _uiState.emit(_uiState.value.copy(query = it.query))
            }
        }

        photos = searchFlow.flatMapLatest {
            getPhotosPagingDataUseCase(uiState.value.query).map { pagingData ->
                pagingData.map(mapper::map)
            }
        }.cachedIn(viewModelScope)

        viewModelScope.launch {
            accept(UiAction.Search)
        }

    }

    companion object {
        private const val SAVED_QUERY = "SAVED_QUERY"
        const val DEFAULT_QUERY = "fruits"
    }

    data class UiState(
        val query: String = ""
    )

    sealed class UiAction {
        data class Input(val query: String) : UiAction()
        object Search : UiAction()
    }

}