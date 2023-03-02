package com.arsars.pixabayclient.data

import com.arsars.pixabayclient.data.source.local.photos.Photo
import com.arsars.pixabayclient.data.source.local.queries.SearchQuery
import com.arsars.pixabayclient.data.source.remote.photos.ApiPhotoDto
import com.arsars.pixabayclient.ui.screens.photo.details.PhotosSearchViewModel.Companion.DEFAULT_QUERY

object Stubs {
    val searchQuery1 = SearchQuery(query = "Test query1")
    val defaultQuery = SearchQuery(query = DEFAULT_QUERY)
    val photos = (1..10).map { photoIndex ->
        Photo(
            photoId = photoIndex.toLong(),
            tags = (1..3).map { "tag$it" },
            previewURL = "url$photoIndex",
            previewWidth = 0,
            previewHeight = 0,
            userName = "userName$photoIndex",
            imageURL = "imageUrl$photoIndex",
            downloads = 10,
            likes = 20,
            comments = 30
        )
    }

    val apiPhotosDto = (1..10).map { photoIndex ->
        ApiPhotoDto(
            id = photoIndex.toLong(),
            tags = (1..3).map { "tag$it" }.joinToString { it },
            previewURL = "url$photoIndex",
            previewWidth = 0,
            previewHeight = 0,
            user = "userName$photoIndex",
            largeImageURL = "imageUrl$photoIndex",
            downloads = 10,
            likes = 20,
            comments = 30
        )
    }

}