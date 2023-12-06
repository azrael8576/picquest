package com.wei.picquest.feature.photo.photosearch

import androidx.lifecycle.viewModelScope
import com.wei.picquest.core.base.BaseViewModel
import com.wei.picquest.core.data.repository.UserDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoSearchViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
) : BaseViewModel<
    PhotoSearchViewAction,
    PhotoSearchViewState,
    >(PhotoSearchViewState()) {

    init {
        viewModelScope.launch {
            userDataRepository.userData.collect { userData ->
                updateState {
                    copy(
                        recentSearchQueries = userData.recentSearchPhotoQueries.asReversed(),
                    )
                }
            }
        }
    }

    private fun searchQueryChanged(query: String) {
        updateState { copy(searchQuery = query) }
    }

    private fun searchTriggered(query: String) {
        if (query.isNotBlank()) {
            viewModelScope.launch {
                userDataRepository.addRecentSearchPhotoQuery(query)
            }
        }
        updateState { copy(searchQuery = "") }
    }

    private fun clearRecentSearchQueries() {
        viewModelScope.launch {
            userDataRepository.clearRecentSearchPhotoQueries()
        }
    }

    override fun dispatch(action: PhotoSearchViewAction) {
        when (action) {
            is PhotoSearchViewAction.SearchQueryChanged -> searchQueryChanged(action.query)
            is PhotoSearchViewAction.SearchTriggered -> searchTriggered(action.query)
            is PhotoSearchViewAction.RecentSearchClicked -> searchTriggered(action.query)
            is PhotoSearchViewAction.ClearRecentSearchQueriesClicked -> clearRecentSearchQueries()
        }
    }
}
