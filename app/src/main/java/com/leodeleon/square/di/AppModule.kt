package com.leodeleon.square.di

import android.content.Context
import android.preference.PreferenceManager
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.leodeleon.square.BuildConfig
import com.leodeleon.square.state.BaseStateMachine
import com.leodeleon.square.data.GithubRepository
import com.leodeleon.square.data.GithubService
import com.leodeleon.square.data.NetworkInterceptor
import com.leodeleon.square.data.SharedRepository
import com.leodeleon.square.features.details.DetailsViewModel
import com.leodeleon.square.features.repos.ReposViewModel
import com.leodeleon.square.state.DetailsStateMachine
import com.leodeleon.square.state.ReposStateMachine
import com.leodeleon.square.utils.BASE_URL
import com.leodeleon.square.utils.SchedulerProvider
import okhttp3.Cache
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

val appModule = module {
    viewModel { ReposViewModel(get()) }
    viewModel { DetailsViewModel(get()) }
    single { ReposStateMachine(get(), get()) }
    single { DetailsStateMachine(get(), get()) }
    single { provideOkHttp(get(),get()) }
    single { provideService<GithubService>(get()) }
    single { GithubRepository(get()) }
    single { SharedRepository(get(),get()) }
    single { provideSharedPrefs(androidContext()) }
    single { RxSharedPreferences.create(get()) }
    single { NetworkInterceptor(BuildConfig.GITHUB_TOKEN) }
    single { provideCache(androidContext()) }
}

private fun provideSharedPrefs(context: Context) = PreferenceManager.getDefaultSharedPreferences(context)

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