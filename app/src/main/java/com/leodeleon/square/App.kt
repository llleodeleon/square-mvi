package com.leodeleon.square

import android.app.Application
import com.leodeleon.square.di.appModule
import org.koin.android.ext.android.startKoin

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(appModule))
    }
}