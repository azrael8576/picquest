package com.wei.picquest.feature.photo.photosearch.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.wei.picquest.feature.photo.photosearch.PhotoSearchRoute

const val photoSearchRoute = "photo_search_route"

fun NavController.navigateToPhotoSearch(navOptions: NavOptions? = null) {
    this.navigate(photoSearchRoute, navOptions)
}

fun NavGraphBuilder.photoSearchGraph(
    navController: NavController,
    onSearchClick: (String) -> Unit,
    nestedGraphs: NavGraphBuilder.() -> Unit,
) {
    composable(route = photoSearchRoute) {
        PhotoSearchRoute(
            onSearchClick = onSearchClick,
            navController = navController,
        )
    }
    nestedGraphs()
}
