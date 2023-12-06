package com.wei.picquest.feature.video.videolibrary

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.wei.picquest.core.data.model.VideoDetail
import com.wei.picquest.core.designsystem.component.FunctionalityNotAvailablePopup
import com.wei.picquest.core.designsystem.icon.PqIcons
import com.wei.picquest.core.designsystem.theme.SPACING_MEDIUM
import com.wei.picquest.core.designsystem.theme.SPACING_SMALL
import com.wei.picquest.feature.video.R

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
internal fun VideoLibraryRoute(
    navController: NavController,
    viewModel: VideoLibraryViewModel = hiltViewModel(),
) {
    val lazyPagingItems = viewModel.videosState.collectAsLazyPagingItems()

    Surface(modifier = Modifier.fillMaxSize()) {
        Box {
            VideoLibraryScreen(lazyPagingItems)

            TopBarActions(
                onBackClick = navController::popBackStack,
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun VideoLibraryScreen(
    lazyPagingItems: LazyPagingItems<VideoDetail>,
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
            val pagerState = rememberPagerState(
                initialPage = 0,
                initialPageOffsetFraction = 0f,
                pageCount = { lazyPagingItems.itemCount },
            )

            VerticalPager(
                state = pagerState,
                modifier = Modifier.weight(1f),
            ) { page ->
                val videoDetail = lazyPagingItems[page]
                videoDetail?.let {
                    VideoPlayer(
                        uri = it.videos.tiny.url.toUri(),
                        previewUrl = "https://i.vimeocdn.com/video/${it.pictureId}_100x75.jpg",
                    )
                }
            }

            PagingStateHandling(lazyPagingItems)
        }
    }
}

@Composable
fun TopBarActions(
    onBackClick: () -> Unit,
) {
    Column {
        Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
        Row(modifier = Modifier.padding(SPACING_MEDIUM.dp)) {
            BackButton(onBackClick = onBackClick)
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun BackButton(
    onBackClick: () -> Unit,
) {
    IconButton(
        onClick = { onBackClick() },
        modifier = Modifier
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

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(uri: Uri, previewUrl: String) {
    val context = LocalContext.current
    val isPlayerReady = remember { mutableStateOf(false) }

    val exoPlayer = remember(uri) {
        ExoPlayer.Builder(context).build().apply {
            playWhenReady = true
            videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT
            repeatMode = Player.REPEAT_MODE_ONE

            addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(state: Int) {
                    isPlayerReady.value = (state == Player.STATE_READY)
                }
            })

            val dataSourceFactory: DataSource.Factory = DefaultDataSource.Factory(context)
            val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(uri))

            setMediaSource(mediaSource)
            prepare()
        }
    }

    DisposableEffect(uri) {
        onDispose {
            exoPlayer.release()
        }
    }

    Box {
        AndroidView(
            factory = {
                PlayerView(it).apply {
                    useController = false
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                    player = exoPlayer
                    alpha = if (isPlayerReady.value) 0f else 1f
                }
            },
            modifier = Modifier.fillMaxSize(),
        )

        if (!isPlayerReady.value) {
            LoadingView(previewUrl = previewUrl)
        }
    }
}

@Composable
fun PagingStateHandling(lazyPagingItems: LazyPagingItems<VideoDetail>) {
    lazyPagingItems.apply {
        when {
            loadState.refresh is LoadState.Loading -> PageLoader()
            loadState.refresh is LoadState.Error -> PageLoaderError { retry() }
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
    val noDataFound = stringResource(R.string.no_data_found)
    Box(
        modifier = Modifier
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
            Text(text = stringResource(R.string.retry))
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
private fun LoadingView(previewUrl: String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(previewUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth(),
            )
            CircularProgressIndicator(modifier = Modifier.size(30.dp))
        }
    }
}
