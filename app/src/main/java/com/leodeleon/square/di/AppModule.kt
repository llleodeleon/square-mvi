package com.leodeleon.square.di

import android.content.Context
import android.preference.PreferenceManager
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.leodeleon.data.repositories.GithubAPI
import com.leodeleon.data.repositories.GithubRepository
import com.leodeleon.data.repositories.SharedRepository
import com.leodeleon.domain.repositories.ILocalRepository
import com.leodeleon.domain.repositories.IRemoteRepository
import com.leodeleon.domain.states.DetailsStateMachine
import com.leodeleon.domain.states.ReposStateMachine
import com.leodeleon.square.features.details.DetailsViewModel
import com.leodeleon.square.features.repos.ReposViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val appModule = module {
    viewModel { ReposViewModel(get()) }
    viewModel { DetailsViewModel(get()) }
    single { ReposStateMachine(get(), get()) }
    single { DetailsStateMachine(get(), get()) }
    single { GithubAPI(androidContext()) }
    single<IRemoteRepository> { GithubRepository(get()) }
    single<ILocalRepository> { SharedRepository(get(),get()) }
    single { provideSharedPrefs(androidContext()) }
    single { RxSharedPreferences.create(get()) }
}

private fun provideSharedPrefs(context: Context) = PreferenceManager.getDefaultSharedPreferences(context)