package com.arsars.pixabayclient.data.repo

import com.arsars.pixabayclient.data.source.local.QueriesLocalDataSource
import com.arsars.pixabayclient.data.source.local.queries.SearchQuery
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QueriesRepositoryImpl
@Inject
constructor(
    private val queriesLocalDataSource: QueriesLocalDataSource,
) : QueriesRepository {
    override suspend fun findQuery(query: String): SearchQuery? {
        return queriesLocalDataSource.findQuery(query)
    }

    override suspend fun insertSearchQuery(searchQuery: SearchQuery) {
        queriesLocalDataSource.insertQuery(searchQuery)
    }


}
