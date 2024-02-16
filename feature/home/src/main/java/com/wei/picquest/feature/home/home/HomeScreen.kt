package com.wei.picquest.feature.home.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.wei.picquest.core.designsystem.component.FunctionalityNotAvailablePopup
import com.wei.picquest.core.designsystem.component.ThemePreviews
import com.wei.picquest.core.designsystem.theme.PqTheme
import com.wei.picquest.core.designsystem.theme.SPACING_MEDIUM
import com.wei.picquest.core.designsystem.ui.TrackScreenViewEvent
import com.wei.picquest.feature.home.R

/**
 *
 * UI 事件決策樹
 * 下圖顯示了一個決策樹，用於查找處理特定事件用例的最佳方法。
 *
 *                                                      ┌───────┐
 *                                                      │ Start │
 *                                                      └───┬───┘
 *                                                          ↓
 *                                       ┌───────────────────────────────────┐
 *                                       │ Where is event originated?        │
 *                                       └──────┬─────────────────────┬──────┘
 *                                              ↓                     ↓
 *                                              UI                  ViewModel
 *                                              │                     │
 *                           ┌─────────────────────────┐      ┌───────────────┐
 *                           │ When the event requires │      │ Update the UI │
 *                           │ ...                     │      │ State         │
 *                           └─┬─────────────────────┬─┘      └───────────────┘
 *                             ↓                     ↓
 *                        Business logic      UI behavior logic
 *                             │                     │
 *     ┌─────────────────────────────────┐   ┌──────────────────────────────────────┐
 *     │ Delegate the business logic to  │   │ Modify the UI element state in the   │
 *     │ the ViewModel                   │   │ UI directly                          │
 *     └─────────────────────────────────┘   └──────────────────────────────────────┘
 *
 *
 */
@Composable
internal fun HomeRoute(navController: NavController) {
    HomeScreen()
}

@Composable
internal fun HomeScreen(
    withTopSpacer: Boolean = true,
    withBottomSpacer: Boolean = true,
) {
    val showPopup = remember { mutableStateOf(false) }

    if (showPopup.value) {
        FunctionalityNotAvailablePopup(
            onDismiss = {
                showPopup.value = false
            },
        )
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (withTopSpacer) {
                Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
            }
            LazyColumn {
                item {
                    val importantNote = stringResource(id = R.string.feature_home_important_note)
                    val importantNoteMessage = stringResource(id = R.string.feature_home_important_note_message)
                    Text(
                        text = importantNote,
                        style = MaterialTheme.typography.titleLarge,
                        modifier =
                        Modifier
                            .padding(SPACING_MEDIUM.dp)
                            .semantics { contentDescription = importantNote },
                    )
                    Text(
                        text = importantNoteMessage,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier =
                        Modifier
                            .padding(SPACING_MEDIUM.dp)
                            .semantics { contentDescription = importantNoteMessage },
                    )
                }
            }
            if (withBottomSpacer) {
                Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
            }
        }
    }
    TrackScreenViewEvent(screenName = "Home")
}

@ThemePreviews
@Composable
fun HomeScreenPreview() {
    PqTheme {
        HomeScreen()
    }
}
