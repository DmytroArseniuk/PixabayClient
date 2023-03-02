package com.arsars.pixabayclient.data.source.local

import com.arsars.pixabayclient.data.source.local.paging.PagingKey

interface PagingLocalDataSource {
    suspend fun insert(keys: List<PagingKey>)

    suspend fun getCurrentPage(photoId: Long, queryId: String): PagingKey?

    suspend fun clearPagingForQuery(queryId: String)
}