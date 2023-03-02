package com.arsars.pixabayclient.di

import com.arsars.pixabayclient.data.repo.PhotosRepository
import com.arsars.pixabayclient.data.repo.PhotosRepositoryImpl
import com.arsars.pixabayclient.data.repo.QueriesRepository
import com.arsars.pixabayclient.data.repo.QueriesRepositoryImpl
import com.arsars.pixabayclient.data.source.local.PagingLocalDataSource
import com.arsars.pixabayclient.data.source.local.PhotosLocalDataSource
import com.arsars.pixabayclient.data.source.local.QueriesLocalDataSource
import com.arsars.pixabayclient.data.source.local.paging.PagingLocalDataSourceImpl
import com.arsars.pixabayclient.data.source.local.photos.PhotosLocalDataSourceImpl
import com.arsars.pixabayclient.data.source.local.queries.QueriesLocalDataSourceImpl
import com.arsars.pixabayclient.data.source.remote.PhotosRemoteDataSource
import com.arsars.pixabayclient.data.source.remote.photos.PhotosRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface BindsModule {
    @Binds
    fun providePhotosRemoteDataSource(clazz: PhotosRemoteDataSourceImpl): PhotosRemoteDataSource

    @Binds
    fun providePagingLocalDataSource(clazz: PagingLocalDataSourceImpl): PagingLocalDataSource

    @Binds
    fun providePhotosLocalDataSource(clazz: PhotosLocalDataSourceImpl): PhotosLocalDataSource

    @Binds
    fun provideQueriesLocalDataSource(clazz: QueriesLocalDataSourceImpl): QueriesLocalDataSource

    @Binds
    fun providePhotosRepository(clazz: PhotosRepositoryImpl): PhotosRepository

    @Binds
    fun provideQueriesRepository(clazz: QueriesRepositoryImpl): QueriesRepository
}