package com.arsars.pixabayclient.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class DispatcherIo

@Module
@InstallIn(SingletonComponent::class)
object CoroutinesModule {

    @Provides
    @DispatcherIo
    fun providesDispatcherIO(): CoroutineDispatcher = Dispatchers.IO
}
