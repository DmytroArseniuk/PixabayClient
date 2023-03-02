package com.arsars.pixabayclient.data

import android.util.Log
import androidx.paging.*
import androidx.paging.PagingSource.LoadResult.Page
import androidx.room.withTransaction
import com.arsars.pixabayclient.data.Stubs.apiPhotosDto
import com.arsars.pixabayclient.data.Stubs.photos
import com.arsars.pixabayclient.data.source.local.PagingLocalDataSource
import com.arsars.pixabayclient.data.source.local.PhotosLocalDataSource
import com.arsars.pixabayclient.data.source.local.PixabayDB
import com.arsars.pixabayclient.data.source.local.QueriesLocalDataSource
import com.arsars.pixabayclient.data.source.local.paging.PagingKey
import com.arsars.pixabayclient.data.source.local.photos.Photo
import com.arsars.pixabayclient.data.source.local.queries.SearchQuery
import com.arsars.pixabayclient.data.source.remote.PhotosRemoteDataSource
import com.arsars.pixabayclient.data.source.remote.photos.ApiPhotoDto
import com.arsars.pixabayclient.data.source.remote.photos.PhotoResponse
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import retrofit2.HttpException
import retrofit2.Response

@OptIn(ExperimentalPagingApi::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PagingMediatorTest {


    private val photosRemoteDataSource: PhotosRemoteDataSource = mockk()
    private val photosLocalDataSource: PhotosLocalDataSource = mockk()
    private val queriesLocalDataSource: QueriesLocalDataSource = mockk()
    private val pagingLocalDataSource: PagingLocalDataSource = mockk()
    private val database: PixabayDB = mockk()
    private val photosMapper: PhotosMapper = PhotosMapper()
    private val query: SearchQuery = Stubs.defaultQuery

    private val pagingMediator = PagingMediator(
        photosRemoteDataSource,
        photosLocalDataSource,
        queriesLocalDataSource,
        pagingLocalDataSource,
        database,
        photosMapper,
        query
    )

    @BeforeAll
    fun prepare() {
        mockkStatic("androidx.room.RoomDatabaseKt")

        mockkStatic(Log::class)
        every { Log.v(any(), any()) } returns 0
        every { Log.d(any(), any()) } returns 0
        every { Log.i(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0

        val transactionLambda = slot<suspend () -> Unit>()
        coEvery { database.withTransaction(capture(transactionLambda)) } coAnswers {
            transactionLambda.captured.invoke()
        }

        coEvery { photosLocalDataSource.clearPhotosForQuery(any()) } just Runs

        coEvery { queriesLocalDataSource.clearCachedRefForQuery(any()) } just Runs

        coEvery { pagingLocalDataSource.clearPagingForQuery(any()) } just Runs

        coEvery { pagingLocalDataSource.insert(any()) } just Runs

        coEvery { photosLocalDataSource.insertCrossRef(any()) } just Runs

    }

    @Test
    @OptIn(ExperimentalCoroutinesApi::class)
    fun load_refreshPhotosNotEmpty_Success() = runTest {
        coEvery {
            photosRemoteDataSource.search(any(), any())
        } returns PhotoResponse(apiPhotosDto.size, apiPhotosDto.size, ArrayList(apiPhotosDto))


        val pagingState = PagingState<Int, Photo>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )


        coEvery {
            photosLocalDataSource.insert(any())
        } returns apiPhotosDto.map { it.id }.toLongArray()


        val result = pagingMediator.load(LoadType.REFRESH, pagingState)

        assertTrue { result is RemoteMediator.MediatorResult.Success }
        assertFalse { (result as RemoteMediator.MediatorResult.Success).endOfPaginationReached }
    }

    @Test
    @OptIn(ExperimentalCoroutinesApi::class)
    fun load_refreshPhotosEmpty_Success() = runTest {
        coEvery {
            photosRemoteDataSource.search(any(), any())
        } returns PhotoResponse(apiPhotosDto.size, apiPhotosDto.size, arrayListOf())


        val pagingState = PagingState<Int, Photo>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )


        coEvery {
            photosLocalDataSource.insert(any())
        } returns longArrayOf()

        val result = pagingMediator.load(LoadType.REFRESH, pagingState)

        assertTrue { result is RemoteMediator.MediatorResult.Success }
        assertTrue { (result as RemoteMediator.MediatorResult.Success).endOfPaginationReached }
    }

    @Test
    @OptIn(ExperimentalCoroutinesApi::class)
    fun load_refreshNetworkError_Fail() = runTest {
        coEvery {
            photosRemoteDataSource.search(any(), any())
        } throws HttpException(
            Response.error<PhotoResponse>(
                400,
                "{\"key\":[\"somestuff\"]}".toResponseBody("application/json".toMediaTypeOrNull())
            )
        )

        val pagingState = PagingState<Int, Photo>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )

        coEvery {
            photosLocalDataSource.insert(any())
        } returns longArrayOf()

        val result = pagingMediator.load(LoadType.REFRESH, pagingState)

        assertTrue { result is RemoteMediator.MediatorResult.Error }
    }

    @Test
    @OptIn(ExperimentalCoroutinesApi::class)
    fun load_appendPhotosNotEmpty_Success() = runTest {
        coEvery {
            photosRemoteDataSource.search(any(), any())
        } returns PhotoResponse(apiPhotosDto.size, apiPhotosDto.size, ArrayList(apiPhotosDto))



        coEvery {
            pagingLocalDataSource.getCurrentPage(any(), any())
        } returns PagingKey(1, "", 2, null)

        val pagingState = PagingState(
            listOf(Page(photos, 0, 2)),
            null,
            PagingConfig(10),
            10
        )

        coEvery {
            photosLocalDataSource.insert(any())
        } returns longArrayOf()

        val result = pagingMediator.load(LoadType.APPEND, pagingState)

        assertTrue { result is RemoteMediator.MediatorResult.Success }
        assertFalse { (result as RemoteMediator.MediatorResult.Success).endOfPaginationReached }
    }
}