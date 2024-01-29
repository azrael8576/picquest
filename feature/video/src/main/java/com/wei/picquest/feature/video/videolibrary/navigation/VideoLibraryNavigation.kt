package com.wei.picquest.feature.video.videolibrary.navigation

import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.wei.picquest.feature.video.videolibrary.VideoLibraryRoute
import com.wei.picquest.feature.video.videosearch.navigation.VIDEO_SEARCH_ROUTE
import java.net.URLDecoder
import java.net.URLEncoder

private val URL_CHARACTER_ENCODING = Charsets.UTF_8.name()

@VisibleForTesting
internal const val VIDEO_SEARCH_QUERY_ARG = "videoSearchQuery"

internal class VideoLibraryArgs(val videoSearchQuery: String) {
    constructor(savedStateHandle: SavedStateHandle) :
        this(
            URLDecoder.decode(
                checkNotNull(savedStateHandle[VIDEO_SEARCH_QUERY_ARG]),
                URL_CHARACTER_ENCODING,
            ),
        )
}

fun NavController.navigateToVideoLibrary(videoSearchQuery: String) {
    val encodedKey =
        URLEncoder.encode(
            videoSearchQuery.ifBlank { "$videoSearchQuery " },
            URL_CHARACTER_ENCODING,
        )
    this.navigate("$VIDEO_SEARCH_ROUTE/$encodedKey") {
        launchSingleTop = true
    }
}

@OptIn(ExperimentalFoundationApi::class)
fun NavGraphBuilder.videoLibraryGraph(navController: NavController) {
    composable(
        route = "$VIDEO_SEARCH_ROUTE/{$VIDEO_SEARCH_QUERY_ARG}",
        arguments =
        listOf(
            navArgument(VIDEO_SEARCH_QUERY_ARG) { type = NavType.StringType },
        ),
    ) {
        VideoLibraryRoute(
            navController = navController,
        )
    }
}
