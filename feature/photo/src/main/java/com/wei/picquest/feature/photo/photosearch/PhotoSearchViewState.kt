package com.wei.picquest.feature.photo.photosearch

import com.wei.picquest.core.base.Action
import com.wei.picquest.core.base.State

sealed class PhotoSearchViewAction : Action {
    data class SearchPhotos(
        val query: String,
    ) : PhotoSearchViewAction()
}

object PhotoSearchViewState : State
