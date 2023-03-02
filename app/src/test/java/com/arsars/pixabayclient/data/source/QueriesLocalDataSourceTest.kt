package com.arsars.pixabayclient.data.source

import com.arsars.pixabayclient.data.Stubs
import com.arsars.pixabayclient.data.source.local.queries.QueriesDao
import com.arsars.pixabayclient.data.source.local.queries.QueriesLocalDataSourceImpl
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class QueriesLocalDataSourceTest {

    private val queriesDao = mockk<QueriesDao>()
    private val queriesLocalDataSource = QueriesLocalDataSourceImpl(queriesDao)

    @Test
    fun findQuery_anyQuery_daoCalled() = runTest {
        coEvery {
            queriesDao.findQuery(any())
        } returns mockk()

        val query = Stubs.searchQuery1
        queriesLocalDataSource.findQuery(query.query)

        coVerify(exactly = 1) {
            queriesDao.findQuery(query.query)
        }
    }

    @Test
    fun insertQuery_anyQuery_daoCalled() = runTest {
        coEvery {
            queriesDao.insertQuery(any())
        } just Runs

        val query = Stubs.searchQuery1
        queriesLocalDataSource.insertQuery(query)

        coVerify(exactly = 1) {
            queriesDao.insertQuery(query)
        }
    }

    @Test
    fun clearCachedRefForQuery_anyQuery_daoCalled() = runTest {
        coEvery {
            queriesDao.clearCrossRefForQuery(any())
        } just Runs

        val query = Stubs.searchQuery1
        queriesLocalDataSource.clearCachedRefForQuery(query.queryId)

        coVerify(exactly = 1) {
            queriesDao.clearCrossRefForQuery(query.queryId)
        }
    }


}