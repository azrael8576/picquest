package com.wei.picquest.feature.photo.photosearch

import com.wei.picquest.core.base.BaseViewModel
import com.wei.picquest.feature.photo.photosearch.manager.RecentSearchManager
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PhotoSearchViewModel @Inject constructor() : BaseViewModel<
    PhotoSearchViewAction,
    PhotoSearchViewState,
    >(PhotoSearchViewState()) {

    private val recentSearchManager = RecentSearchManager()
    private fun searchQueryChanged(query: String) {
        updateState {
            copy(
                searchQuery = query,
            )
        }
    }

    private fun searchTriggered(query: String) {
        recentSearchManager.addSearchQuery(query)
        updateState {
            copy(
                searchQuery = "",
                recentSearchQueries = recentSearchManager.recentSearchQueries,
            )
        }
    }

    private fun clearRecentSearchQueries() {
        recentSearchManager.clearSearches()
        updateState {
            copy(
                recentSearchQueries = recentSearchManager.recentSearchQueries,
            )
        }
        Timber.e("clearRecentSearchQueries " + recentSearchManager.recentSearchQueries.toString())
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
