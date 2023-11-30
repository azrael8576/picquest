package com.wei.picquest.ui

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.window.layout.DisplayFeature
import com.wei.picquest.R
import com.wei.picquest.core.data.utils.NetworkMonitor
import com.wei.picquest.core.designsystem.component.FunctionalityNotAvailablePopup
import com.wei.picquest.core.designsystem.component.PqAppSnackbar
import com.wei.picquest.core.designsystem.component.PqBackground
import com.wei.picquest.core.designsystem.component.PqNavigationBar
import com.wei.picquest.core.designsystem.component.PqNavigationBarItem
import com.wei.picquest.core.designsystem.component.PqNavigationDrawer
import com.wei.picquest.core.designsystem.component.PqNavigationDrawerItem
import com.wei.picquest.core.designsystem.component.PqNavigationRail
import com.wei.picquest.core.designsystem.component.PqNavigationRailItem
import com.wei.picquest.core.designsystem.theme.SPACING_LARGE
import com.wei.picquest.core.designsystem.ui.PqNavigationType
import com.wei.picquest.core.manager.ErrorTextPrefix
import com.wei.picquest.core.manager.Message
import com.wei.picquest.core.manager.SnackbarManager
import com.wei.picquest.core.manager.SnackbarState
import com.wei.picquest.core.utils.UiText
import com.wei.picquest.navigation.PqNavHost
import com.wei.picquest.navigation.TopLevelDestination

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalLayoutApi::class,
    ExperimentalComposeUiApi::class,
)
@Composable
fun PqApp(
    networkMonitor: NetworkMonitor,
    windowSizeClass: WindowSizeClass,
    displayFeatures: List<DisplayFeature>,
    appState: PqAppState = rememberPqAppState(
        networkMonitor = networkMonitor,
        windowSizeClass = windowSizeClass,
        displayFeatures = displayFeatures,
    ),
    snackbarManager: SnackbarManager,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    val pqBottomBar = stringResource(R.string.tag_pq_bottom_bar)
    val pqNavRail = stringResource(R.string.tag_pq_nav_rail)
    val pqNavDrawer = stringResource(R.string.tag_pq_nav_drawer)

    if (appState.showFunctionalityNotAvailablePopup.value) {
        FunctionalityNotAvailablePopup(
            onDismiss = {
                appState.showFunctionalityNotAvailablePopup.value = false
            },
        )
    }

    val isOffline by appState.isOffline.collectAsStateWithLifecycle()

    // If user is not connected to the internet show a snack bar to inform them.
    val notConnectedMessage = stringResource(R.string.not_connected)
    LaunchedEffect(isOffline) {
        if (isOffline) {
            snackbarManager.showMessage(
                state = SnackbarState.Error,
                uiText = UiText.DynamicString(notConnectedMessage),
            )
        }
    }

    LaunchedEffect(key1 = snackbarHostState) {
        collectAndShowSnackbar(snackbarManager, snackbarHostState, context)
    }
    PqBackground {
        Scaffold(
            modifier = Modifier.semantics {
                testTagsAsResourceId = true
            },
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onBackground,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            snackbarHost = {
                SnackbarHost(
                    hostState = snackbarHostState,
                    snackbar = { snackbarData ->
                        if (!appState.isFullScreenCurrentDestination) {
                            val isError = snackbarData.visuals.message.startsWith(ErrorTextPrefix)
                            PqAppSnackbar(snackbarData, isError)
                        }
                    },
                )
            },
            bottomBar = {
                if (!appState.isFullScreenCurrentDestination &&
                    appState.navigationType == PqNavigationType.BOTTOM_NAVIGATION
                ) {
                    PqBottomBar(
                        destinations = appState.topLevelDestinations,
                        onNavigateToDestination = appState::navigateToTopLevelDestination,
                        currentDestination = appState.currentDestination,
                        modifier = Modifier.testTag(pqBottomBar),
                    )
                }
            },
        ) { padding ->
            Row(
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .consumeWindowInsets(padding)
                    .windowInsetsPadding(
                        WindowInsets.safeDrawing.only(
                            WindowInsetsSides.Horizontal,
                        ),
                    ),
            ) {
                if (!appState.isFullScreenCurrentDestination &&
                    appState.navigationType == PqNavigationType.PERMANENT_NAVIGATION_DRAWER
                ) {
                    PqNavDrawer(
                        destinations = appState.topLevelDestinations,
                        onNavigateToDestination = appState::navigateToTopLevelDestination,
                        currentDestination = appState.currentDestination,
                        modifier = Modifier
                            .testTag(pqNavDrawer)
                            .padding(SPACING_LARGE.dp)
                            .safeDrawingPadding(),
                    )
                }

                if (!appState.isFullScreenCurrentDestination &&
                    appState.navigationType == PqNavigationType.NAVIGATION_RAIL
                ) {
                    PqNavRail(
                        destinations = appState.topLevelDestinations,
                        onNavigateToDestination = appState::navigateToTopLevelDestination,
                        currentDestination = appState.currentDestination,
                        modifier = Modifier
                            .testTag(pqNavRail)
                            .safeDrawingPadding(),
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                ) {
                    PqNavHost(
                        modifier = Modifier.fillMaxSize(),
                        appState = appState,
                        displayFeatures = displayFeatures,
                    )
                }
            }
        }
    }
}

@Composable
private fun PqNavDrawer(
    destinations: List<TopLevelDestination>,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier,
) {
    PqNavigationDrawer(modifier = modifier) {
        destinations.forEach { destination ->
            val selected = currentDestination.isTopLevelDestinationInHierarchy(destination)
            PqNavigationDrawerItem(
                modifier = Modifier,
                selected = selected,
                onClick = { onNavigateToDestination(destination) },
                icon = {
                    Icon(
                        imageVector = destination.unselectedIcon,
                        contentDescription = stringResource(destination.iconTextId),
                    )
                },
                selectedIcon = {
                    Icon(
                        imageVector = destination.selectedIcon,
                        contentDescription = stringResource(destination.iconTextId),
                    )
                },
            ) {
                Text(
                    text = stringResource(id = destination.iconTextId),
                )
            }
        }
    }
}

@Composable
private fun PqNavRail(
    destinations: List<TopLevelDestination>,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier,
) {
    PqNavigationRail(modifier = modifier) {
        destinations.forEach { destination ->
            val selected = currentDestination.isTopLevelDestinationInHierarchy(destination)
            PqNavigationRailItem(
                selected = selected,
                onClick = { onNavigateToDestination(destination) },
                icon = {
                    Icon(
                        imageVector = destination.unselectedIcon,
                        contentDescription = stringResource(destination.iconTextId),
                    )
                },
                modifier = Modifier,
                selectedIcon = {
                    Icon(
                        imageVector = destination.selectedIcon,
                        contentDescription = stringResource(destination.iconTextId),
                    )
                },
            )
        }
    }
}

@Composable
private fun PqBottomBar(
    destinations: List<TopLevelDestination>,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier,
) {
    PqNavigationBar(
        modifier = modifier,
    ) {
        destinations.forEach { destination ->
            val selected = currentDestination.isTopLevelDestinationInHierarchy(destination)
            PqNavigationBarItem(
                modifier = Modifier,
                selected = selected,
                onClick = { onNavigateToDestination(destination) },
                icon = {
                    Icon(
                        imageVector = destination.unselectedIcon,
                        contentDescription = stringResource(id = destination.iconTextId),
                    )
                },
                selectedIcon = {
                    Icon(
                        imageVector = destination.selectedIcon,
                        contentDescription = stringResource(id = destination.iconTextId),
                    )
                },
                label = { Text(stringResource(destination.iconTextId)) },
            )
        }
    }
}

private fun NavDestination?.isTopLevelDestinationInHierarchy(destination: TopLevelDestination) =
    this?.hierarchy?.any {
        it.route?.contains(destination.name, true) ?: false
    } ?: false

suspend fun collectAndShowSnackbar(
    snackbarManager: SnackbarManager,
    snackbarHostState: SnackbarHostState,
    context: Context,
) {
    snackbarManager.messages.collect { messages ->
        if (messages.isNotEmpty()) {
            val message = messages.first()
            val text = getMessageText(message, context)

            if (message.state == SnackbarState.Error) {
                snackbarHostState.showSnackbar(
                    message = ErrorTextPrefix + text,
                )
            } else {
                snackbarHostState.showSnackbar(message = text)
            }
            snackbarManager.setMessageShown(message.id)
        }
    }
}

fun getMessageText(message: Message, context: Context): String {
    return when (message.uiText) {
        is UiText.DynamicString -> (message.uiText as UiText.DynamicString).value
        is UiText.StringResource -> context.getString(
            (message.uiText as UiText.StringResource).resId,
            *(message.uiText as UiText.StringResource).args.map { it.toString(context) }
                .toTypedArray(),
        )
    }
}
