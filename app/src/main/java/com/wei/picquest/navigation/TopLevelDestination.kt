package com.wei.picquest.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import com.wei.picquest.R
import com.wei.picquest.core.designsystem.icon.PqIcons

/**
 * Type for the top level destinations in the application. Each of these destinations
 * can contain one or more screens (based on the window size). Navigation from one screen to the
 * next within a single destination will be handled directly in composables.
 */
enum class TopLevelDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconTextId: Int,
    val titleTextId: Int,
) {
    HOME(
        selectedIcon = PqIcons.Home,
        unselectedIcon = PqIcons.HomeBorder,
        iconTextId = R.string.home,
        titleTextId = R.string.home,
    ),
    PHOTO_LIBRARY(
        selectedIcon = PqIcons.PhotoLibrary,
        unselectedIcon = PqIcons.PhotoLibraryBorder,
        iconTextId = R.string.photo_library,
        titleTextId = R.string.photo_library,
    ),
    CONTACT_ME(
        selectedIcon = PqIcons.ContactMe,
        unselectedIcon = PqIcons.ContactMeBorder,
        iconTextId = R.string.contact_me,
        titleTextId = R.string.contact_me,
    ),
}
