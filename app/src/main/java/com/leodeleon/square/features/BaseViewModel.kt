package com.leodeleon.square.features


import android.arch.lifecycle.ViewModel
import android.content.Context
import android.view.View
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import com.leodeleon.square.Action
import com.leodeleon.square.StateMachine
import com.leodeleon.square.utils.SchedulerProvider
import com.leodeleon.square.utils.logd
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy

abstract class BaseViewModel(stateMachine: StateMachine) : ViewModel() {

    private val inputRelay: Relay<Action> = PublishRelay.create()
    private val subscriptions = CompositeDisposable()

    val input: Consumer<Action> = inputRelay

    init {
        inputRelay.subscribe(stateMachine.input).addTo(subscriptions)
        stateMachine.state
                .observeOn(SchedulerProvider.main())
                .doOnNext {
                    logd("Rendering state: $it", "MVI")
                }
                .subscribe(::render)
                .addTo(subscriptions)
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions.clear()
    }

    abstract fun inflateView(context: Context): View
    abstract fun render(state: StateMachine.State)
}