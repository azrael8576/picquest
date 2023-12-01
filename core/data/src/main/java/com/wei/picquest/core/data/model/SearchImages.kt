package com.wei.picquest.core.data.model

import com.wei.picquest.core.network.model.NetworkSearchImages

data class SearchImages(
    val total: Int,
    val totalHits: Int,
    val images: List<ImageDetail>,
)

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
)

fun NetworkSearchImages.asExternalModel() = SearchImages(
    total = this.total,
    totalHits = this.totalHits,
    images = this.hits.map { networkImageDetail ->
        ImageDetail(
            id = networkImageDetail.id,
            pageURL = networkImageDetail.pageURL,
            type = networkImageDetail.type,
            tags = networkImageDetail.tags,
            previewURL = networkImageDetail.previewURL,
            previewWidth = networkImageDetail.previewWidth,
            previewHeight = networkImageDetail.previewHeight,
            webformatURL = networkImageDetail.webformatURL,
            webformatWidth = networkImageDetail.webformatWidth,
            webformatHeight = networkImageDetail.webformatHeight,
            largeImageURL = networkImageDetail.largeImageURL,
            imageWidth = networkImageDetail.imageWidth,
            imageHeight = networkImageDetail.imageHeight,
            imageSize = networkImageDetail.imageSize,
            views = networkImageDetail.views,
            downloads = networkImageDetail.downloads,
            likes = networkImageDetail.likes,
            comments = networkImageDetail.comments,
            userId = networkImageDetail.userId,
            user = networkImageDetail.user,
            userImageURL = networkImageDetail.userImageURL,
        )
    },
)
