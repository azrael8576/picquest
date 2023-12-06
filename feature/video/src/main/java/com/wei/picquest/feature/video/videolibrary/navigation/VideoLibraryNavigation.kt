package com.wei.picquest.feature.video.videolibrary.navigation

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.wei.picquest.feature.video.videolibrary.VideoLibraryRoute
import com.wei.picquest.feature.video.videosearch.navigation.videoSearchRoute
import java.net.URLDecoder
import java.net.URLEncoder

private val URL_CHARACTER_ENCODING = Charsets.UTF_8.name()

@VisibleForTesting
internal const val videoSearchQueryArg = "videoSearchQuery"

internal class VideoLibraryArgs(val videoSearchQuery: String) {
    constructor(savedStateHandle: SavedStateHandle) :
        this(
            URLDecoder.decode(
                checkNotNull(savedStateHandle[videoSearchQueryArg]),
                URL_CHARACTER_ENCODING,
            ),
        )
}

fun NavController.navigateToVideoLibrary(videoSearchQuery: String) {
    val encodedKey = URLEncoder.encode(
        videoSearchQuery.ifBlank { "$videoSearchQuery " },
        URL_CHARACTER_ENCODING,
    )
    this.navigate("$videoSearchRoute/$encodedKey") {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.videoLibraryGraph(
    navController: NavController,
) {
    composable(
        route = "$videoSearchRoute/{$videoSearchQueryArg}",
        arguments = listOf(
            navArgument(videoSearchQueryArg) { type = NavType.StringType },
        ),
    ) {
        VideoLibraryRoute(
            navController = navController,
        )
    }
}
