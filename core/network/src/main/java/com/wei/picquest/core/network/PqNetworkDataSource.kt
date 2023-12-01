package com.wei.picquest.core.network

import com.wei.picquest.core.network.model.NetworkSearchImages

/**
 * Interface representing network calls to the PicQuest backend
 */
interface PqNetworkDataSource {

    suspend fun searchImages(query: String): NetworkSearchImages
}
