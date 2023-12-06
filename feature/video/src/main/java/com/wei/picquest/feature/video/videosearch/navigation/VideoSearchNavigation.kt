package com.wei.picquest.feature.video.videosearch.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.wei.picquest.feature.video.videosearch.VideoSearchRoute

const val videoSearchRoute = "video_search_route"

fun NavController.navigateToVideoSearch(navOptions: NavOptions? = null) {
    this.navigate(videoSearchRoute, navOptions)
}

fun NavGraphBuilder.videoSearchGraph(
    navController: NavController,
    nestedGraphs: NavGraphBuilder.() -> Unit,
) {
    composable(route = videoSearchRoute) {
        VideoSearchRoute(
            navController = navController,
        )
    }
    nestedGraphs()
}
