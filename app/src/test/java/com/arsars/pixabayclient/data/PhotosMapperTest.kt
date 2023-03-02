package com.arsars.pixabayclient.data

import com.arsars.pixabayclient.data.source.remote.photos.ApiPhotoDto
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

class PhotosMapperTest {

    private val mapper = PhotosMapper()

    @Test
    fun map_ApiPhotoDtoIntoPhoto_success() {
        val apiDto = ApiPhotoDto(
            id = 10,
            tags = "tag1, tag2, tag3",
            previewURL = "url",
            previewWidth = 111,
            previewHeight = 222,
            user = "userName",
            largeImageURL = "urlLarge",
            downloads = 11,
            likes = 12,
            comments = 13
        )

        val photo = mapper.map(apiDto)

        assertAll(
            "All fields mapped as expected",
            { assertEquals(photo.photoId, apiDto.id) },
            { assertEquals(photo.tags, listOf("tag1", "tag2", "tag3")) },
            { assertEquals(photo.previewURL, apiDto.previewURL) },
            { assertEquals(photo.previewWidth, apiDto.previewWidth) },
            { assertEquals(photo.previewHeight, apiDto.previewHeight) },
            { assertEquals(photo.userName, apiDto.user) },
            { assertEquals(photo.imageURL, apiDto.largeImageURL) },
            { assertEquals(photo.downloads, apiDto.downloads) },
            { assertEquals(photo.likes, apiDto.likes) },
            { assertEquals(photo.comments, apiDto.comments) },
        )
    }
}