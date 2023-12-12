package com.wei.picquest.feature.video.videolibrary

import android.app.PictureInPictureParams
import android.content.Context
import android.graphics.Rect
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
import androidx.compose.foundation.pager.PagerState
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
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.wei.picquest.core.data.model.VideoDetail
import com.wei.picquest.core.data.model.VideoDetailSize
import com.wei.picquest.core.data.model.VideoStreams
import com.wei.picquest.core.designsystem.component.FunctionalityNotAvailablePopup
import com.wei.picquest.core.designsystem.icon.PqIcons
import com.wei.picquest.core.designsystem.theme.SPACING_MEDIUM
import com.wei.picquest.core.designsystem.theme.SPACING_SMALL
import com.wei.picquest.core.pip.enterPictureInPicture
import com.wei.picquest.core.pip.isInPictureInPictureMode
import com.wei.picquest.core.pip.updatedPipParams
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
@ExperimentalFoundationApi
@Composable
internal fun VideoLibraryRoute(
    navController: NavController,
    viewModel: VideoLibraryViewModel = hiltViewModel(),
) {
    val lazyPagingItems = viewModel.videosState.collectAsLazyPagingItems()
    val context = LocalContext.current
    val isInPictureInPicture = context.isInPictureInPictureMode

    val pipParams = remember { mutableStateOf<PictureInPictureParams?>(null) }
    val exoPlayer = remember { ExoPlayer.Builder(context).build() }
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f,
        pageCount = { lazyPagingItems.itemCount },
    )

    if (isInPictureInPicture) {
        DefaultPictureInPictureContent(
            exoPlayer,
            testLandVideoDetail,
            pipParams = pipParams,
            context = LocalContext.current,
        )
    } else {
        Surface(modifier = Modifier.fillMaxSize()) {
            Box {
                VideoLibraryScreen(
                    exoPlayer = exoPlayer,
                    context = context,
                    lazyPagingItems = lazyPagingItems,
                    pagerState = pagerState,
                    pipParams = pipParams,
                )

                TopBarActions(
                    onBackClick = navController::popBackStack,
                    onPipClick = {
                        handlePipClick(lazyPagingItems, pagerState, context, pipParams.value)
                    },
                )
            }
        }
    }
}

/**
 * Renders the default PiP content, using the video state that's provided.
 */
@Composable
internal fun DefaultPictureInPictureContent(
    exoPlayer: ExoPlayer,
    videoDetail: VideoDetail,
    pipParams: MutableState<PictureInPictureParams?>,
    context: Context,
) {
    VideoPlayer(
        exoPlayer = exoPlayer,
        isInPictureInPicture = true,
        videoDetail = videoDetail,
        onPipParamsChanged = { rect ->
            val params = updatedPipParams(
                context = context,
                width = rect.width(),
                height = rect.height(),
                rect = rect,
            )
            pipParams.value = params
        },
    )
}

@OptIn(ExperimentalFoundationApi::class)
fun handlePipClick(
    lazyPagingItems: LazyPagingItems<VideoDetail>,
    pagerState: PagerState,
    context: Context,
    pipParams: PictureInPictureParams?,
) {
//    lazyPagingItems[pagerState.currentPage]?.let { currentVideoDetail ->
//        enterPictureInPicture(context, currentVideoDetail)
//    }
    enterPictureInPicture(
        context = context,
        params = pipParams,
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun VideoLibraryScreen(
    exoPlayer: ExoPlayer,
    context: Context,
    isInPictureInPicture: Boolean = false,
    lazyPagingItems: LazyPagingItems<VideoDetail>,
    pagerState: PagerState,
    pipParams: MutableState<PictureInPictureParams?>,
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
            // TODO Wei
            val videoDetail = testLandVideoDetail

            videoDetail?.let {
                VideoPlayer(
                    exoPlayer = exoPlayer,
                    videoDetail = videoDetail,
                    onPipParamsChanged = { rect ->
                        val params = updatedPipParams(
                            context = context,
                            width = rect.width(),
                            height = rect.height(),
                            rect = rect,
                        )
                        pipParams.value = params
                    },
                )
            }
//            VerticalPager(
//                state = pagerState,
//                modifier = Modifier.weight(1f),
//            ) { page ->
//                val videoDetail = lazyPagingItems[page]
//
//                videoDetail?.let {
//                    VideoPlayer(
//                        exoPlayer = exoPlayer,
//                        videoDetail = videoDetail,
//                        onPipParamsChanged = { rect ->
//                            val params = updatedPipParams(
//                                context = context,
//                                width = rect.width(),
//                                height = rect.height(),
//                                rect = rect,
//                            )
//                            pipParams.value = params
//                        },
//                    )
//                }
//            }

//            PagingStateHandling(lazyPagingItems)
        }
    }
}

@Composable
fun TopBarActions(
    onBackClick: () -> Unit,
    onPipClick: () -> Unit,
) {
    Column {
        Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
        Row(modifier = Modifier.padding(SPACING_MEDIUM.dp)) {
            BackButton(onBackClick = onBackClick)
            Spacer(modifier = Modifier.weight(1f))
            PiPButton(onPipClick = onPipClick)
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

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(
    exoPlayer: ExoPlayer,
    isInPictureInPicture: Boolean = false,
    videoDetail: VideoDetail,
    onPipParamsChanged: (Rect) -> Unit,
) {
    val context = LocalContext.current
    val isPlayerReady = remember { mutableStateOf(false) }
    val uri = videoDetail.videos.tiny.url.toUri()
    val previewUrl = "https://i.vimeocdn.com/video/${videoDetail.pictureId}_100x75.jpg"

    DisposableEffect(uri) {
        if (exoPlayer.currentMediaItem?.localConfiguration?.uri != uri) {
            exoPlayer.apply {
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

        onDispose {
        }
    }

//    val exoPlayer = remember(uri) {
//        ExoPlayer.Builder(context).build().apply {
//            playWhenReady = true
//            videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT
//            repeatMode = Player.REPEAT_MODE_ONE
//
//            addListener(object : Player.Listener {
//                override fun onPlaybackStateChanged(state: Int) {
//                    isPlayerReady.value = (state == Player.STATE_READY)
//                }
//            })
//
//            val dataSourceFactory: DataSource.Factory = DefaultDataSource.Factory(context)
//            val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
//                .createMediaSource(MediaItem.fromUri(uri))
//
//            setMediaSource(mediaSource)
//            prepare()
//        }
//    }
//
//    DisposableEffect(uri) {
//        onDispose {
//            exoPlayer.release()
//        }
//    }

    Box {
        val videoDetailWidth = videoDetail.videos.tiny.width
        val videoDetailHeight = videoDetail.videos.tiny.height

        AndroidView(
            factory = {
                PlayerView(it).apply {
                    useController = false
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                    player = exoPlayer
                    alpha = if (isPlayerReady.value) 0f else 1f
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .onGloballyPositioned { coordinates ->
                    val viewBounds = coordinates.boundsInWindow()
                    val playerViewWidth = viewBounds.width
                    val playerViewHeight = viewBounds.height

                    val videoAspectRatio = videoDetailWidth.toFloat() / videoDetailHeight.toFloat()
                    val viewAspectRatio = playerViewWidth.toFloat() / playerViewHeight.toFloat()

                    val finalWidth: Float
                    val finalHeight: Float
                    if (videoAspectRatio > viewAspectRatio) {
                        finalWidth = playerViewWidth.toFloat()
                        finalHeight = finalWidth / videoAspectRatio
                    } else {
                        finalHeight = playerViewHeight.toFloat()
                        finalWidth = finalHeight * videoAspectRatio
                    }

                    val offsetX = (playerViewWidth - finalWidth) / 2
                    val offsetY = (playerViewHeight - finalHeight) / 2

                    val rect = Rect(
                        (viewBounds.left + offsetX).toInt(),
                        (viewBounds.top + offsetY).toInt(),
                        (viewBounds.left + offsetX + finalWidth).toInt(),
                        (viewBounds.top + offsetY + finalHeight).toInt(),
                    )

                    onPipParamsChanged(rect)
                },
        )

//        if (!isPlayerReady.value && !isInPictureInPicture) {
//            LoadingView(previewUrl = previewUrl)
//        }
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
                modifier = Modifier.fillMaxWidth(),
            )
            CircularProgressIndicator(modifier = Modifier.size(30.dp))
        }
    }
}

val testVideoDetail = VideoDetail(
    id = 2879,
    pageURL = "https://pixabay.com/videos/id-2879/",
    type = "film",
    tags = "cat, pet, feline",
    duration = 11,
    pictureId = "567410872-03e024e6a79627e5739c1055ee37f724a2dd92cce453b85fab4002a340fd0326-d",
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
            url = "https://cdn.pixabay.com/vimeo/174173800/cat-3740.mp4?width=640&hash=7ed73fe4cb521c03cde94829347c2db95b51d080",
            width = 640,
            height = 360,
            size = 704905,
        ),
    ),
    views = 170506,
    downloads = 92366,
    likes = 574,
    comments = 172,
    userId = 2012561,
    user = "gustavo_belemmi",
    userImageURL = "https://cdn.pixabay.com/user/2015/11/28/05-59-27-448_250x250.png",
)

val testLandVideoDetail = VideoDetail(
    id = 191441,
    pageURL = "https://pixabay.com/videos/id-191441/",
    type = "film",
    tags = "leaves, lantern, light",
    duration = 18,
    pictureId = "1762012799-ffb602af2dcf6dd9649ae5b0e3fed271f2494710fca24791991309515a50384b-d",
    videos = VideoStreams(
        large = VideoDetailSize(
            url = "https://cdn.pixabay.com/vimeo/890098915/leaves-191441.mp4?width=1080&hash=96d95c0c92a7e3dcdaf9f838ffd2232dabacc983",
            width = 1080,
            height = 1920,
            size = 8670449,
        ),
        medium = VideoDetailSize(
            url = "https://cdn.pixabay.com/vimeo/890098915/leaves-191441.mp4?width=720&hash=dfafdce008387e10abc256d1001ecda706498d5a",
            width = 720,
            height = 1280,
            size = 5120638,
        ),
        small = VideoDetailSize(
            url = "https://cdn.pixabay.com/vimeo/890098915/leaves-191441.mp4?width=540&hash=8cd1b14eaf800652139a33d08b9d45fb64162e32",
            width = 540,
            height = 960,
            size = 3093905,
        ),
        tiny = VideoDetailSize(
            url = "https://cdn.pixabay.com/vimeo/890098915/leaves-191441.mp4?width=360&hash=2bf67c7317888b989a38ad833517003a6021d285",
            width = 360,
            height = 640,
            size = 2046623,
        ),
    ),
    views = 10173,
    downloads = 3918,
    likes = 82,
    comments = 0,
    userId = 10327513,
    user = "NickyPe",
    userImageURL = "https://cdn.pixabay.com/user/2023/09/25/05-54-14-791_250x250.jpg",
)
