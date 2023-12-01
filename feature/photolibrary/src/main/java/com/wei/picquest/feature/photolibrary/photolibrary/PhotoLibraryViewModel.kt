package com.wei.picquest.feature.photolibrary.photolibrary

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.wei.picquest.core.base.BaseViewModel
import com.wei.picquest.core.data.repository.SearchImagesRepository
import com.wei.picquest.core.network.model.NetworkImageDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoLibraryViewModel @Inject constructor(
    private val searchImagesRepository: SearchImagesRepository,
) : BaseViewModel<
    PhotoLibraryViewAction,
    PhotoLibraryViewState,
    >(PhotoLibraryViewState()) {
    private val _imagesState: MutableStateFlow<PagingData<NetworkImageDetail>> =
        MutableStateFlow(value = PagingData.empty())
    val imagesState: MutableStateFlow<PagingData<NetworkImageDetail>> get() = _imagesState

    init {
        searchImages("flow")
    }

    private fun searchImages(query: String) {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }

            searchImagesRepository.getSearchImages(query)
                .distinctUntilChanged()
                .cachedIn(viewModelScope)
                .collect { pagingData ->
                    _imagesState.value = pagingData
                }
        }
    }

    override fun dispatch(action: PhotoLibraryViewAction) {
        when (action) {
            is PhotoLibraryViewAction.SearchImages -> searchImages(action.query)
        }
    }
}
