package com.leodeleon.domain.states

import com.leodeleon.domain.repositories.ILocalRepository
import com.leodeleon.domain.repositories.IRemoteRepository

class ReposStateMachine(remote: IRemoteRepository, local: ILocalRepository): BaseStateMachine() {

    override val initialState = LoadingState

    override fun reducer(state: State, action: Action): State {
        return when(action){
            is ShowLoading -> LoadingState
            is BookmarksLoaded -> BookmarksState(action.bookmarkIds)
            is ReposLoaded -> ShowReposState(action.repos)
            else -> state
        }
    }

    override val effects= sideEffects {
        sideEffect<LoadRepos> {
            it.flatMap<Action> {
                remote.getSquareRepos()
                        .map<Action> {
                            ReposLoaded(it)
                        }
                        .onErrorReturn { ShowError("Error loading repos") }
            }
        }

        sideEffect<ReposLoaded> {
            it.doOnNext {
                println("MVI ACTION: $it")
            }.map<Action> {
                RefreshBookmarks
            }
        }

        sideEffect<RefreshBookmarks> {
            it.flatMap {
                local.getBookmarks()
                        .map<Action> {
                            BookmarksLoaded(it.map { it.id })
                        }
            }
        }
    }
}