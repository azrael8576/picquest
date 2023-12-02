package com.wei.picquest.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.window.layout.DisplayFeature
import com.wei.picquest.core.designsystem.ui.DeviceOrientation
import com.wei.picquest.feature.home.home.navigation.homeGraph
import com.wei.picquest.feature.home.home.navigation.homeRoute
import com.wei.picquest.feature.photo.photolibrary.navigation.navigateToPhotoLibrary
import com.wei.picquest.feature.photo.photolibrary.navigation.photoLibraryGraph
import com.wei.picquest.feature.photo.photosearch.navigation.photoSearchGraph
import com.wei.picquest.ui.PqAppState

/**
 * Top-level navigation graph. Navigation is organized as explained at
 * https://d.android.com/jetpack/compose/nav-adaptive
 *
 * The navigation graph defined in this file defines the different top level routes. Navigation
 * within each route is handled using state and Back Handlers.
 */
@Composable
fun PqNavHost(
    modifier: Modifier = Modifier,
    appState: PqAppState,
    displayFeatures: List<DisplayFeature>,
    startDestination: String = homeRoute,
) {
    val navController = appState.navController
    val navigationType = appState.navigationType
    val isPortrait = appState.currentDeviceOrientation == DeviceOrientation.PORTRAIT
    val contentType = appState.contentType

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        homeGraph(
            navController = navController,
        )
        photoSearchGraph(
            navController = navController,
            onSearchClick = navController::navigateToPhotoLibrary,
            nestedGraphs = {
                photoLibraryGraph(navController = navController)
            },
        )
    }
}
