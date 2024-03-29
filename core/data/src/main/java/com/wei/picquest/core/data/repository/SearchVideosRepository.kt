package com.wei.picquest.core.data.repository

import androidx.paging.PagingData
import com.wei.picquest.core.model.data.VideoDetail
import kotlinx.coroutines.flow.Flow

interface SearchVideosRepository {

    suspend fun getSearchVideo(query: String): Flow<PagingData<VideoDetail>>
}
