package com.wei.picquest.feature.photo.photolibrary

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.wei.picquest.core.designsystem.component.coilImagePainter
import com.wei.picquest.core.designsystem.icon.PqIcons
import com.wei.picquest.core.designsystem.theme.PqTheme
import com.wei.picquest.core.designsystem.theme.SPACING_LARGE
import com.wei.picquest.core.designsystem.theme.SPACING_MEDIUM
import com.wei.picquest.core.designsystem.theme.SPACING_SMALL
import com.wei.picquest.core.designsystem.ui.TrackScreenViewEvent
import com.wei.picquest.core.model.data.ImageDetail
import com.wei.picquest.feature.photo.R
import com.wei.picquest.feature.photo.photolibrary.component.LayoutSwitchWarningDialog
import com.wei.picquest.feature.photo.photolibrary.data.fake.fakeImageDetails
import kotlinx.coroutines.flow.MutableStateFlow

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
internal fun PhotoLibraryRoute(
    navController: NavController,
    viewModel: PhotoLibraryViewModel = hiltViewModel(),
) {
    val photoSearchQuery = viewModel.photoSearchQuery
    val lazyPagingItems = viewModel.imagesState.collectAsLazyPagingItems()
    val uiStates: PhotoLibraryViewState by viewModel.states.collectAsStateWithLifecycle()

    Surface(modifier = Modifier.fillMaxSize()) {
        Box {
            when (uiStates.layoutType) {
                LayoutType.LIST ->
                    PhotoLibraryListScreen(
                        photoSearchQuery = photoSearchQuery,
                        lazyPagingItems = lazyPagingItems,
                    )

                LayoutType.GRID ->
                    PhotoLibraryGridScreen(
                        photoSearchQuery = photoSearchQuery,
                        lazyPagingItems = lazyPagingItems,
                    )
            }

            TopBarActions(
                layoutType = uiStates.layoutType,
                onBackClick = navController::popBackStack,
                onSwitchLayoutClick = {
                    viewModel.dispatch(PhotoLibraryViewAction.SwitchLayoutType)
                },
            )
        }
    }
}

@Composable
fun TopBarActions(
    layoutType: LayoutType,
    onBackClick: () -> Unit,
    onSwitchLayoutClick: () -> Unit,
) {
    Column {
        Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
        Row(modifier = Modifier.padding(SPACING_MEDIUM.dp)) {
            BackButton(onBackClick = onBackClick)
            Spacer(modifier = Modifier.weight(1f))
            SwitchLayoutButton(
                layoutType = layoutType,
                onClick = onSwitchLayoutClick,
            )
        }
    }
}

@Composable
fun BackButton(onBackClick: () -> Unit) {
    IconButton(
        onClick = { onBackClick() },
        modifier =
        Modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .semantics { contentDescription = "Search" },
    ) {
        Icon(
            imageVector = PqIcons.Search,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
        )
    }
}

@Composable
fun SwitchLayoutButton(
    layoutType: LayoutType,
    onClick: () -> Unit,
) {
    val showPopup = remember { mutableStateOf(false) }

    if (showPopup.value) {
        LayoutSwitchWarningDialog(
            onDismiss = {
                showPopup.value = false
                onClick()
            },
        )
    }

    IconButton(
        onClick = {
            showPopup.value = true
        },
        modifier =
        Modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .semantics { contentDescription = "Switch Layout" },
    ) {
        Icon(
            imageVector = if (layoutType == LayoutType.LIST) PqIcons.GridView else PqIcons.ListView,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
        )
    }
}

@Composable
fun PhotoLibraryListScreen(
    photoSearchQuery: String = "",
    lazyPagingItems: LazyPagingItems<ImageDetail>,
    isPreview: Boolean = false,
    withTopSpacer: Boolean = true,
    withBottomSpacer: Boolean = true,
) {
    Surface {
        Box(modifier = Modifier.fillMaxSize()) {
            Column {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(lazyPagingItems.itemCount) { index ->
                        lazyPagingItems[index]?.let {
                            ImageDetailItem(
                                layoutType = LayoutType.LIST,
                                imageDetail = it,
                                isPreview = isPreview,
                            )
                        }
                    }
                }
                PagingStateHandling(lazyPagingItems)
                if (withBottomSpacer) {
                    Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
                }
            }
        }
    }
    TrackScreenViewEvent(screenName = "PhotoLibraryList, $photoSearchQuery")
}

@Composable
fun PhotoLibraryGridScreen(
    photoSearchQuery: String = "",
    lazyPagingItems: LazyPagingItems<ImageDetail>,
    withTopSpacer: Boolean = true,
    withBottomSpacer: Boolean = true,
    isPreview: Boolean = false,
) {
    Surface {
        Box(modifier = Modifier.fillMaxSize()) {
            Column {
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Adaptive(150.dp),
                    modifier = Modifier.weight(1f),
                    state = rememberLazyStaggeredGridState(),
                    contentPadding = PaddingValues(0.dp),
                    horizontalArrangement = Arrangement.spacedBy(0.dp),
                    verticalItemSpacing = 0.dp,
                    flingBehavior = ScrollableDefaults.flingBehavior(),
                ) {
                    items(lazyPagingItems.itemCount) { index ->
                        lazyPagingItems[index]?.let {
                            ImageDetailItem(
                                layoutType = LayoutType.GRID,
                                imageDetail = it,
                                isPreview = isPreview,
                            )
                        }
                    }
                }

                PagingStateHandling(lazyPagingItems)
                if (withBottomSpacer) {
                    Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
                }
            }
        }
    }
    TrackScreenViewEvent(screenName = "PhotoLibraryGrid, $photoSearchQuery")
}

@Composable
fun ImageDetailItem(
    layoutType: LayoutType,
    imageDetail: ImageDetail,
    isPreview: Boolean,
) {
    val imageAspectRatio = imageDetail.aspectRatio

    if (isPreview) {
        PreviewImageDetailItem(imageAspectRatio = imageAspectRatio)
        return
    }

    SubcomposeAsyncImage(
        model = imageDetail.webformatURL,
        contentDescription = null,
        modifier =
        Modifier
            .fillMaxWidth()
            .aspectRatio(imageAspectRatio),
    ) {
        when (painter.state) {
            is AsyncImagePainter.State.Loading, is AsyncImagePainter.State.Error -> {
                Box(
                    modifier =
                    Modifier
                        .fillMaxWidth()
                        .aspectRatio(imageAspectRatio),
                    contentAlignment = Alignment.Center,
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.feature_photo_placeholder_image),
                        contentDescription = null,
                        modifier =
                        if (layoutType == LayoutType.LIST) {
                            Modifier.size(300.dp)
                        } else {
                            Modifier.size(120.dp)
                        },
                    )
                }
            }

            else -> {
                SubcomposeAsyncImageContent()
            }
        }
    }
}

@Composable
fun PreviewImageDetailItem(imageAspectRatio: Float) {
    val resId =
        if (imageAspectRatio > 1) R.drawable.feature_photo_preview_portrait_images else R.drawable.feature_photo_preview_land_images
    val painter = coilImagePainter(resId, true)
    Box(
        modifier =
        Modifier
            .fillMaxWidth()
            .aspectRatio(imageAspectRatio),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painter,
            contentDescription = "",
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
fun PagingStateHandling(lazyPagingItems: LazyPagingItems<ImageDetail>) {
    lazyPagingItems.apply {
        when {
            loadState.refresh is LoadState.Loading -> PageLoader()
            loadState.refresh is LoadState.Error -> PageLoaderError { retry() }
            loadState.append is LoadState.Loading -> LoadingNextPageItem()
            loadState.append is LoadState.Error -> ErrorMessage { retry() }
        }
        if (itemCount == 0 &&
            loadState.append is LoadState.NotLoading &&
            loadState.append.endOfPaginationReached
        ) {
            NoDataMessage()
        }
    }
}

@Composable
fun NoDataMessage() {
    val noDataFound = stringResource(R.string.feature_photo_no_data_found)
    Box(
        modifier =
        Modifier
            .fillMaxSize()
            .semantics {
                contentDescription = noDataFound
            },
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "(´･ω･`)",
                style = MaterialTheme.typography.displayMedium,
            )
            Spacer(modifier = Modifier.height(SPACING_SMALL.dp))
            Text(
                text = noDataFound,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

@Composable
fun PageLoaderError(onClickRetry: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize(),
    ) {
        OutlinedButton(onClick = onClickRetry) {
            Text(text = stringResource(R.string.feature_photo_retry))
        }
    }
}

@Composable
fun PageLoader() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize(),
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorMessage(onClickRetry: () -> Unit) {
    Column(
        modifier =
        Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = stringResource(R.string.feature_photo_error_message), color = MaterialTheme.colorScheme.error)
        Spacer(modifier = Modifier.height(SPACING_SMALL.dp))
        OutlinedButton(onClick = onClickRetry) {
            Text(text = stringResource(R.string.feature_photo_retry))
        }
    }
}

@Composable
fun LoadingNextPageItem(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier =
        modifier
            .fillMaxWidth()
            .padding(SPACING_LARGE.dp),
    ) {
        CircularProgressIndicator(modifier = Modifier.size(30.dp))
    }
}

@Preview
@Composable
fun PhotoLibraryListScreenPreview() {
    val pagingData = PagingData.from(fakeImageDetails)
    val fakeDataFlow = MutableStateFlow(pagingData)
    val lazyPagingItems = fakeDataFlow.collectAsLazyPagingItems()

    PqTheme {
        PhotoLibraryListScreen(
            lazyPagingItems = lazyPagingItems,
            isPreview = true,
        )
    }
}

@Preview
@Composable
fun PhotoLibraryGridScreenPreview() {
    val pagingData = PagingData.from(fakeImageDetails)
    val fakeDataFlow = MutableStateFlow(pagingData)
    val lazyPagingItems = fakeDataFlow.collectAsLazyPagingItems()

    PqTheme {
        PhotoLibraryGridScreen(
            lazyPagingItems = lazyPagingItems,
            isPreview = true,
        )
    }
}
