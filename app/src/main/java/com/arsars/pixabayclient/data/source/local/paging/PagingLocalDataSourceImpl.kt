package com.arsars.pixabayclient.data.source.local.paging

import com.arsars.pixabayclient.data.source.local.PagingLocalDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PagingLocalDataSourceImpl
@Inject
constructor(
    private val pagingKeysDao: PagingKeysDao
) : PagingLocalDataSource {

    override suspend fun insert(keys: List<PagingKey>) {
        pagingKeysDao.insert(keys)
    }

    override suspend fun getCurrentPage(photoId: Long, queryId: String): PagingKey? {
        return pagingKeysDao.getCurrentPage(photoId = photoId, queryId = queryId)
    }

    override suspend fun clearPagingForQuery(queryId: String) {
        pagingKeysDao.clearPagingKeysForQuery(queryId)
    }
}