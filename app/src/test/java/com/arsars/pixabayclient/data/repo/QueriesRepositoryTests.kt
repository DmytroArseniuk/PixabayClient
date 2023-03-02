package com.arsars.pixabayclient.data.repo

import com.arsars.pixabayclient.data.Stubs
import com.arsars.pixabayclient.data.source.local.QueriesLocalDataSource
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class QueriesRepositoryTests {

    private val queriesLocalDataSource = mockk<QueriesLocalDataSource>()
    private val queriesRepository = QueriesRepositoryImpl(queriesLocalDataSource)

    @Test
    fun findQuery_success() = runTest {
        val query = Stubs.searchQuery1

        coEvery {
            queriesLocalDataSource.findQuery(query.query)
        } returns query

        val queryFromRepository = queriesRepository.findQuery(query.query)

        coVerify(exactly = 1) {
            queriesLocalDataSource.findQuery(query.query)
        }

        assertEquals(query, queryFromRepository)
    }

    @Test
    fun findQuery_fail() = runTest {
        val query = Stubs.searchQuery1

        coEvery {
            queriesLocalDataSource.findQuery(query.query)
        } returns null

        val queryFromRepository = queriesRepository.findQuery(query.query)

        coVerify(exactly = 1) {
            queriesLocalDataSource.findQuery(query.query)
        }

        assertEquals(null, queryFromRepository)
    }

    @Test
    fun insertSearchQuery_success() = runTest {
        val query = Stubs.searchQuery1

        coEvery {
            queriesLocalDataSource.insertQuery(any())
        } just Runs

        queriesRepository.insertSearchQuery(query)

        coVerify(exactly = 1) {
            queriesLocalDataSource.insertQuery(any())
        }
    }

}