package com.wei.picquest.feature.photolibrary.photolibrary

import com.wei.picquest.core.base.Action
import com.wei.picquest.core.base.State

sealed class PhotoLibraryViewAction : Action {
    data class SearchImages(
        val query: String,
    ) : PhotoLibraryViewAction()
}

data class PhotoLibraryViewState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    // 其他 UI 相關狀態
) : State
