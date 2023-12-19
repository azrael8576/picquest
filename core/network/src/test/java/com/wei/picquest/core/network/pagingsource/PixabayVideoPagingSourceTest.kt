package com.wei.picquest.core.network.pagingsource

import JvmUnitTestFakeAssetManager
import androidx.paging.PagingSource
import com.google.common.truth.Truth.assertThat
import com.wei.picquest.core.network.fake.FakePqNetworkDataSource
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for [PixabayVideoPagingSource].
 *
 * 遵循此模型，安排、操作、斷言：
 * {Arrange}{Act}{Assert}
 */
class PixabayVideoPagingSourceTest {

    private lateinit var fakePqNetworkDataSource: FakePqNetworkDataSource
    private lateinit var pixabayVideoPagingSource: PixabayVideoPagingSource

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        fakePqNetworkDataSource = FakePqNetworkDataSource(
            ioDispatcher = testDispatcher,
            networkJson = Json { ignoreUnknownKeys = true },
            assets = JvmUnitTestFakeAssetManager,
        )
        pixabayVideoPagingSource = PixabayVideoPagingSource(fakePqNetworkDataSource, "testQuery")
    }

    @Test
    fun `load should return non-empty page for first page`() = runTest(testDispatcher) {
        // Arrange
        val firstPageKey = 0

        // Act
        val firstPageResult = pixabayVideoPagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = firstPageKey,
                loadSize = 20,
                placeholdersEnabled = false,
            ),
        )

        // Assert
        assertThat(firstPageResult).isInstanceOf(PagingSource.LoadResult.Page::class.java)
        assertThat((firstPageResult as PagingSource.LoadResult.Page).data).isNotEmpty()
    }

    @Test
    fun `load should return empty page at end of pagination`() = runTest(testDispatcher) {
        // Arrange
        val endPageKey = 1 // Assuming this is the end

        // Act
        val endPageResult = pixabayVideoPagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = endPageKey,
                loadSize = 20,
                placeholdersEnabled = false,
            ),
        )

        // Assert
        assertThat(endPageResult).isInstanceOf(PagingSource.LoadResult.Page::class.java)
        assertThat((endPageResult as PagingSource.LoadResult.Page).data).isEmpty()
    }

    @Test
    fun `load should return error when data source throws exception`() = runTest {
        // Arrange
        fakePqNetworkDataSource.setReturnErrorForVideos(true)

        // Act
        val result = pixabayVideoPagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = 0,
                loadSize = 20,
                placeholdersEnabled = false,
            ),
        )

        // Assert
        assertThat(result).isInstanceOf(PagingSource.LoadResult.Error::class.java)
    }
}
