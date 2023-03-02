package com.arsars.pixabayclient.data.source

import com.arsars.pixabayclient.data.Stubs
import com.arsars.pixabayclient.data.source.local.photos.PhotosDao
import com.arsars.pixabayclient.data.source.local.photos.PhotosLocalDataSourceImpl
import com.arsars.pixabayclient.data.source.local.queries.QueryPhotoCrossRef
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PhotosLocalDataSourceTest {

    private val photosDao: PhotosDao = mockk()
    private val photosLocalDataSource = PhotosLocalDataSourceImpl(photosDao)

    @Test
    fun insert_anyPhotos_daoCalled() = runTest {
        coEvery {
            photosDao.insert(any())
        } returns longArrayOf()

        photosLocalDataSource.insert(Stubs.photos)

        coVerify(exactly = 1) {
            photosDao.insert(Stubs.photos)
        }
    }

    @Test
    fun insertCrossRef_anyRefs_daoCalled() = runTest {
        coEvery {
            photosDao.insertCrossRef(any())
        } just Runs

        val query = Stubs.searchQuery1
        val refs = Stubs.photos.map { QueryPhotoCrossRef(query.queryId, it.id) }

        photosLocalDataSource.insertCrossRef(refs)

        coVerify(exactly = 1) {
            photosDao.insertCrossRef(refs)
        }
    }

    @Test
    fun clearPhotosForQuery_anyId_daoCalled() = runTest {
        coEvery {
            photosDao.clearPhotosForQuery(any())
        } just Runs

        photosLocalDataSource.clearPhotosForQuery(Stubs.searchQuery1.queryId)

        coVerify(exactly = 1) {
            photosDao.clearPhotosForQuery(Stubs.searchQuery1.queryId)
        }
    }

    @Test
    fun getPhoto_anyId_daoCalled() = runTest {
        coEvery {
            photosDao.getPhoto(any())
        } returns mockk()

        val photo = Stubs.photos.first()
        photosLocalDataSource.getPhoto(photo.id)

        coVerify(exactly = 1) {
            photosDao.getPhoto(photo.id)
        }
    }

    @Test
    fun getPhotosByQueryId_anyId_daoCalled() = runTest {
        coEvery {
            photosDao.getQueryWithPhotosByQueryId(any())
        } returns mockk()

        val query = Stubs.searchQuery1
        photosLocalDataSource.getPhotosByQueryId(query.queryId)

        coVerify(exactly = 1) {
            photosDao.getQueryWithPhotosByQueryId(query.queryId)
        }
    }

}