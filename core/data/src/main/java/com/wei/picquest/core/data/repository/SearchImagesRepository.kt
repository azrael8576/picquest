package com.wei.picquest.core.data.repository

import com.wei.picquest.core.network.model.NetworkSearchImages
import kotlinx.coroutines.flow.Flow


interface SearchImagesRepository {

    suspend fun getSearchImages(query: String): Flow<NetworkSearchImages>
}
