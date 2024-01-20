package com.wei.picquest.feature.video.videolibrary.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.media3.exoplayer.ExoPlayer
import androidx.paging.compose.LazyPagingItems
import com.wei.picquest.core.model.data.VideoDetail

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VideoPager(
    lazyPagingItems: LazyPagingItems<VideoDetail>,
    pagerState: PagerState,
    exoPlayer: ExoPlayer?,
    isPlayerReady: MutableState<Boolean>,
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
                    exoPlayer = exoPlayer,
                    isPlayerReady = isPlayerReady,
                    isCurrentPage = pagerState.currentPage == page,
                    isPreview = isPreview,
                )
            }
        }
    }
}
