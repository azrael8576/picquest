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
internal const val photoSearchKeywordArg = "photoSearchKeyword"

internal class PhotoLibraryArgs(val photoSearchKeyword: String) {
    constructor(savedStateHandle: SavedStateHandle) :
        this(
            URLDecoder.decode(
                checkNotNull(savedStateHandle[photoSearchKeywordArg]),
                URL_CHARACTER_ENCODING,
            ),
        )
}

fun NavController.navigateToPhotoLibrary(photoSearchKeyword: String) {
    val encodedKey = URLEncoder.encode(photoSearchKeyword, URL_CHARACTER_ENCODING)
    this.navigate("$photoSearchRoute/$encodedKey") {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.photoLibraryGraph(
    navController: NavController,
) {
    composable(
        route = "$photoSearchRoute/{$photoSearchKeywordArg}",
        arguments = listOf(
            navArgument(photoSearchKeywordArg) { type = NavType.StringType },
        ),
    ) {
        PhotoLibraryRoute(
            navController = navController,
        )
    }
}
