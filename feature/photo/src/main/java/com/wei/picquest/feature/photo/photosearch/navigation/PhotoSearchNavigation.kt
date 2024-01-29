package com.wei.picquest.feature.photo.photosearch.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.wei.picquest.feature.photo.photosearch.PhotoSearchRoute

const val PHOTO_SEARCH_ROUTE = "photo_search_route"

fun NavController.navigateToPhotoSearch(navOptions: NavOptions? = null) {
    this.navigate(PHOTO_SEARCH_ROUTE, navOptions)
}

fun NavGraphBuilder.photoSearchGraph(
    navController: NavController,
    nestedGraphs: NavGraphBuilder.() -> Unit,
) {
    composable(route = PHOTO_SEARCH_ROUTE) {
        PhotoSearchRoute(
            navController = navController,
        )
    }
    nestedGraphs()
}
