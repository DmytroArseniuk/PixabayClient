package com.arsars.pixabayclient.data

import com.arsars.pixabayclient.data.source.local.photos.Photo
import com.arsars.pixabayclient.data.source.remote.photos.ApiPhotoDto

class PhotosMapper {
    fun map(photoDto: ApiPhotoDto): Photo = Photo(
        photoId = photoDto.id,
        tags = photoDto.tags?.split(",")?.map { it.trim() } ?: emptyList(),
        previewURL = photoDto.previewURL ?: "",
        previewWidth = photoDto.previewWidth ?: 0,
        previewHeight = photoDto.previewHeight ?: 0,
        userName = photoDto.user ?: "",
        imageURL = photoDto.largeImageURL ?: "",
        downloads = photoDto.downloads ?: 0,
        likes = photoDto.likes ?: 0,
        comments = photoDto.comments ?: 0
    )
}