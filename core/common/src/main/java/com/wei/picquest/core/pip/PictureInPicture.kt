package com.wei.picquest.core.pip

import android.app.Activity
import android.app.PictureInPictureParams
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.graphics.Rect
import android.os.Build
import android.util.Rational

fun updatedPipParams(
    context: Context,
    rect: Rect,
) {
    if (!context.isPictureInPictureSupported) return

    val aspect = Rational(rect.width(), rect.height())
    val paramsBuilder = PictureInPictureParams.Builder()

    if (aspect.toFloat() in 0.418410..2.390000) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            paramsBuilder.setAspectRatio(aspect)
            paramsBuilder.setSourceRectHint(rect)
        }
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        paramsBuilder.setSeamlessResizeEnabled(true)
    }
    context.findActivity().setPictureInPictureParams(paramsBuilder.build())
}

@Suppress("DEPRECATION")
fun enterPictureInPicture(
    context: Context,
) {
    context.findActivity().enterPictureInPictureMode()
}

val Context.isPictureInPictureSupported: Boolean
    get() {
        return packageManager.hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE) &&
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
    }

val Context.isInPictureInPictureMode: Boolean
    get() {
        val currentActivity = findActivity()
        return currentActivity.isInPictureInPictureMode
    }

internal fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    throw IllegalStateException("Activity not found. Unknown error.")
}
