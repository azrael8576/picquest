package com.wei.picquest.feature.video.videolibrary

import android.content.Context
import android.graphics.Rect
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.pager.PagerState
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
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
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.wei.picquest.core.data.model.VideoDetail
import com.wei.picquest.core.data.model.VideoDetailSize
import com.wei.picquest.core.data.model.VideoStreams
import com.wei.picquest.core.designsystem.component.ThemePreviews
import com.wei.picquest.core.designsystem.component.coilImagePainter
import com.wei.picquest.core.designsystem.icon.PqIcons
import com.wei.picquest.core.designsystem.theme.PqTheme
import com.wei.picquest.core.designsystem.theme.SPACING_MEDIUM
import com.wei.picquest.core.designsystem.theme.SPACING_SMALL
import com.wei.picquest.core.pip.enterPictureInPicture
import com.wei.picquest.core.pip.isInPictureInPictureMode
import com.wei.picquest.core.pip.updatedPipParams
import com.wei.picquest.feature.video.R
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
    val lazyPagingItems = viewModel.videosState.collectAsLazyPagingItems()
    val isInPiPMode = LocalContext.current.isInPictureInPictureMode

    val countState = rememberUpdatedState(newValue = lazyPagingItems.itemCount)
    val pagerState = rememberPagerState(
        pageCount = {
            countState.value
        },
    )

    VideoLibraryScreen(
        lazyPagingItems = lazyPagingItems,
        pagerState = pagerState,
        isInPiPMode = isInPiPMode,
        onBackClick = navController::popBackStack,
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun VideoLibraryScreen(
    lazyPagingItems: LazyPagingItems<VideoDetail>,
    pagerState: PagerState,
    isInPiPMode: Boolean = false,
    onBackClick: () -> Unit,
    withTopSpacer: Boolean = true,
    withBottomSpacer: Boolean = true,
    isPreview: Boolean = false,
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Box {
            VideoPager(
                lazyPagingItems = lazyPagingItems,
                pagerState = pagerState,
                isPreview = isPreview,
            )

            if (!isInPiPMode) {
                TopBarActions(onBackClick = onBackClick)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VideoPager(
    lazyPagingItems: LazyPagingItems<VideoDetail>,
    pagerState: PagerState,
    isPreview: Boolean,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        VerticalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
        ) { page ->
            val videoDetail = lazyPagingItems[page]

            videoDetail?.let {
                VideoPlayer(
                    videoDetail = videoDetail,
                    isCurrentPage = pagerState.currentPage == page,
                    isPreview = isPreview,
                )
            }
        }

        PagingStateHandling(lazyPagingItems = lazyPagingItems)
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
fun VideoPlayer(
    videoDetail: VideoDetail,
    isCurrentPage: Boolean,
    isPreview: Boolean,
) {
    if (isPreview) {
        Box(
            Modifier.background(MaterialTheme.colorScheme.error),
        ) {
            LoadingView(isPreview = true)
            PiPButtonLayout(
                onPipClick = {},
            )
        }
    } else {
        val context = LocalContext.current
        val isInPiPMode = context.isInPictureInPictureMode
        val isPlayerReady = remember { mutableStateOf(false) }

        Box(
            Modifier.background(MaterialTheme.colorScheme.background),
        ) {
            if (isCurrentPage) {
                PlayerViewContainer(
                    context = context,
                    videoDetail = videoDetail,
                    isPlayerReady = isPlayerReady,
                )

                if (!isInPiPMode) {
                    PiPButtonLayout(onPipClick = {
                        enterPictureInPicture(
                            context = context,
                        )
                    })
                }
            }

            if (!isPlayerReady.value || !isCurrentPage) {
                val previewUrl = "https://i.vimeocdn.com/video/${videoDetail.pictureId}_295x166.jpg"
                LoadingView(previewUrl = previewUrl)
            }
        }
    }
}

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun rememberExoPlayer(
    context: Context,
    videoDetail: VideoDetail,
    isPlayerReady: MutableState<Boolean>,
    playerViewBounds: MutableState<Rect?>,
): ExoPlayer {
    val uri = videoDetail.videos.tiny.url.toUri()
    val mediaItem = MediaItem.Builder().setUri(uri).setMediaId(videoDetail.id.toString()).build()

    return remember(videoDetail.id) {
        ExoPlayer.Builder(context).build().apply {
            playWhenReady = true
            videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT
            repeatMode = Player.REPEAT_MODE_ONE

            addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(state: Int) {
                    isPlayerReady.value = (state == Player.STATE_READY)
                    playerViewBounds.value?.let { bounds ->
                        calculateAndSetPiPParams(context, bounds, videoDetail)
                    }
                }
            })

            val dataSourceFactory: DataSource.Factory = DefaultDataSource.Factory(context)
            val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(mediaItem)

            setMediaSource(mediaSource)
            prepare()
        }
    }
}

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun PlayerViewContainer(
    context: Context,
    videoDetail: VideoDetail,
    isPlayerReady: MutableState<Boolean>,
) {
    val playerViewBounds = remember { mutableStateOf<Rect?>(null) }
    val exoPlayer = rememberExoPlayer(
        context = context,
        videoDetail = videoDetail,
        isPlayerReady = isPlayerReady,
        playerViewBounds = playerViewBounds,
    )

    DisposableEffect(videoDetail.id) {
        onDispose { exoPlayer.release() }
    }

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
            .background(MaterialTheme.colorScheme.background)
            .onGloballyPositioned { layoutCoordinates ->
                playerViewBounds.value = layoutCoordinates
                    .boundsInWindow()
                    .toAndroidGraphicsRect()
            },
    )
}

@Composable
fun PiPButtonLayout(onPipClick: () -> Unit) {
    Column {
        Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
        Row(modifier = Modifier.padding(SPACING_MEDIUM.dp)) {
            Spacer(modifier = Modifier.weight(1f))
            PiPButton(
                onPipClick = onPipClick,
            )
        }
    }
}

@Composable
fun PiPButton(
    onPipClick: () -> Unit,
) {
    IconButton(
        onClick = { onPipClick() },
        modifier = Modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .semantics { contentDescription = "PictureInPicture" },
    ) {
        Icon(
            imageVector = PqIcons.PictureInPicture,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
        )
    }
}

fun androidx.compose.ui.geometry.Rect.toAndroidGraphicsRect(): android.graphics.Rect {
    return android.graphics.Rect(
        left.toInt(),
        top.toInt(),
        right.toInt(),
        bottom.toInt(),
    )
}

fun calculateAndSetPiPParams(context: Context, viewBounds: Rect, videoDetail: VideoDetail) {
    val videoDetailWidth = videoDetail.videos.tiny.width
    val videoDetailHeight = videoDetail.videos.tiny.height
    val videoAspectRatio = videoDetailWidth.toFloat() / videoDetailHeight.toFloat()

    val finalWidth: Float
    val finalHeight: Float
    if (videoAspectRatio > viewBounds.width().toFloat() / viewBounds.height().toFloat()) {
        finalWidth = viewBounds.width().toFloat()
        finalHeight = finalWidth / videoAspectRatio
    } else {
        finalHeight = viewBounds.height().toFloat()
        finalWidth = finalHeight * videoAspectRatio
    }

    val offsetX = (viewBounds.width() - finalWidth) / 2
    val offsetY = (viewBounds.height() - finalHeight) / 2

    val rect = Rect(
        (viewBounds.left + offsetX).toInt(),
        (viewBounds.top + offsetY).toInt(),
        (viewBounds.left + offsetX + finalWidth).toInt(),
        (viewBounds.top + offsetY + finalHeight).toInt(),
    )

    updatedPipParams(context, rect)
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
private fun LoadingView(
    previewUrl: String? = "",
    isPreview: Boolean = false,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        if (isPreview) {
            val resId = R.drawable.preview_images
            val painter = coilImagePainter(resId, isPreview)
            Image(
                painter = painter,
                contentDescription = "",
                modifier = Modifier.fillMaxSize(),
            )
        } else {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(previewUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "",
                modifier = Modifier.fillMaxSize(),
            )
        }
        CircularProgressIndicator(modifier = Modifier.size(30.dp))
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

    PqTheme {
        VideoLibraryScreen(
            lazyPagingItems = lazyPagingItems,
            pagerState = pagerState,
            isInPiPMode = false,
            onBackClick = {},
            withTopSpacer = false,
            withBottomSpacer = false,
            isPreview = true,
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
