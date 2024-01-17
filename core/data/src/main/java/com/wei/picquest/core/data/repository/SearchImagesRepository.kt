package com.wei.picquest.core.data.repository

import androidx.paging.PagingData
import com.wei.picquest.core.model.data.ImageDetail
import kotlinx.coroutines.flow.Flow

interface SearchImagesRepository {

    suspend fun getSearchImages(query: String): Flow<PagingData<ImageDetail>>
}
