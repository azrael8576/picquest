import androidx.annotation.StringRes
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.wei.picquest.MainActivity
import kotlin.properties.ReadOnlyProperty

/**
 * Robot for [NavigationTest].
 *
 * 遵循此模型，找到測試使用者介面元素、檢查其屬性、和透過測試規則執行動作：
 * composeTestRule{.finder}{.assertion}{.action}
 *
 * Testing cheatsheet：
 * https://developer.android.com/jetpack/compose/testing-cheatsheet
 */
internal fun navigationRobot(
    composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>,
    func: NavigationRobot.() -> Unit,
) = NavigationRobot(composeTestRule).apply(func)

internal open class NavigationRobot(
    private val composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>,
) {
    private fun AndroidComposeTestRule<*, *>.stringResource(@StringRes resId: Int) =
        ReadOnlyProperty<Any?, String> { _, _ -> activity.getString(resId) }

    // The strings used for matching in these tests
    private val home by composeTestRule.stringResource(com.wei.picquest.R.string.home)
    private val photo by composeTestRule.stringResource(com.wei.picquest.R.string.photo)
    private val video by composeTestRule.stringResource(com.wei.picquest.R.string.video)
    private val contactMe by composeTestRule.stringResource(com.wei.picquest.R.string.contact_me)

    private val navHome by lazy {
        composeTestRule.onNodeWithContentDescription(
            home,
            useUnmergedTree = true,
        )
    }
    private val navPhoto by lazy {
        composeTestRule.onNodeWithContentDescription(
            photo,
            useUnmergedTree = true,
        )
    }
    private val navVideo by lazy {
        composeTestRule.onNodeWithContentDescription(
            video,
            useUnmergedTree = true,
        )
    }
    private val navContactMe by lazy {
        composeTestRule.onNodeWithContentDescription(
            contactMe,
            useUnmergedTree = true,
        )
    }

    internal fun clickNavHome() {
        navHome.performClick()
        // 等待任何動畫完成
        composeTestRule.waitForIdle()
    }

    internal fun clickNavPhoto() {
        navPhoto.performClick()
        // 等待任何動畫完成
        composeTestRule.waitForIdle()
    }

    internal fun clickVideo() {
        navVideo.performClick()
        // 等待任何動畫完成
        composeTestRule.waitForIdle()
    }

    internal fun clickNavContactMe() {
        navContactMe.performClick()
        // 等待任何動畫完成
        composeTestRule.waitForIdle()
    }
}
