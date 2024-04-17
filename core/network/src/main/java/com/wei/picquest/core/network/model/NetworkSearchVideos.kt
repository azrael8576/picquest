package com.wei.picquest.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Network representation of [NetworkSearchVideos] when fetched from /videos/
 */
@Serializable
data class NetworkSearchVideos(
    @SerialName("total")
    val total: Int,
    @SerialName("totalHits")
    val totalHits: Int,
    @SerialName("hits")
    val hits: List<NetworkVideoDetail>,
)

@Serializable
data class NetworkVideoDetail(
    @SerialName("id")
    val id: Int,
    @SerialName("pageURL")
    val pageURL: String,
    @SerialName("type")
    val type: String,
    @SerialName("tags")
    val tags: String,
    @SerialName("duration")
    val duration: Int,
    @SerialName("videos")
    val videos: NetworkVideoStreams,
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
)

@Serializable
data class NetworkVideoStreams(
    @SerialName("large")
    val large: NetworkVideoDetailSize,
    @SerialName("medium")
    val medium: NetworkVideoDetailSize,
    @SerialName("small")
    val small: NetworkVideoDetailSize,
    @SerialName("tiny")
    val tiny: NetworkVideoDetailSize,
)

@Serializable
data class NetworkVideoDetailSize(
    @SerialName("url")
    val url: String,
    @SerialName("width")
    val width: Int,
    @SerialName("height")
    val height: Int,
    @SerialName("size")
    val size: Long,
    @SerialName("thumbnail")
    val thumbnail: String,
)