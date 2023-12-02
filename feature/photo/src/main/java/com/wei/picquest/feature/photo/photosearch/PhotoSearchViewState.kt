package com.wei.picquest.feature.photo.photosearch

import com.wei.picquest.core.base.Action
import com.wei.picquest.core.base.State

sealed class PhotoSearchViewAction : Action {
    data class SearchQueryChanged(
        val query: String,
    ) : PhotoSearchViewAction()

    data class SearchTriggered(
        val query: String,
    ) : PhotoSearchViewAction()

    data class RecentSearchClicked(
        val query: String,
    ) : PhotoSearchViewAction()

    data object ClearRecentSearchQueriesClicked : PhotoSearchViewAction()
}

data class PhotoSearchViewState(
    val searchQuery: String = "",
    val recentSearchQueries: List<String> = emptyList(),
) : State
