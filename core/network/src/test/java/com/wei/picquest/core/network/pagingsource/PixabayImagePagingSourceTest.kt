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
 * Unit tests for [PixabayImagePagingSource].
 *
 * 遵循此模型，安排、操作、斷言：
 * {Arrange}{Act}{Assert}
 */
class PixabayImagePagingSourceTest {

    private lateinit var fakePqNetworkDataSource: FakePqNetworkDataSource
    private lateinit var pixabayImagePagingSource: PixabayImagePagingSource

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        fakePqNetworkDataSource = FakePqNetworkDataSource(
            ioDispatcher = testDispatcher,
            networkJson = Json { ignoreUnknownKeys = true },
            assets = JvmUnitTestFakeAssetManager,
        )
        pixabayImagePagingSource = PixabayImagePagingSource(fakePqNetworkDataSource, "testQuery")
    }

    @Test
    fun `load should return non-empty page for first and second page`() = runTest(testDispatcher) {
        // Arrange
        val firstPageKey = 0
        val secondPageKey = 1

        // Act
        val firstPageResult = pixabayImagePagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = firstPageKey,
                loadSize = 20,
                placeholdersEnabled = false,
            ),
        )
        val secondPageResult = pixabayImagePagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = secondPageKey,
                loadSize = 20,
                placeholdersEnabled = false,
            ),
        )

        // Assert
        assertThat(firstPageResult).isInstanceOf(PagingSource.LoadResult.Page::class.java)
        assertThat((firstPageResult as PagingSource.LoadResult.Page).data).isNotEmpty()
        assertThat(secondPageResult).isInstanceOf(PagingSource.LoadResult.Page::class.java)
        assertThat((secondPageResult as PagingSource.LoadResult.Page).data).isNotEmpty()
    }

    @Test
    fun `load should return empty page at end of pagination`() = runTest(testDispatcher) {
        // Arrange
        val endPageKey = 2 // Assuming this is the end

        // Act
        val endPageResult = pixabayImagePagingSource.load(
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
        fakePqNetworkDataSource.setReturnErrorForImages(true)

        // Act
        val result = pixabayImagePagingSource.load(
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
