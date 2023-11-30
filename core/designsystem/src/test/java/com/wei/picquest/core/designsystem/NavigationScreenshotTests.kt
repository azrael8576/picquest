package com.wei.picquest.core.designsystem

import androidx.activity.ComponentActivity
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onRoot
import com.github.takahirom.roborazzi.captureRoboImage
import com.google.accompanist.testharness.TestHarness
import com.wei.picquest.core.designsystem.component.PqNavigationBar
import com.wei.picquest.core.designsystem.component.PqNavigationBarItem
import com.wei.picquest.core.designsystem.component.PqNavigationDrawer
import com.wei.picquest.core.designsystem.component.PqNavigationDrawerItem
import com.wei.picquest.core.designsystem.component.PqNavigationRail
import com.wei.picquest.core.designsystem.component.PqNavigationRailItem
import com.wei.picquest.core.designsystem.icon.PqIcons
import com.wei.picquest.core.designsystem.theme.PqTheme
import com.wei.picquest.core.testing.util.DefaultRoborazziOptions
import com.wei.picquest.core.testing.util.captureMultiTheme
import dagger.hilt.android.testing.HiltTestApplication
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import org.robolectric.annotation.LooperMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = HiltTestApplication::class, sdk = [33], qualifiers = "480dpi")
@LooperMode(LooperMode.Mode.PAUSED)
class NavigationScreenshotTests() {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun navigationBar_multipleThemes() {
        composeTestRule.captureMultiTheme("NavigationBar") {
            Surface {
                PqNavigationBarExample()
            }
        }
    }

    @Test
    fun navigationBar_hugeFont() {
        composeTestRule.setContent {
            CompositionLocalProvider(
                LocalInspectionMode provides true,
            ) {
                TestHarness(fontScale = 2f) {
                    PqTheme {
                        PqNavigationBarExample("Looong item")
                    }
                }
            }
        }
        composeTestRule.onRoot()
            .captureRoboImage(
                "src/test/screenshots/NavigationBar" +
                    "/NavigationBar_fontScale2.png",
                roborazziOptions = DefaultRoborazziOptions,
            )
    }

    @Test
    fun navigationRail_multipleThemes() {
        composeTestRule.captureMultiTheme("NavigationRail") {
            Surface {
                PqNavigationRailExample()
            }
        }
    }

    @Test
    fun navigationDrawer_multipleThemes() {
        composeTestRule.captureMultiTheme("NavigationDrawer") {
            Surface {
                PqNavigationDrawerExample()
            }
        }
    }

    @Test
    fun navigationDrawer_hugeFont() {
        composeTestRule.setContent {
            CompositionLocalProvider(
                LocalInspectionMode provides true,
            ) {
                TestHarness(fontScale = 2f) {
                    PqTheme {
                        PqNavigationDrawerExample("Loooooooooooooooong item")
                    }
                }
            }
        }
        composeTestRule.onRoot()
            .captureRoboImage(
                "src/test/screenshots/NavigationDrawer" +
                    "/NavigationDrawer_fontScale2.png",
                roborazziOptions = DefaultRoborazziOptions,
            )
    }

    @Composable
    private fun PqNavigationBarExample(label: String = "Item") {
        PqNavigationBar {
            (0..2).forEach { index ->
                PqNavigationBarItem(
                    selected = index == 0,
                    onClick = { },
                    icon = {
                        Icon(
                            imageVector = PqIcons.UpcomingBorder,
                            contentDescription = "",
                        )
                    },
                    selectedIcon = {
                        Icon(
                            imageVector = PqIcons.Upcoming,
                            contentDescription = "",
                        )
                    },
                    label = { Text(label) },
                )
            }
        }
    }

    @Composable
    private fun PqNavigationRailExample() {
        PqNavigationRail {
            (0..2).forEach { index ->
                PqNavigationRailItem(
                    selected = index == 0,
                    onClick = { },
                    icon = {
                        Icon(
                            imageVector = PqIcons.UpcomingBorder,
                            contentDescription = "",
                        )
                    },
                    selectedIcon = {
                        Icon(
                            imageVector = PqIcons.Upcoming,
                            contentDescription = "",
                        )
                    },
                )
            }
        }
    }

    @Composable
    private fun PqNavigationDrawerExample(label: String = "Item") {
        PqNavigationDrawer {
            (0..2).forEach { index ->
                PqNavigationDrawerItem(
                    selected = index == 0,
                    onClick = { },
                    icon = {
                        Icon(
                            imageVector = PqIcons.UpcomingBorder,
                            contentDescription = "",
                        )
                    },
                    selectedIcon = {
                        Icon(
                            imageVector = PqIcons.Upcoming,
                            contentDescription = "",
                        )
                    },
                    label = { Text(label) },
                )
            }
        }
    }
}
