package com.leodeleon.domain.states

import com.freeletics.rxredux.SideEffect
import com.freeletics.rxredux.reduxStore
import com.freeletics.rxredux.StateAccessor
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer

abstract class BaseStateMachine {

    abstract val effects: List<SideEffect<State, Action>>
    abstract val initialState: State

    val input: Relay<Action> = PublishRelay.create()

    val state: Observable<State> by lazy {
        input.doOnNext { println("MVI: Input action $it") }
                .reduxStore(
                        initialState = initialState,
                        reducer = ::reducer,
                        sideEffects = effects
                )
                .distinctUntilChanged()
                .doOnNext { println("MVI: RxStore state: $it") }
    }

    abstract fun reducer(state: State, action: Action): State

    fun sideEffects(effects: SideEffectsBuilder.() -> Unit): List<SideEffect<State, Action>> {
        val builder = SideEffectsBuilder()
        builder.effects()
        return builder.build()
    }

    class SideEffectsBuilder {
        val sideEffects: MutableList<SideEffect<State, Action>> = mutableListOf()

        fun build(): List<SideEffect<State, Action>> = sideEffects

        inline fun <reified T : Action> sideEffect(crossinline block: (Observable<T>) -> ObservableSource<Action>) {
            val effect = object : SideEffect<State, Action> {
                override fun invoke(actions: Observable<Action>, state: StateAccessor<State>): Observable<out Action> {
                    return actions
                            .ofType(T::class.java)
                            .compose(ObservableTransformer {
                                return@ObservableTransformer block(it)
                            })
                }
            }
            sideEffects.add(effect)
        }
    }
}