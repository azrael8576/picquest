package com.wei.picquest.core.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.wei.picquest.core.data.model.ImageDetail
import com.wei.picquest.core.data.model.asExternalModel
import com.wei.picquest.core.network.PqNetworkDataSource
import com.wei.picquest.core.network.pagingsource.PixabayImagePagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DefaultSearchImagesRepository @Inject constructor(
    private val pqNetworkDataSource: PqNetworkDataSource,
) : SearchImagesRepository {

    override suspend fun getSearchImages(query: String): Flow<PagingData<ImageDetail>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 5,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = { PixabayImagePagingSource(pqNetworkDataSource, query) },
        ).flow.map { pagingData ->
            pagingData.map { it.asExternalModel() }
        }
    }
}
