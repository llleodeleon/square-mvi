package com.leodeleon.data.repositories

import android.content.Context
import com.leodeleon.data.BuildConfig
import com.leodeleon.data.GithubService
import com.leodeleon.data.utils.BASE_URL
import com.leodeleon.data.utils.NetworkInterceptor
import com.leodeleon.data.utils.SchedulerProvider
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

class GithubAPI(context: Context) {
    private val cache = provideCache(context)
    private val interceptor = NetworkInterceptor(BuildConfig.GITHUB_TOKEN)
    private val client = provideOkHttp(interceptor,cache)

    val service: GithubService

    init {
        service = provideService(client)
    }

    private fun provideOkHttp(networkInterceptor: NetworkInterceptor, cache: Cache) : OkHttpClient {
        return OkHttpClient.Builder()
                .addNetworkInterceptor(networkInterceptor)
                .cache(cache)
                .readTimeout(20, TimeUnit.SECONDS)
                .build()
    }

    private fun provideCache(context: Context): Cache {
        val dir = File(context.cacheDir.absolutePath, "OkHttpCache")
        val size = 10L * 1024 * 1024
        return Cache(dir,size)
    }

    private inline fun <reified T> provideService(okHttp: OkHttpClient): T {
        val retrofit =
                Retrofit.Builder()
                        .client(okHttp)
                        .addConverterFactory(MoshiConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(SchedulerProvider.io()))
                        .baseUrl(BASE_URL)
                        .build()

        return retrofit.create(T::class.java)
    }
}