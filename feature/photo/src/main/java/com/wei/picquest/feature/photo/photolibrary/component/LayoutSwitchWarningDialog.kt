package com.wei.picquest.feature.photo.photolibrary.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.wei.picquest.feature.photo.R

@Composable
fun LayoutSwitchWarningDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        text = {
            val warningText = stringResource(R.string.feature_photo_layout_switch_warning)

            Text(
                text = warningText,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.semantics { contentDescription = warningText },
            )
        },
        confirmButton = {
            val buttonText = stringResource(R.string.feature_photo_understand)

            TextButton(onClick = onDismiss) {
                Text(
                    text = buttonText,
                    modifier = Modifier.semantics { contentDescription = buttonText },
                )
            }
        },
    )
}
