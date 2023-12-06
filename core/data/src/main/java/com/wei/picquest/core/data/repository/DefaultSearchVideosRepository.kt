package com.wei.picquest.core.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.wei.picquest.core.data.model.VideoDetail
import com.wei.picquest.core.data.model.asExternalModel
import com.wei.picquest.core.network.PqNetworkDataSource
import com.wei.picquest.core.network.pagingsource.PixabayVideoPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DefaultSearchVideosRepository @Inject constructor(
    private val pqNetworkDataSource: PqNetworkDataSource,
) : SearchVideosRepository {

    override suspend fun getSearchVideo(query: String): Flow<PagingData<VideoDetail>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 5,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = { PixabayVideoPagingSource(pqNetworkDataSource, query) },
        ).flow.map { pagingData ->
            pagingData.map { it.asExternalModel() }
        }
    }
}
