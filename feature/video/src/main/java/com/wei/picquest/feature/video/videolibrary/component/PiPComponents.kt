package com.wei.picquest.feature.video.videolibrary.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.wei.picquest.core.designsystem.icon.PqIcons
import com.wei.picquest.core.designsystem.theme.SPACING_MEDIUM

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
