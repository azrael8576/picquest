package com.wei.picquest.core.network

import com.wei.picquest.core.network.model.NetworkSearchImages
import com.wei.picquest.core.network.model.NetworkSearchVideos

/**
 * Interface representing network calls to the PicQuest backend
 */
interface PqNetworkDataSource {

    suspend fun searchImages(query: String, page: Int, perPage: Int): NetworkSearchImages

    suspend fun searchVideos(query: String, page: Int, perPage: Int): NetworkSearchVideos
}
