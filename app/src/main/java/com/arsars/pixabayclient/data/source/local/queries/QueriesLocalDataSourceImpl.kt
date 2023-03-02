package com.arsars.pixabayclient.data.source.local.queries

import com.arsars.pixabayclient.data.source.local.QueriesLocalDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QueriesLocalDataSourceImpl
@Inject
constructor(
    private val queryDao: QueriesDao,
) : QueriesLocalDataSource {

    override suspend fun findQuery(query: String): SearchQuery? {
        return queryDao.findQuery(query)
    }

    override suspend fun insertQuery(searchQuery: SearchQuery) {
        queryDao.insertQuery(searchQuery)
    }

    override suspend fun clearCachedRefForQuery(queryId: String) {
        queryDao.clearCrossRefForQuery(queryId)
    }
}