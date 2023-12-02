package com.wei.picquest.feature.photolibrary.photolibrary

import com.wei.picquest.core.base.Action
import com.wei.picquest.core.base.State

enum class LayoutType {
    LIST, GRID
}

sealed class PhotoLibraryViewAction : Action {
    data class SearchImages(
        val query: String,
    ) : PhotoLibraryViewAction()

    object SwitchLayoutType : PhotoLibraryViewAction()
}

data class PhotoLibraryViewState(
    val layoutType: LayoutType = LayoutType.LIST,
) : State
