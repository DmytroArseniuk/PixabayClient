package com.arsars.pixabayclient.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.arsars.pixabayclient.data.source.local.paging.PagingKeysDao
import com.arsars.pixabayclient.data.source.local.photos.PhotosDao
import com.arsars.pixabayclient.data.source.local.queries.QueriesDao
import com.arsars.pixabayclient.data.source.local.paging.PagingKey
import com.arsars.pixabayclient.data.source.local.photos.Photo
import com.arsars.pixabayclient.data.source.local.queries.QueryPhotoCrossRef
import com.arsars.pixabayclient.data.source.local.queries.SearchQuery

@Database(
    entities = [Photo::class, PagingKey::class, QueryPhotoCrossRef::class, SearchQuery::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class PixabayDB : RoomDatabase() {

    abstract fun getPhotosDao(): PhotosDao
    abstract fun getPagingKeysDao(): PagingKeysDao
    abstract fun getQueriesDao(): QueriesDao
}