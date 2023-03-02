package com.arsars.pixabayclient.data.source.remote.photos

import com.arsars.pixabayclient.data.source.remote.photos.PhotoResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PixabayApi {
    @GET("/api/")
    suspend fun searchImages(
        @Query("key") apiKey: String,
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): PhotoResponse

}