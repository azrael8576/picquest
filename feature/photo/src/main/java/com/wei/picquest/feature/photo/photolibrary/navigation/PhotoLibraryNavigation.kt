package com.wei.picquest.feature.photo.photolibrary.navigation

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.wei.picquest.feature.photo.photolibrary.PhotoLibraryRoute
import com.wei.picquest.feature.photo.photosearch.navigation.PHOTO_SEARCH_ROUTE
import java.net.URLDecoder
import java.net.URLEncoder

private val URL_CHARACTER_ENCODING = Charsets.UTF_8.name()

@VisibleForTesting
internal const val PHOTO_SEARCH_QUERY_ARG = "photoSearchQuery"

internal class PhotoLibraryArgs(val photoSearchQuery: String) {
    constructor(savedStateHandle: SavedStateHandle) :
        this(
            URLDecoder.decode(
                checkNotNull(savedStateHandle[PHOTO_SEARCH_QUERY_ARG]),
                URL_CHARACTER_ENCODING,
            ),
        )
}

fun NavController.navigateToPhotoLibrary(photoSearchQuery: String) {
    val encodedKey =
        URLEncoder.encode(
            photoSearchQuery.ifBlank { "$photoSearchQuery " },
            URL_CHARACTER_ENCODING,
        )
    this.navigate("$PHOTO_SEARCH_ROUTE/$encodedKey") {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.photoLibraryGraph(navController: NavController) {
    composable(
        route = "$PHOTO_SEARCH_ROUTE/{$PHOTO_SEARCH_QUERY_ARG}",
        arguments =
        listOf(
            navArgument(PHOTO_SEARCH_QUERY_ARG) { type = NavType.StringType },
        ),
    ) {
        PhotoLibraryRoute(
            navController = navController,
        )
    }
}
