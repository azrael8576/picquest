package com.wei.picquest.core.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.wei.picquest.core.network.Dispatcher
import com.wei.picquest.core.network.PqDispatchers
import com.wei.picquest.core.network.PqNetworkDataSource
import com.wei.picquest.core.network.model.NetworkImageDetail
import com.wei.picquest.core.network.retrofit.PixabayPagingSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultSearchImagesRepository @Inject constructor(
    @Dispatcher(PqDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    private val pqNetworkDataSource: PqNetworkDataSource,
) : SearchImagesRepository {

    override suspend fun getSearchImages(
        query: String,
    ): Flow<PagingData<NetworkImageDetail>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 5,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = { PixabayPagingSource(pqNetworkDataSource, query) },
        ).flow
    }
}
