package com.wei.picquest.feature.photo.photolibrary

import com.wei.picquest.core.base.Action
import com.wei.picquest.core.base.State

enum class LayoutType {
    LIST,
    GRID,
}

sealed class PhotoLibraryViewAction : Action {

    object SwitchLayoutType : PhotoLibraryViewAction()
}

data class PhotoLibraryViewState(
    val layoutType: LayoutType = LayoutType.LIST,
) : State
