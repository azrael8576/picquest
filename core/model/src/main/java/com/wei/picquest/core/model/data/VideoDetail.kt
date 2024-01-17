package com.wei.picquest.core.model.data

data class VideoDetail(
    val id: Int,
    val pageURL: String,
    val type: String,
    val tags: String,
    val duration: Int,
    val pictureId: String,
    val videos: VideoStreams,
    val views: Int,
    val downloads: Int,
    val likes: Int,
    val comments: Int,
    val userId: Int,
    val user: String,
    val userImageURL: String,
)

data class VideoStreams(
    val large: VideoDetailSize,
    val medium: VideoDetailSize,
    val small: VideoDetailSize,
    val tiny: VideoDetailSize,
)

data class VideoDetailSize(
    val url: String,
    val width: Int,
    val height: Int,
    val size: Long,
)
