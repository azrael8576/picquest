package com.wei.picquest.core.data.model

import com.wei.picquest.core.network.model.NetworkVideoDetail
import com.wei.picquest.core.network.model.NetworkVideoDetailSize
import com.wei.picquest.core.network.model.NetworkVideoStreams

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

fun NetworkVideoDetail.asExternalModel() = VideoDetail(
    id = this.id,
    pageURL = this.pageURL,
    type = this.type,
    tags = this.tags,
    duration = this.duration,
    pictureId = this.pictureId,
    videos = this.videos.asExternalModel(),
    views = this.views,
    downloads = this.downloads,
    likes = this.likes,
    comments = this.comments,
    userId = this.userId,
    user = this.user,
    userImageURL = this.userImageURL,
)

fun NetworkVideoStreams.asExternalModel() = VideoStreams(
    large = this.large.asExternalModel(),
    medium = this.medium.asExternalModel(),
    small = this.small.asExternalModel(),
    tiny = this.tiny.asExternalModel(),
)

fun NetworkVideoDetailSize.asExternalModel() = VideoDetailSize(
    url = this.url,
    width = this.width,
    height = this.height,
    size = this.size,
)
