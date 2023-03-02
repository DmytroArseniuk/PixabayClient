package com.arsars.pixabayclient.data.source.local.queries

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.arsars.pixabayclient.data.source.local.photos.Photo

data class QueryWithPhotos(
    @Embedded val query: SearchQuery,
    @Relation(
        parentColumn = "queryId",
        entityColumn = "photoId",
        associateBy = Junction(QueryPhotoCrossRef::class)
    )
    val photos: List<Photo>
)
