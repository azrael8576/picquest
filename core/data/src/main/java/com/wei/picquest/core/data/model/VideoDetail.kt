package com.wei.picquest.core.data.model

import com.wei.picquest.core.model.data.VideoDetail
import com.wei.picquest.core.model.data.VideoDetailSize
import com.wei.picquest.core.model.data.VideoStreams
import com.wei.picquest.core.network.model.NetworkVideoDetail
import com.wei.picquest.core.network.model.NetworkVideoDetailSize
import com.wei.picquest.core.network.model.NetworkVideoStreams

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
