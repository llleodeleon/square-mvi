package com.leodeleon.square.state

import com.leodeleon.square.data.GithubRepository
import com.leodeleon.square.data.SharedRepository
import com.leodeleon.square.utils.MVI
import com.leodeleon.square.utils.logd

class ReposStateMachine(repository: GithubRepository, prefsRepository: SharedRepository): BaseStateMachine() {

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
                repository.getSquareRepos()
                        .map<Action> {
                            ReposLoaded(it)
                        }
                        .startWith(ShowLoading)
                        .onErrorReturn { ShowError("Error loading repos") }
            }
        }

        sideEffect<ReposLoaded> {
            it.doOnNext {
                logd("ACTION: $it", MVI)
            }.map<Action> {
                RefreshBookmarks
            }
        }

        sideEffect<RefreshBookmarks> {
            it.flatMap {
                prefsRepository.getBookmarks()
                        .map<Action> {
                            BookmarksLoaded(it.map { it.id })
                        }
            }
        }
    }
}