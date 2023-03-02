package com.arsars.pixabayclient.data.source.remote.photos

import com.google.gson.annotations.SerializedName

data class ApiPhotoDto(
    @SerializedName("id") var id: Long,
    @SerializedName("tags") var tags: String? = null,
    @SerializedName("webformatURL") var previewURL: String? = null,
    @SerializedName("webformatWidth") var previewWidth: Int? = null,
    @SerializedName("webformatHeight") var previewHeight: Int? = null,
    @SerializedName("user") var user: String? = null,
    @SerializedName("largeImageURL") var largeImageURL: String? = null,
    @SerializedName("downloads") var downloads: Int? = null,
    @SerializedName("likes") var likes: Int? = null,
    @SerializedName("comments") var comments: Int? = null,
)