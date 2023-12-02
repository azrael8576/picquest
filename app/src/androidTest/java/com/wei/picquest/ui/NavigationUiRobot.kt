
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.DpSize
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.window.layout.FoldingFeature
import com.google.accompanist.testharness.TestHarness
import com.wei.picquest.R
import com.wei.picquest.core.data.utils.NetworkMonitor
import com.wei.picquest.core.manager.SnackbarManager
import com.wei.picquest.ui.PqApp
import com.wei.picquest.uitesthiltmanifest.HiltComponentActivity
import com.wei.picquest.utilities.FoldingDeviceUtil
import kotlin.properties.ReadOnlyProperty

/**
 * Robot for [NavigationUiTest].
 *
 * 遵循此模型，找到測試使用者介面元素、檢查其屬性、和透過測試規則執行動作：
 * composeTestRule{.finder}{.assertion}{.action}
 *
 * Testing cheatsheet：
 * https://developer.android.com/jetpack/compose/testing-cheatsheet
 */
internal fun navigationUiRobot(
    composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<HiltComponentActivity>, HiltComponentActivity>,
    func: NavigationUiRobot.() -> Unit,
) = NavigationUiRobot(composeTestRule).apply(func)

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
internal open class NavigationUiRobot(
    private val composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<HiltComponentActivity>, HiltComponentActivity>,
) {
    private fun AndroidComposeTestRule<*, *>.stringResource(@StringRes resId: Int) =
        ReadOnlyProperty<Any?, String> { _, _ -> activity.getString(resId) }

    // The strings used for matching in these tests
    private val pqBottomBarTag by composeTestRule.stringResource(R.string.tag_pq_bottom_bar)
    private val pqNavRailTag by composeTestRule.stringResource(R.string.tag_pq_nav_rail)
    private val pqNavDrawerTag by composeTestRule.stringResource(R.string.tag_pq_nav_drawer)

    private val pqBottomBar by lazy {
        composeTestRule.onNodeWithTag(
            pqBottomBarTag,
            useUnmergedTree = true,
        )
    }

    private val pqNavRail by lazy {
        composeTestRule.onNodeWithTag(
            pqNavRailTag,
            useUnmergedTree = true,
        )
    }
    private val pqNavDrawer by lazy {
        composeTestRule.onNodeWithTag(
            pqNavDrawerTag,
            useUnmergedTree = true,
        )
    }

    fun setPqAppContent(
        dpSize: DpSize,
        networkMonitor: NetworkMonitor,
        snackbarManager: SnackbarManager,
        foldingState: FoldingFeature.State? = null,
    ) {
        composeTestRule.setContent {
            TestHarness(dpSize) {
                BoxWithConstraints {
                    val displayFeatures = if (foldingState != null) {
                        val foldBounds = FoldingDeviceUtil.getFoldBounds(dpSize)
                        listOf(FoldingDeviceUtil.getFoldingFeature(foldBounds, foldingState))
                    } else {
                        emptyList()
                    }

                    PqApp(
                        windowSizeClass = WindowSizeClass.calculateFromSize(dpSize),
                        networkMonitor = networkMonitor,
                        displayFeatures = displayFeatures,
                        snackbarManager = snackbarManager,
                    )
                }
            }
        }
    }

    fun setPqAppContentWithBookPosture(
        dpSize: DpSize,
        networkMonitor: NetworkMonitor,
        snackbarManager: SnackbarManager,
    ) {
        setPqAppContent(dpSize, networkMonitor, snackbarManager, FoldingFeature.State.HALF_OPENED)
    }

    fun verifyPqBottomBarDisplayed() {
        pqBottomBar.assertExists().assertIsDisplayed()
    }

    fun verifyPqNavRailDisplayed() {
        pqNavRail.assertExists().assertIsDisplayed()
    }

    fun verifyPqNavDrawerDisplayed() {
        pqNavDrawer.assertExists().assertIsDisplayed()
    }

    fun verifyPqBottomBarDoesNotExist() {
        pqBottomBar.assertDoesNotExist()
    }

    fun verifyPqNavRailDoesNotExist() {
        pqNavRail.assertDoesNotExist()
    }

    fun verifyPqNavDrawerDoesNotExist() {
        pqNavDrawer.assertDoesNotExist()
    }
}
