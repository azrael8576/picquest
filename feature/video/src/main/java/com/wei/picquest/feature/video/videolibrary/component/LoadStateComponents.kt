package com.wei.picquest.feature.video.videolibrary.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.wei.picquest.core.designsystem.component.coilImagePainter
import com.wei.picquest.core.designsystem.theme.SPACING_SMALL
import com.wei.picquest.feature.video.R

@Composable
internal fun LoadingView(
    previewUrl: String? = "",
    isPreview: Boolean = false,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier =
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
    ) {
        if (isPreview) {
            val resId = R.drawable.feature_video_preview_images
            val painter = coilImagePainter(resId, isPreview)
            Image(
                painter = painter,
                contentDescription = "",
                modifier = Modifier.fillMaxSize(),
            )
        } else {
            AsyncImage(
                model =
                    ImageRequest.Builder(LocalContext.current)
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

@Composable
internal fun PageLoader() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize(),
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun PageLoaderError(onClickRetry: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize(),
    ) {
        OutlinedButton(onClick = onClickRetry) {
            Text(text = stringResource(R.string.feature_video_retry))
        }
    }
}

@Composable
fun NoDataMessage() {
    val noDataFound = stringResource(R.string.feature_video_no_data_found)
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
