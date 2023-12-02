package com.wei.picquest.feature.photo.photosearch

import com.wei.picquest.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PhotoSearchViewModel @Inject constructor() : BaseViewModel<
    PhotoSearchViewAction,
    PhotoSearchViewState,
    >(PhotoSearchViewState) {

    override fun dispatch(action: PhotoSearchViewAction) {
        when (action) {
            is PhotoSearchViewAction.SearchPhotos -> TODO()
        }
    }
}
