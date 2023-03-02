package com.arsars.pixabayclient.data.repo

import androidx.paging.Pager
import com.arsars.pixabayclient.data.PagerFactory
import com.arsars.pixabayclient.data.Stubs
import com.arsars.pixabayclient.data.source.local.PhotosLocalDataSource
import com.arsars.pixabayclient.data.source.local.photos.Photo
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

class PhotosRepositoryTests {

    private val photosLocalDataSource: PhotosLocalDataSource = mockk()

    private val pagerFactory: PagerFactory = mockk()

    private val perPage = 10

    private val photosRepository = PhotosRepositoryImpl(
        perPage,
        pagerFactory,
        photosLocalDataSource
    )

    @Test
    fun getPhotos_byPhotoId_success() {
        every {
            photosLocalDataSource.getPhoto(any())
        } returns mockk()

        val photoId = 1L
        photosRepository.getPhoto(photoId)

        verify(exactly = 1) {
            photosLocalDataSource.getPhoto(photoId)
        }
    }

    @Test
    fun getPhotosPaged_properPagerCreation_success() {
        val query = Stubs.searchQuery1

        val pager: Pager<Int, Photo> = mockk()
        every { pager.flow } returns mockk()
        every { pagerFactory.getPager(any(), any()) } returns pager

        photosRepository.getPhotosPaged(query)

        verify(exactly = 1) {
            pagerFactory.getPager(perPage, query)
        }

    }

}