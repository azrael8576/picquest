package com.wei.picquest.feature.contactme.contactme.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.window.layout.DisplayFeature
import com.wei.picquest.core.designsystem.ui.PqContentType
import com.wei.picquest.core.designsystem.ui.PqNavigationType
import com.wei.picquest.feature.contactme.contactme.ContactMeRoute

const val CONTACT_ME_ROUTE = "contact_me_route"

fun NavController.navigateToContactMe(navOptions: NavOptions? = null) {
    this.navigate(CONTACT_ME_ROUTE, navOptions)
}

fun NavGraphBuilder.contactMeGraph(
    navController: NavController,
    contentType: PqContentType,
    displayFeatures: List<DisplayFeature>,
    navigationType: PqNavigationType,
    nestedGraphs: NavGraphBuilder.() -> Unit,
) {
    composable(route = CONTACT_ME_ROUTE) {
        ContactMeRoute(
            navController = navController,
            contentType = contentType,
            displayFeatures = displayFeatures,
            navigationType = navigationType,
        )
    }
}
