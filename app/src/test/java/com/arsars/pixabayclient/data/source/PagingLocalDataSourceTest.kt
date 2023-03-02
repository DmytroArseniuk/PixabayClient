package com.arsars.pixabayclient.data.source

import com.arsars.pixabayclient.data.Stubs
import com.arsars.pixabayclient.data.source.local.paging.PagingKey
import com.arsars.pixabayclient.data.source.local.paging.PagingKeysDao
import com.arsars.pixabayclient.data.source.local.paging.PagingLocalDataSourceImpl
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PagingLocalDataSourceTest {

    private val pagingKeysDao = mockk<PagingKeysDao>()
    private val pagingLocalDataSource = PagingLocalDataSourceImpl(pagingKeysDao)

    @Test
    fun insert_anyList_daoCalled() = runTest {
        coEvery {
            pagingKeysDao.insert(any())
        } just Runs

        val keys = listOf(PagingKey(1, "queryId", 3, 1))

        pagingLocalDataSource.insert(keys)

        coVerify {
            pagingKeysDao.insert(keys)
        }
    }

    @Test
    fun getCurrentPage_anyIds_daoCalled() = runTest {
        coEvery {
            pagingKeysDao.getCurrentPage(any(), any())
        } returns mockk()

        val pagingKey = PagingKey(1, "queryId", 3, 1)

        pagingLocalDataSource.getCurrentPage(pagingKey.photoId, pagingKey.queryId)

        coVerify {
            pagingKeysDao.getCurrentPage(pagingKey.photoId, pagingKey.queryId)
        }
    }

    @Test
    fun clearPagingForQuery_anyId_daoCalled() = runTest {
        coEvery {
            pagingKeysDao.clearPagingKeysForQuery(any())
        } just Runs

        val query = Stubs.searchQuery1

        pagingLocalDataSource.clearPagingForQuery(query.queryId)

        coVerify {
            pagingKeysDao.clearPagingKeysForQuery(query.queryId)
        }
    }

}