package com.arsars.pixabayclient.data.source.local.photos

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "photos")
data class Photo(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    @ColumnInfo(name = "photoId")
    var photoId: Long,
    @ColumnInfo(name = "tags")
    var tags: List<String> = emptyList(),
    @ColumnInfo(name = "previewURL")
    var previewURL: String,
    @ColumnInfo(name = "previewWidth")
    var previewWidth: Int,
    @ColumnInfo(name = "previewHeight")
    var previewHeight: Int,
    @ColumnInfo(name = "userName")
    var userName: String,
    @ColumnInfo(name = "imageURL")
    var imageURL: String,
    @ColumnInfo(name = "downloads")
    var downloads: Int = 0,
    @ColumnInfo(name = "likes")
    var likes: Int = 0,
    @ColumnInfo(name = "comments")
    var comments: Int = 0
)