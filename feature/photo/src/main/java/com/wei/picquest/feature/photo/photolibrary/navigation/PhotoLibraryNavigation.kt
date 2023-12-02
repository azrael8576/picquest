package com.wei.picquest.feature.photo.photolibrary.navigation

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.wei.picquest.feature.photo.photolibrary.PhotoLibraryRoute
import com.wei.picquest.feature.photo.photosearch.navigation.photoSearchRoute
import java.net.URLDecoder
import java.net.URLEncoder

private val URL_CHARACTER_ENCODING = Charsets.UTF_8.name()

@VisibleForTesting
internal const val photoSearchQueryArg = "photoSearchQuery"

internal class PhotoLibraryArgs(val photoSearchQuery: String) {
    constructor(savedStateHandle: SavedStateHandle) :
        this(
            URLDecoder.decode(
                checkNotNull(savedStateHandle[photoSearchQueryArg]),
                URL_CHARACTER_ENCODING,
            ),
        )
}

fun NavController.navigateToPhotoLibrary(photoSearchQuery: String) {
    val encodedKey = URLEncoder.encode(
        photoSearchQuery.ifBlank { "$photoSearchQuery " },
        URL_CHARACTER_ENCODING,
    )
    this.navigate("$photoSearchRoute/$encodedKey") {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.photoLibraryGraph(
    navController: NavController,
) {
    composable(
        route = "$photoSearchRoute/{$photoSearchQueryArg}",
        arguments = listOf(
            navArgument(photoSearchQueryArg) { type = NavType.StringType },
        ),
    ) {
        PhotoLibraryRoute(
            navController = navController,
        )
    }
}
