package com.wei.picquest.feature.video.videolibrary.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.wei.picquest.feature.video.videolibrary.VideoLibraryRoute

const val videoLibraryRoute = "video_library_route"

fun NavController.navigateToVideoLibrary(navOptions: NavOptions? = null) {
    this.navigate(videoLibraryRoute, navOptions)
}

fun NavGraphBuilder.videoLibraryGraph(
    navController: NavController,
) {
    composable(route = videoLibraryRoute) {
        VideoLibraryRoute(
            navController = navController,
        )
    }
}
