package com.wei.picquest.core.network.fake

import JvmUnitTestFakeAssetManager
import com.wei.picquest.core.network.Dispatcher
import com.wei.picquest.core.network.PqDispatchers
import com.wei.picquest.core.network.PqNetworkDataSource
import com.wei.picquest.core.network.model.NetworkSearchImages
import com.wei.picquest.core.network.model.NetworkSearchVideos
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import org.jetbrains.annotations.VisibleForTesting

class FakePqNetworkDataSource(
    @Dispatcher(PqDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    private val networkJson: Json,
    private val assets: FakeAssetManager = JvmUnitTestFakeAssetManager,
) : PqNetworkDataSource {

    private var shouldReturnErrorForImages = false
    private var shouldReturnErrorForVideos = false

    @VisibleForTesting
    fun setReturnErrorForImages(shouldReturnError: Boolean) {
        shouldReturnErrorForImages = shouldReturnError
    }

    @VisibleForTesting
    fun setReturnErrorForVideos(shouldReturnError: Boolean) {
        shouldReturnErrorForVideos = shouldReturnError
    }

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun searchImages(query: String, page: Int, perPage: Int): NetworkSearchImages =
        withContext(ioDispatcher) {
            if (shouldReturnErrorForImages) {
                throw Exception("Fake exception for images")
            }
            when (page) {
                0 -> assets.open(IMAGES_PAGE1_ASSET).use(networkJson::decodeFromStream)
                1 -> assets.open(IMAGES_PAGE2_ASSET).use(networkJson::decodeFromStream)
                else -> {
                    assets.open(IMAGES_PAGE_END_ASSET).use(networkJson::decodeFromStream)
                }
            }
        }

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun searchVideos(query: String, page: Int, perPage: Int): NetworkSearchVideos =
        withContext(ioDispatcher) {
            if (shouldReturnErrorForVideos) {
                throw Exception("Fake exception for images")
            }
            when (page) {
                0 -> assets.open(VIDEOS_PAGE1_ASSET).use(networkJson::decodeFromStream)
                else -> {
                    assets.open(VIDEOS_PAGE_END_ASSET).use(networkJson::decodeFromStream)
                }
            }
        }

    companion object {
        private const val IMAGES_PAGE1_ASSET = "images_page1.json"
        private const val IMAGES_PAGE2_ASSET = "images_page2.json"
        private const val IMAGES_PAGE_END_ASSET = "images_page_end.json"
        private const val VIDEOS_PAGE1_ASSET = "videos_page1.json"
        private const val VIDEOS_PAGE_END_ASSET = "videos_page_end.json"
    }
}
