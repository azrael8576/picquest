package com.wei.picquest.feature.video.videosearch.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.wei.picquest.feature.video.videosearch.VideoSearchRoute

const val VIDEO_SEARCH_ROUTE = "video_search_route"

fun NavController.navigateToVideoSearch(navOptions: NavOptions? = null) {
    this.navigate(VIDEO_SEARCH_ROUTE, navOptions)
}

fun NavGraphBuilder.videoSearchGraph(
    navController: NavController,
    nestedGraphs: NavGraphBuilder.() -> Unit,
) {
    composable(route = VIDEO_SEARCH_ROUTE) {
        VideoSearchRoute(
            navController = navController,
        )
    }
    nestedGraphs()
}
