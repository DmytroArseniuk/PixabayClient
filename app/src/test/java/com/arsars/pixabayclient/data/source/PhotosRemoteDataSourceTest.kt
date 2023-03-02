package com.arsars.pixabayclient.data.source

import com.arsars.pixabayclient.data.source.remote.photos.PhotosRemoteDataSourceImpl
import com.arsars.pixabayclient.data.source.remote.photos.PixabayApi
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PhotosRemoteDataSourceTest {

    private val pixabayApi: PixabayApi = mockk()
    private val remoteDataSource = PhotosRemoteDataSourceImpl("", 0, pixabayApi, Dispatchers.Main)

    @Test
    fun search_anyPageAndQuery_apiCalled() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))

        coEvery {
            pixabayApi.searchImages(any(), any(), any(), any())
        } returns mockk()

        val page = 1
        val query = "query"
        remoteDataSource.search(query, page)

        coVerify(exactly = 1) {
            pixabayApi.searchImages(any(), query, page, any())
        }

    }
}