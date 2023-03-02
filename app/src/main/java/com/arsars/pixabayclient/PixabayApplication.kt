package com.arsars.pixabayclient

import android.app.Application
import coil.Coil
import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PixabayApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Coil.setImageLoader(ImageLoader.Builder(this)
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(0.3)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(cacheDir.resolve("image_cache"))
                    .maxSizePercent(0.3)
                    .build()
            }
            .respectCacheHeaders(false)
            .build()
        )
    }
}