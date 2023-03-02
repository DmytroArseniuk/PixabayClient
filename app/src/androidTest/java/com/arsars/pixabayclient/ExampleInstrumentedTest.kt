package com.arsars.pixabayclient

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.arsars.pixabayclient.data.source.local.Converters
import com.arsars.pixabayclient.data.source.local.PixabayDB
import com.arsars.pixabayclient.data.source.local.paging.PagingKeysDao
import com.arsars.pixabayclient.data.source.local.photos.PhotosDao
import com.arsars.pixabayclient.data.source.local.queries.QueriesDao
import com.google.gson.Gson
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    private lateinit var photosDao: PhotosDao
    private lateinit var pagingKeysDao: PagingKeysDao
    private lateinit var queriesDao: QueriesDao
    private lateinit var db: PixabayDB

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, PixabayDB::class.java)
            .addTypeConverter(Converters(Gson()))
            .setTransactionExecutor(testDispatcher.asExecutor())
            .setQueryExecutor(testDispatcher.asExecutor())
            .build()
        photosDao = db.getPhotosDao()
        pagingKeysDao = db.getPagingKeysDao()
        queriesDao = db.getQueriesDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertQuery() = testScope.runTest {
        val query = Stubs.searchQuery1
        queriesDao.insertQuery(query)
        val result = queriesDao.findQuery(query.query)
        assertEquals(query, result)
    }

    @Test
    fun deleteQuery() = testScope.runTest {
        val query = Stubs.searchQuery1
        queriesDao.insertQuery(query)
        queriesDao.clearQueries()
        val result = queriesDao.findQuery(query.query)
        assertEquals(result, null)
    }
}