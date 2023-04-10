package com.arsars.pixabayclient.ui.screens.photo

import com.arsars.pixabayclient.data.source.local.photos.Photo

class PhotoUIMapper {

    fun map(photo: Photo): PhotoUI {
        return PhotoUI(
            id = photo.id,
            tags = photo.tags,
            previewURL = photo.previewURL,
            previewWidth = photo.previewWidth,
            previewHeight = photo.previewHeight,
            userName = photo.userName,
            imageURL = photo.imageURL,
            downloads = photo.downloads,
            comments = photo.comments,
            likes = photo.likes
        )
    }

}