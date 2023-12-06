package com.wei.picquest.feature.video.videosearch

import androidx.lifecycle.viewModelScope
import com.wei.picquest.core.base.BaseViewModel
import com.wei.picquest.core.data.repository.UserDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoSearchViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
) : BaseViewModel<
    VideoSearchViewAction,
    VideoSearchViewState,
    >(VideoSearchViewState()) {

    init {
        viewModelScope.launch {
            userDataRepository.userData.collect { userData ->
                updateState {
                    copy(
                        recentSearchQueries = userData.recentSearchVideoQueries.asReversed(),
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
                userDataRepository.addRecentSearchVideoQuery(query)
            }
        }
        updateState { copy(searchQuery = "") }
    }

    private fun clearRecentSearchQueries() {
        viewModelScope.launch {
            userDataRepository.clearRecentSearchVideoQueries()
        }
    }

    override fun dispatch(action: VideoSearchViewAction) {
        when (action) {
            is VideoSearchViewAction.SearchQueryChanged -> searchQueryChanged(action.query)
            is VideoSearchViewAction.SearchTriggered -> searchTriggered(action.query)
            is VideoSearchViewAction.RecentSearchClicked -> searchTriggered(action.query)
            is VideoSearchViewAction.ClearRecentSearchQueriesClicked -> clearRecentSearchQueries()
        }
    }
}
