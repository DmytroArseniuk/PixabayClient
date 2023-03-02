package com.arsars.pixabayclient.data.repo

import com.arsars.pixabayclient.data.source.local.queries.SearchQuery

interface QueriesRepository {
    suspend fun findQuery(query: String): SearchQuery?
    suspend fun insertSearchQuery(searchQuery: SearchQuery)

}
