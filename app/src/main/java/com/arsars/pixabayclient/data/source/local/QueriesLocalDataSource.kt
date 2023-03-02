package com.arsars.pixabayclient.data.source.local

import com.arsars.pixabayclient.data.source.local.queries.SearchQuery

interface QueriesLocalDataSource {
    suspend fun findQuery(query: String): SearchQuery?

    suspend fun insertQuery(searchQuery: SearchQuery)

    suspend fun clearCachedRefForQuery(queryId: String)
}