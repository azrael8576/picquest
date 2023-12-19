package com.wei.picquest.core.network.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.wei.picquest.core.network.PqNetworkDataSource
import com.wei.picquest.core.network.model.NetworkVideoDetail

class PixabayVideoPagingSource(
    private val pqNetworkDataSource: PqNetworkDataSource,
    private val query: String,
) : PagingSource<Int, NetworkVideoDetail>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NetworkVideoDetail> {
        try {
            val currentPage = params.key ?: 1
            val response = pqNetworkDataSource.searchVideos(
                query = query,
                page = currentPage,
                perPage = 20,
            )

            val endOfPaginationReached = response.hits.isEmpty()

            return LoadResult.Page(
                data = response.hits,
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (endOfPaginationReached) null else currentPage + 1,
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, NetworkVideoDetail>): Int? {
        return state.anchorPosition
    }
}
