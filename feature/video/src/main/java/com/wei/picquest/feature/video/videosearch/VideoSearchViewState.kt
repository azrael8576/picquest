package com.wei.picquest.feature.video.videosearch

import com.wei.picquest.core.base.Action
import com.wei.picquest.core.base.State

sealed class VideoSearchViewAction : Action {
    data class SearchQueryChanged(
        val query: String,
    ) : VideoSearchViewAction()

    data class SearchTriggered(
        val query: String,
    ) : VideoSearchViewAction()

    data class RecentSearchClicked(
        val query: String,
    ) : VideoSearchViewAction()

    data object ClearRecentSearchQueriesClicked : VideoSearchViewAction()
}

data class VideoSearchViewState(
    val searchQuery: String = "",
    val recentSearchQueries: List<String> = emptyList(),
) : State
