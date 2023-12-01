package com.wei.picquest.core.data.model

import com.wei.picquest.core.network.model.NetworkImageDetail

data class ImageDetail(
    val id: Int,
    val pageURL: String,
    val type: String,
    val tags: String,
    val previewURL: String,
    val previewWidth: Int,
    val previewHeight: Int,
    val webformatURL: String,
    val webformatWidth: Int,
    val webformatHeight: Int,
    val largeImageURL: String,
    val imageWidth: Int,
    val imageHeight: Int,
    val imageSize: Long,
    val views: Int,
    val downloads: Int,
    val likes: Int,
    val comments: Int,
    val userId: Int,
    val user: String,
    val userImageURL: String,
) {
    val aspectRatio get() = imageWidth.toFloat() / imageHeight.toFloat()
}

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
