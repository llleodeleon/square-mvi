package com.leodeleon.square.utils

import android.content.Context
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory

@GlideModule
class GlideAppModule : AppGlideModule() {

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        val calculator = MemorySizeCalculator.Builder(context).build()
        val defaultMemoryCacheSize = calculator.memoryCacheSize.toLong()
        val defaultBitmapPoolSize = calculator.bitmapPoolSize.toLong()
        val cacheSize = 10000L

        builder.setMemoryCache(LruResourceCache(defaultMemoryCacheSize))
        builder.setBitmapPool(LruBitmapPool(defaultBitmapPoolSize))
        builder.setDiskCache(InternalCacheDiskCacheFactory(context, cacheSize))
    }
}