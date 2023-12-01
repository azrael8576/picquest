package com.wei.picquest.feature.photolibrary.photolibrary

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.wei.picquest.core.base.BaseViewModel
import com.wei.picquest.core.data.model.ImageDetail
import com.wei.picquest.core.data.repository.SearchImagesRepository
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

    private val _imagesState: MutableStateFlow<PagingData<ImageDetail>> =
        MutableStateFlow(value = PagingData.empty())
    val imagesState: MutableStateFlow<PagingData<ImageDetail>> get() = _imagesState

    init {
        // TODO Wei
        searchImages("")
    }

    private fun searchImages(query: String) {
        viewModelScope.launch {
            searchImagesRepository.getSearchImages(query)
                .distinctUntilChanged()
                .cachedIn(viewModelScope)
                .collect { pagingData ->
                    _imagesState.value = pagingData
                }
        }
    }

    private fun switchLayoutType() {
        updateState {
            copy(
                layoutType = if (layoutType == LayoutType.LIST) LayoutType.GRID else LayoutType.LIST,
            )
        }
    }

    override fun dispatch(action: PhotoLibraryViewAction) {
        when (action) {
            is PhotoLibraryViewAction.SearchImages -> searchImages(action.query)
            is PhotoLibraryViewAction.SwitchLayoutType -> switchLayoutType()
        }
    }
}
