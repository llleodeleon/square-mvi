package com.leodeleon.data.utils

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers

object SchedulerProvider {
    fun io(): Scheduler = Schedulers.io()
    fun main(): Scheduler = AndroidSchedulers.mainThread()
}