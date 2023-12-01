package com.wei.picquest.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Network representation of [SearchImages] when fetched from /
 */
@Serializable
data class NetworkSearchImages(
    @SerialName("total")
    val total: Int,
    @SerialName("totalHits")
    val totalHits: Int,
    @SerialName("hits")
    val hits: List<NetworkImageDetail>,
)

@Serializable
data class NetworkImageDetail(
    @SerialName("id")
    val id: Int,
    @SerialName("pageURL")
    val pageURL: String,
    @SerialName("type")
    val type: String,
    @SerialName("tags")
    val tags: String,
    @SerialName("previewURL")
    val previewURL: String,
    @SerialName("previewWidth")
    val previewWidth: Int,
    @SerialName("previewHeight")
    val previewHeight: Int,
    @SerialName("webformatURL")
    val webformatURL: String,
    @SerialName("webformatWidth")
    val webformatWidth: Int,
    @SerialName("webformatHeight")
    val webformatHeight: Int,
    @SerialName("largeImageURL")
    val largeImageURL: String,
    @SerialName("fullHDURL")
    val fullHDURL: String,
    @SerialName("imageURL")
    val imageURL: String,
    @SerialName("imageWidth")
    val imageWidth: Int,
    @SerialName("imageHeight")
    val imageHeight: Int,
    @SerialName("imageSize")
    val imageSize: Long,
    @SerialName("views")
    val views: Int,
    @SerialName("downloads")
    val downloads: Int,
    @SerialName("likes")
    val likes: Int,
    @SerialName("comments")
    val comments: Int,
    @SerialName("user_id")
    val userId: Int,
    @SerialName("user")
    val user: String,
    @SerialName("userImageURL")
    val userImageURL: String,
    // Add vectorURL if your account has full API access
    // @SerialName("vectorURL")
    // val vectorURL: String? = null
)
