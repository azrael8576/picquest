package com.wei.picquest.feature.video.videosearch

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.wei.picquest.core.designsystem.component.FunctionalityNotAvailablePopup
import com.wei.picquest.core.designsystem.component.ThemePreviews
import com.wei.picquest.core.designsystem.icon.PqIcons
import com.wei.picquest.core.designsystem.theme.PqTheme
import com.wei.picquest.core.designsystem.theme.SPACING_LARGE
import com.wei.picquest.core.designsystem.theme.SPACING_SMALL
import com.wei.picquest.core.designsystem.ui.TrackScreenViewEvent
import com.wei.picquest.feature.video.R
import com.wei.picquest.feature.video.videolibrary.navigation.navigateToVideoLibrary

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
internal fun VideoSearchRoute(
    navController: NavController,
    viewModel: VideoSearchViewModel = hiltViewModel(),
) {
    val uiStates: VideoSearchViewState by viewModel.states.collectAsStateWithLifecycle()

    VideoSearchScreen(
        uiStates = uiStates,
        onSearchQueryChanged = {
            viewModel.dispatch(VideoSearchViewAction.SearchQueryChanged(it))
        },
        onSearchTriggered = {
            viewModel.dispatch(VideoSearchViewAction.SearchTriggered(it))
            navController.navigateToVideoLibrary(it)
        },
        onRecentSearchClicked = {
            viewModel.dispatch(VideoSearchViewAction.RecentSearchClicked(it))
            navController.navigateToVideoLibrary(it)
        },
        onClearRecentSearches = {
            viewModel.dispatch(VideoSearchViewAction.ClearRecentSearchQueriesClicked)
        },
    )
}

@Composable
internal fun VideoSearchScreen(
    uiStates: VideoSearchViewState,
    onSearchQueryChanged: (String) -> Unit,
    onSearchTriggered: (String) -> Unit,
    onRecentSearchClicked: (String) -> Unit,
    onClearRecentSearches: () -> Unit,
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

            Column {
                SearchTextField(
                    onSearchQueryChanged = onSearchQueryChanged,
                    onSearchTriggered = onSearchTriggered,
                    searchQuery = uiStates.searchQuery,
                )
                RecentSearchesBody(
                    modifier = Modifier.weight(1f),
                    onClearRecentSearches = onClearRecentSearches,
                    onRecentSearchClicked = onRecentSearchClicked,
                    recentSearchQueries = uiStates.recentSearchQueries,
                )
            }

            if (withBottomSpacer) {
                Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
            }
        }
    }
    TrackScreenViewEvent(screenName = "VideoSearch")
}

@Composable
private fun RecentSearchesBody(
    modifier: Modifier = Modifier,
    onClearRecentSearches: () -> Unit,
    onRecentSearchClicked: (String) -> Unit,
    recentSearchQueries: List<String>,
) {
    Column(modifier = modifier) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text =
                buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(stringResource(id = R.string.feature_video_recent_video_searches))
                    }
                },
                modifier =
                Modifier.padding(
                    horizontal = SPACING_LARGE.dp,
                    vertical = SPACING_SMALL.dp,
                ),
            )
            if (recentSearchQueries.isNotEmpty()) {
                IconButton(
                    onClick = {
                        onClearRecentSearches()
                    },
                    modifier = Modifier.padding(horizontal = SPACING_LARGE.dp),
                ) {
                    Icon(
                        imageVector = PqIcons.Close,
                        contentDescription =
                        stringResource(
                            id = R.string.feature_video_clear_recent_searches_content_desc,
                        ),
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        }
        LazyColumn(modifier = Modifier.padding(horizontal = SPACING_LARGE.dp)) {
            items(recentSearchQueries) { recentSearch ->
                Text(
                    text = recentSearch,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier =
                    Modifier
                        .padding(vertical = SPACING_LARGE.dp)
                        .clickable { onRecentSearchClicked(recentSearch) }
                        .fillMaxWidth(),
                )
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun SearchTextField(
    onSearchQueryChanged: (String) -> Unit,
    searchQuery: String,
    onSearchTriggered: (String) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    val onSearchExplicitlyTriggered = {
        keyboardController?.hide()
        onSearchTriggered(searchQuery)
    }

    TextField(
        colors =
        TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
        ),
        leadingIcon = {
            Icon(
                imageVector = PqIcons.Search,
                contentDescription = stringResource(R.string.feature_video_search),
                tint = MaterialTheme.colorScheme.onSurface,
            )
        },
        trailingIcon = {
            if (searchQuery.isNotEmpty()) {
                IconButton(
                    onClick = {
                        onSearchQueryChanged("")
                    },
                ) {
                    Icon(
                        imageVector = PqIcons.Close,
                        contentDescription = stringResource(R.string.feature_video_clear_search_text_content_desc),
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        },
        onValueChange = {
            if (!it.contains("\n")) {
                onSearchQueryChanged(it)
            }
        },
        modifier =
        Modifier
            .fillMaxWidth()
            .padding(SPACING_LARGE.dp)
            .focusRequester(focusRequester)
            .onKeyEvent {
                if (it.key == Key.Enter) {
                    onSearchExplicitlyTriggered()
                    true
                } else {
                    false
                }
            }
            .testTag("searchTextField"),
        shape = RoundedCornerShape(32.dp),
        value = searchQuery,
        keyboardOptions =
        KeyboardOptions(
            imeAction = ImeAction.Search,
        ),
        keyboardActions =
        KeyboardActions(
            onSearch = {
                onSearchExplicitlyTriggered()
            },
        ),
        maxLines = 1,
        singleLine = true,
    )
    // TODO move to ViewModel
//    LaunchedEffect(Unit) {
//        focusRequester.requestFocus()
//    }
}

@ThemePreviews
@Composable
fun VideoSearchScreenPreview() {
    PqTheme {
        VideoSearchScreen(
            uiStates =
            VideoSearchViewState(
                searchQuery = "cat video",
                recentSearchQueries =
                listOf(
                    "cat",
                    "mouse",
                ),
            ),
            onSearchQueryChanged = {},
            onSearchTriggered = {},
            onRecentSearchClicked = {},
            onClearRecentSearches = {},
        )
    }
}
