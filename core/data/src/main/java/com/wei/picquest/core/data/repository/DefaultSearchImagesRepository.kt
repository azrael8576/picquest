package com.wei.picquest.core.data.repository

import com.wei.picquest.core.network.Dispatcher
import com.wei.picquest.core.network.PqDispatchers
import com.wei.picquest.core.network.PqNetworkDataSource
import com.wei.picquest.core.network.model.NetworkSearchImages
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.io.PrintWriter
import javax.inject.Inject


/**
 * Implementation of the [SearchImagesRepository].
 * @param ioDispatcher 用於執行 IO 相關操作的 CoroutineDispatcher。
 * @param network 數據源的網路接口。
 */
class DefaultSearchImagesRepository @Inject constructor(
    @Dispatcher(PqDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    private val network: PqNetworkDataSource,
) : SearchImagesRepository {

    /**
     * @param query。A URL encoded search term. If omitted, all images are returned. This value may not exceed 100 characters.
     * Example: "yellow+flower"
     * @return 一個 Flow，內容為 Search Images 的數據。
     */
    override suspend fun getSearchImages(
        query: String
    ): Flow<NetworkSearchImages> = withContext(ioDispatcher) {
        flow {
            emit(network.searchImages(query))
        }
    }
}