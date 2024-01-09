package com.wei.picquest.feature.video.videolibrary.lifecycle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.delay

/**
 * Register a lifecycle observer for ExoPlayer to manage its playback depending on lifecycle events.
 * This function controls the playback of ExoPlayer based on the lifecycle events of the component
 * in which it is used.
 *
 * The default behavior is defined as follows:
 *
 * - The ExoPlayer will start playback (`play()`) when the lifecycle event is `ON_RESUME`.
 * - The ExoPlayer will pause playback (`pause()`) when the lifecycle event is `ON_STOP`, which
 *   includes scenarios such as when the application goes into the background.
 *
 * This lifecycle-aware management ensures that video playback is handled efficiently, respecting
 * the application's current state and reducing resource usage when the app is not in the foreground.
 *
 * @param exoPlayer The ExoPlayer instance that will be controlled based on the lifecycle events.
 * @param pipEnteringDuration The duration (in milliseconds) to wait before changing the playback
 *                            state of the ExoPlayer after a lifecycle event occurs. This can be used
 *                            to account for transitions such as entering Picture-In-Picture mode.
 */
@Composable
internal fun MediaPiPLifecycle(
    exoPlayer: ExoPlayer,
    pipEnteringDuration: Long = 250,
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    var latestLifecycleEvent by remember { mutableStateOf(Lifecycle.Event.ON_ANY) }
    DisposableEffect(lifecycle) {
        val observer = LifecycleEventObserver { _, event ->
            latestLifecycleEvent = event
        }
        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }

    if (latestLifecycleEvent == Lifecycle.Event.ON_RESUME) {
        LaunchedEffect(latestLifecycleEvent) {
            exoPlayer.play()
        }
    }

    if (latestLifecycleEvent == Lifecycle.Event.ON_STOP) {
        LaunchedEffect(latestLifecycleEvent) {
            delay(pipEnteringDuration)
            exoPlayer.pause()
        }
    }
}
