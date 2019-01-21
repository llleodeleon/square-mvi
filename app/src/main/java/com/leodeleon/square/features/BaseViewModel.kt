package com.leodeleon.square.features

import androidx.lifecycle.ViewModel
import android.content.Context
import android.view.View
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import com.leodeleon.data.utils.SchedulerProvider
import com.leodeleon.domain.states.Action
import com.leodeleon.domain.states.BaseStateMachine
import com.leodeleon.domain.states.State
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.rxkotlin.addTo

abstract class BaseViewModel(stateMachine: BaseStateMachine) : ViewModel() {

    private val inputRelay: Relay<Action> = PublishRelay.create()
    private val subscriptions = CompositeDisposable()

    val input: Consumer<Action> = inputRelay

    init {
        inputRelay.subscribe(stateMachine.input).addTo(subscriptions)
        stateMachine.state
                .observeOn(SchedulerProvider.main())
                .doOnNext {
                    println("MVI: Rendering state: $it")
                }
                .subscribe(::render)
                .addTo(subscriptions)
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions.clear()
    }

    abstract fun inflateView(context: Context): View
    abstract fun render(state: State)
}