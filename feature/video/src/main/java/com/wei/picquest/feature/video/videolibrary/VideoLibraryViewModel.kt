package com.wei.picquest.feature.video.videolibrary

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.wei.picquest.core.base.BaseViewModel
import com.wei.picquest.core.data.model.VideoDetail
import com.wei.picquest.core.data.repository.SearchVideosRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoLibraryViewModel @Inject constructor(
    private val searchVideoRepository: SearchVideosRepository,
) : BaseViewModel<
    VideoLibraryViewAction,
    VideoLibraryViewState,
    >(VideoLibraryViewState) {

    private val _videosState: MutableStateFlow<PagingData<VideoDetail>> =
        MutableStateFlow(value = PagingData.empty())
    val videosState: MutableStateFlow<PagingData<VideoDetail>> get() = _videosState

    init {
        searchVideos("cat")
    }

    private fun searchVideos(query: String) {
        viewModelScope.launch {
            searchVideoRepository.getSearchVideo(query)
                .distinctUntilChanged()
                .cachedIn(viewModelScope)
                .collect { pagingData ->
                    _videosState.value = pagingData
                }
        }
    }

    override fun dispatch(action: VideoLibraryViewAction) {
    }
}
