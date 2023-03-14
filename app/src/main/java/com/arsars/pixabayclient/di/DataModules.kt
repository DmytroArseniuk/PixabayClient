package com.arsars.pixabayclient.di

import android.content.Context
import androidx.room.Room
import com.arsars.pixabayclient.BuildConfig
import com.arsars.pixabayclient.data.PhotosMapper
import com.arsars.pixabayclient.data.source.local.Converters
import com.arsars.pixabayclient.data.source.local.PixabayDB
import com.arsars.pixabayclient.data.source.local.paging.PagingKeysDao
import com.arsars.pixabayclient.data.source.local.photos.PhotosDao
import com.arsars.pixabayclient.data.source.local.queries.QueriesDao
import com.arsars.pixabayclient.data.source.remote.photos.PixabayApi
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton


@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class PerPageSetting

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class RexabayApiKey

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun providesRetrofit(gson: Gson): Retrofit =
        Retrofit.Builder()
            .client(
                OkHttpClient().newBuilder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .addInterceptor(HttpLoggingInterceptor().apply {
                        setLevel(
                            HttpLoggingInterceptor.Level.BODY
                        )
                    })
                    .build()
            )
            .baseUrl("https://pixabay.com/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

    @Singleton
    @Provides
    fun providesPixabayApi(retrofit: Retrofit) = retrofit.create(PixabayApi::class.java)

    @Provides
    @RexabayApiKey
    fun providesPixabayApiKey(): String = BuildConfig.PIXABAY_KEY

    @Provides
    @PerPageSetting
    fun providesPerPageSetting(): Int = 30
}


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDB(@ApplicationContext context: Context, gson: Gson): PixabayDB {
        val roomConverter = Converters(gson)
        return Room.databaseBuilder(
            context.applicationContext,
            PixabayDB::class.java,
            "pixabay_client.db"
        )
            .addTypeConverter(roomConverter)
            .build()
    }

    @Singleton
    @Provides
    fun providePhotosDao(db: PixabayDB): PhotosDao = db.getPhotosDao()

    @Singleton
    @Provides
    fun providePagingKeysDao(db: PixabayDB): PagingKeysDao = db.getPagingKeysDao()

    @Singleton
    @Provides
    fun provideQueriesDao(db: PixabayDB): QueriesDao = db.getQueriesDao()

    @Provides
    fun providePhotosMapper() = PhotosMapper()
}