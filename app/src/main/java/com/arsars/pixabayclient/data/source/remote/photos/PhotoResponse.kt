package com.arsars.pixabayclient.data.source.remote.photos

import com.google.gson.annotations.SerializedName

data class PhotoResponse(
    @SerializedName("total") var total: Int,
    @SerializedName("totalHits") var totalHits: Int,
    @SerializedName("hits") var hits: ArrayList<ApiPhotoDto> = arrayListOf()
)