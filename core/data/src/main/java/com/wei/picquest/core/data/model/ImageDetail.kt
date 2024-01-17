package com.wei.picquest.core.data.model

import com.wei.picquest.core.model.data.ImageDetail
import com.wei.picquest.core.network.model.NetworkImageDetail

fun NetworkImageDetail.asExternalModel() = ImageDetail(
    id = this.id,
    pageURL = this.pageURL,
    type = this.type,
    tags = this.tags,
    previewURL = this.previewURL,
    previewWidth = this.previewWidth,
    previewHeight = this.previewHeight,
    webformatURL = this.webformatURL,
    webformatWidth = this.webformatWidth,
    webformatHeight = this.webformatHeight,
    largeImageURL = this.largeImageURL,
    imageWidth = this.imageWidth,
    imageHeight = this.imageHeight,
    imageSize = this.imageSize,
    views = this.views,
    downloads = this.downloads,
    likes = this.likes,
    comments = this.comments,
    userId = this.userId,
    user = this.user,
    userImageURL = this.userImageURL,
)
