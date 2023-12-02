package com.wei.picquest.feature.photolibrary.photolibrary.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.wei.picquest.feature.photolibrary.photolibrary.PhotoLibraryRoute

const val photoLibraryRoute = "photo_library_route"

fun NavController.navigateToPhotoLibrary(navOptions: NavOptions? = null) {
    this.navigate(photoLibraryRoute, navOptions)
}

fun NavGraphBuilder.photoLibraryGraph(
    navController: NavController,
) {
    composable(route = photoLibraryRoute) {
        PhotoLibraryRoute(
            navController = navController,
        )
    }
}
