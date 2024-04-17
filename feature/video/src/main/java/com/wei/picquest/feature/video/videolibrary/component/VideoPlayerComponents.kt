package com.wei.picquest.feature.video.videolibrary.component

import android.content.Context
import android.graphics.Rect
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.wei.picquest.core.model.data.VideoDetail
import com.wei.picquest.core.pip.enterPictureInPicture
import com.wei.picquest.core.pip.updatedPipParams

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(
    videoDetail: VideoDetail,
    exoPlayer: ExoPlayer?,
    isPlayerReady: MutableState<Boolean>,
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

        Box(
            Modifier.background(MaterialTheme.colorScheme.background),
        ) {
            if (isCurrentPage) {
                exoPlayer?.let {
                    PlayerViewContainer(
                        context = context,
                        videoDetail = videoDetail,
                        exoPlayer = it,
                    )
                }

                PiPButtonLayout(onPipClick = {
                    enterPictureInPicture(
                        context = context,
                    )
                })
            }

            if (!isPlayerReady.value || !isCurrentPage) {
                val previewUrl = videoDetail.videos.tiny.thumbnail
                LoadingView(previewUrl = previewUrl)
            }
        }
    }
}

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun PlayerViewContainer(
    context: Context,
    videoDetail: VideoDetail,
    exoPlayer: ExoPlayer,
) {
    val playerViewBounds = remember { mutableStateOf<Rect?>(null) }

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

    playerViewBounds.value?.let { calculateAndSetPiPParams(context, it, videoDetail) }
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
