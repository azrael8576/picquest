package com.wei.picquest

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.google.accompanist.adaptive.calculateDisplayFeatures
import com.wei.picquest.core.data.utils.NetworkMonitor
import com.wei.picquest.core.designsystem.theme.PqTheme
import com.wei.picquest.core.manager.SnackbarManager
import com.wei.picquest.ui.PqApp
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var snackbarManager: SnackbarManager

    @Inject
    lateinit var networkMonitor: NetworkMonitor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()

        splashScreen.setKeepOnScreenCondition { true }

        // Turn off the decor fitting system windows, which allows us to handle insets,
        // including IME animations, and go edge-to-edge
        // This also sets up the initial system bar style based on the platform theme
        enableEdgeToEdge()

        setContent {
            val darkTheme = shouldUseDarkTheme()

            // Update the edge to edge configuration to match the theme
            // This is the same parameters as the default enableEdgeToEdge call, but we manually
            // resolve whether or not to show dark theme using uiState, since it can be different
            // than the configuration's dark theme value based on the user preference.
            DisposableEffect(darkTheme) {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.auto(
                        Color.TRANSPARENT,
                        Color.TRANSPARENT,
                    ) { darkTheme },
                    navigationBarStyle = SystemBarStyle.auto(
                        lightScrim,
                        darkScrim,
                    ) { darkTheme },
                )
                onDispose {}
            }

            CompositionLocalProvider() {
                PqTheme(darkTheme = darkTheme) {
                    PqApp(
                        networkMonitor = networkMonitor,
                        windowSizeClass = calculateWindowSizeClass(this@MainActivity),
                        displayFeatures = calculateDisplayFeatures(this@MainActivity),
                        snackbarManager = snackbarManager,
                    )
                }
            }
        }

        lifecycleScope.launch {
            // TODO Wei Loading user data,
            //  e.g. Photo ViewType, Search record
            delay(2_000)
            splashScreen.setKeepOnScreenCondition { false }
        }
    }
}

/**
 * Returns `true` if dark theme should be used, as a function of the [uiState] and the
 * current system context.
 */
@Composable
private fun shouldUseDarkTheme(): Boolean = isSystemInDarkTheme()

/**
 * The default light scrim, as defined by androidx and the platform:
 * https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:activity/activity/src/main/java/androidx/activity/EdgeToEdge.kt;l=35-38;drc=27e7d52e8604a080133e8b842db10c89b4482598
 */
private val lightScrim = android.graphics.Color.argb(0xe6, 0xFF, 0xFF, 0xFF)

/**
 * The default dark scrim, as defined by androidx and the platform:
 * https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:activity/activity/src/main/java/androidx/activity/EdgeToEdge.kt;l=40-44;drc=27e7d52e8604a080133e8b842db10c89b4482598
 */
private val darkScrim = android.graphics.Color.argb(0x80, 0x1b, 0x1b, 0x1b)
