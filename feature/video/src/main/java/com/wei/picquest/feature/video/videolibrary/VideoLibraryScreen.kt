package com.wei.picquest.feature.video.videolibrary

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.wei.picquest.core.designsystem.component.ThemePreviews
import com.wei.picquest.core.designsystem.theme.PqTheme
import com.wei.picquest.core.designsystem.ui.TrackScreenViewEvent
import com.wei.picquest.core.model.data.VideoDetail
import com.wei.picquest.core.model.data.VideoDetailSize
import com.wei.picquest.core.model.data.VideoStreams
import com.wei.picquest.core.pip.isInPictureInPictureMode
import com.wei.picquest.feature.video.videolibrary.component.NoDataMessage
import com.wei.picquest.feature.video.videolibrary.component.PageLoader
import com.wei.picquest.feature.video.videolibrary.component.PageLoaderError
import com.wei.picquest.feature.video.videolibrary.component.TopBarActions
import com.wei.picquest.feature.video.videolibrary.component.VideoPager
import com.wei.picquest.feature.video.videolibrary.lifecycle.MediaPiPLifecycle
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
@ExperimentalFoundationApi
@Composable
internal fun VideoLibraryRoute(
    navController: NavController,
    viewModel: VideoLibraryViewModel = hiltViewModel(),
) {
    val videoSearchQuery = viewModel.videoSearchQuery
    val lazyPagingItems = viewModel.videosState.collectAsLazyPagingItems()
    val isInPiPMode = LocalContext.current.isInPictureInPictureMode

    val countState = rememberUpdatedState(newValue = lazyPagingItems.itemCount)
    val pagerState = rememberPagerState(
        pageCount = {
            countState.value
        },
    )
    val isPlayerReady = remember { mutableStateOf(false) }
    val exoPlayer = rememberExoPlayerInstance(isPlayerReady = isPlayerReady)

    MediaPiPLifecycle(exoPlayer = exoPlayer)

    LaunchedEffect(lazyPagingItems.loadState.refresh, pagerState.currentPage) {
        if (shouldUpdatePlayerSource(lazyPagingItems, pagerState)) {
            updateExoPlayerSource(exoPlayer, lazyPagingItems[pagerState.currentPage])
        }
    }

    if (isInPiPMode) {
        DefaultPictureInPictureContent(exoPlayer)
    } else {
        VideoLibraryScreen(
            lazyPagingItems = lazyPagingItems,
            pagerState = pagerState,
            onBackClick = navController::popBackStack,
            exoPlayer = exoPlayer,
            isPlayerReady = isPlayerReady,
        )
        TrackScreenViewEvent(screenName = "VideoLibrary, $videoSearchQuery")
    }
}

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
internal fun DefaultPictureInPictureContent(exoPlayer: ExoPlayer) {
    AndroidView(
        factory = {
            PlayerView(it).apply {
                useController = false
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                player = exoPlayer
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun VideoLibraryScreen(
    lazyPagingItems: LazyPagingItems<VideoDetail>,
    pagerState: PagerState,
    exoPlayer: ExoPlayer? = null,
    onBackClick: () -> Unit,
    withTopSpacer: Boolean = true,
    withBottomSpacer: Boolean = true,
    isPreview: Boolean = false,
    isPlayerReady: MutableState<Boolean>,
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Box {
            VideoPager(
                lazyPagingItems = lazyPagingItems,
                pagerState = pagerState,
                exoPlayer = exoPlayer,
                isPlayerReady = isPlayerReady,
                isPreview = isPreview,
            )

            PagingStateHandling(lazyPagingItems = lazyPagingItems)

            TopBarActions(onBackClick = onBackClick)
        }
    }
}

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun rememberExoPlayerInstance(
    isPlayerReady: MutableState<Boolean>,
): ExoPlayer {
    val context = LocalContext.current
    return remember {
        ExoPlayer.Builder(context).build().apply {
            playWhenReady = true
            videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT
            repeatMode = Player.REPEAT_MODE_ONE

            addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(state: Int) {
                    isPlayerReady.value = (state == Player.STATE_READY)
                }
            })
        }
    }
}

fun updateExoPlayerSource(exoPlayer: ExoPlayer, videoDetail: VideoDetail?) {
    videoDetail?.let {
        val uri = it.videos.tiny.url.toUri()
        val mediaItem = MediaItem.Builder().setUri(uri).build()
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
    }
}

@OptIn(ExperimentalFoundationApi::class)
fun shouldUpdatePlayerSource(
    lazyPagingItems: LazyPagingItems<VideoDetail>,
    pagerState: PagerState,
): Boolean {
    val loadState = lazyPagingItems.loadState.refresh
    val itemCount = lazyPagingItems.itemCount
    val currentPage = pagerState.currentPage

    return loadState is LoadState.NotLoading && itemCount > 0 && currentPage < itemCount
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

@OptIn(ExperimentalFoundationApi::class)
@ThemePreviews
@Composable
fun VideoLibraryScreenPreview() {
    val pagingData = PagingData.from(fakeVideoDetails)
    val fakeDataFlow = MutableStateFlow(pagingData)
    val lazyPagingItems = fakeDataFlow.collectAsLazyPagingItems()
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f,
        pageCount = { 1 },
    )
    val isPlayerReady = remember { mutableStateOf(false) }

    PqTheme {
        VideoLibraryScreen(
            lazyPagingItems = lazyPagingItems,
            pagerState = pagerState,
            onBackClick = {},
            withTopSpacer = false,
            withBottomSpacer = false,
            isPreview = true,
            isPlayerReady = isPlayerReady,
        )
    }
}

val fakeVideoDetails = listOf(
    VideoDetail(
        id = 0,
        pageURL = "",
        type = "",
        tags = "",
        duration = 0,
        pictureId = "",
        videos = VideoStreams(
            large = VideoDetailSize(
                url = "",
                width = 0,
                height = 0,
                size = 0,
            ),
            medium = VideoDetailSize(
                url = "",
                width = 0,
                height = 0,
                size = 0,
            ),
            small = VideoDetailSize(
                url = "",
                width = 0,
                height = 0,
                size = 0,
            ),
            tiny = VideoDetailSize(
                url = "",
                width = 0,
                height = 0,
                size = 0,
            ),
        ),
        views = 0,
        downloads = 0,
        likes = 0,
        comments = 0,
        userId = 0,
        user = "",
        userImageURL = "",
    ),
)
