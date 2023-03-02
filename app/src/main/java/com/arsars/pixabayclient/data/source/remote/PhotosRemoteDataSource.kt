package com.arsars.pixabayclient.data.source.remote

import com.arsars.pixabayclient.data.source.remote.photos.PhotoResponse

interface PhotosRemoteDataSource {
    suspend fun search(query: String, page: Int): PhotoResponse
}