package com.wei.picquest.core.network.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.wei.picquest.core.network.PqNetworkDataSource
import com.wei.picquest.core.network.model.NetworkImageDetail

class PixabayImagePagingSource(
    private val pqNetworkDataSource: PqNetworkDataSource,
    private val query: String,
) : PagingSource<Int, NetworkImageDetail>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NetworkImageDetail> {
        try {
            val currentPage = params.key ?: 1
            val response = pqNetworkDataSource.searchImages(
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

    override fun getRefreshKey(state: PagingState<Int, NetworkImageDetail>): Int? {
        return state.anchorPosition
    }
}
