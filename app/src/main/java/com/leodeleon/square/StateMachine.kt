package com.leodeleon.square

import com.freeletics.rxredux.SideEffect
import com.freeletics.rxredux.reduxStore
import com.freeletics.rxredux.StateAccessor
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import com.leodeleon.square.data.GithubRepository
import com.leodeleon.square.data.SharedRepository
import com.leodeleon.square.entities.Repo
import com.leodeleon.square.Action.*
import com.leodeleon.square.StateMachine.State.*
import com.leodeleon.square.entities.User
import com.leodeleon.square.utils.MVI
import com.leodeleon.square.utils.logd
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import java.util.concurrent.TimeUnit

sealed class Action {
    object LoadRepos : Action()
    object RefreshBookmarks : Action()
    data class UpdateBookmark(val repo: Repo, val isBookmarked: Boolean) : Action()
    data class ShowRepo(val repo: Repo, val isBookmarked: Boolean) : Action()
}

private data class ReposLoaded(val repos: List<Repo>) : Action()
private data class UsersLoaded(val users: List<User>) : Action()
private data class BookmarksLoaded(val bookmarkIds: List<Long>) : Action()
private data class BookmarkUpdated(val isBookmarked: Boolean) : Action()
private data class ShowSnack(val message: String) : Action()
private object HideSnack : Action()
private object ShowLoading : Action()
private data class ShowError(val message: String) : Action()

class StateMachine(private val repository: GithubRepository, private val prefsRepository: SharedRepository) {

    private val refreshBookmarksSideEffect = sideEffect<ReposLoaded> {
        it.doOnNext {
            logd("ACTION: $it", MVI)
        }.map<Action> {
            RefreshBookmarks
        }
    }

    sealed class State {
        object LoadingState : State()
        data class ErrorState(val message: String) : State()
        data class ShowReposState(val repos: List<Repo>) : State()
        data class ShowUsersState(val users: List<User>) : State()
        data class SnackState(val shouldShow: Boolean, val message: String = "") : State()
        data class ShowRepoState(val repo: Repo, val isBookmarked: Boolean) : State()
        data class BookmarkState(val isBookmarked: Boolean) : State()
        data class BookmarksState(val bookmarkIds: List<Long>) : State()
    }

    val input: Relay<Action> = PublishRelay.create()

    val state: Observable<State> = input
            .doOnNext { logd("Input action $it", MVI) }
            .reduxStore(
                    initialState = LoadingState,
                    reducer = ::reducer,
                    sideEffects = listOf(
                            ::showAndHideSnackSideEffect,
                            ::loadReposSideEffect,
                            ::loadStargazersSideEffect,
                            ::updateBookmarkSideEffect,
                            ::loadBookmarksSideEffect,
                            refreshBookmarksSideEffect
                    )
            )
            .distinctUntilChanged()
            .doOnNext { logd("RxStore state: $it", MVI) }

    private fun reducer(state: State, action: Action): State {
        logd("Reducing action: $action", MVI)
        logd("Current state: $state", MVI)
        return when (action) {
            is ShowLoading -> LoadingState
            is BookmarksLoaded -> BookmarksState(action.bookmarkIds)
            is ShowError -> ErrorState(action.message)
            is ShowRepo -> ShowRepoState(action.repo, action.isBookmarked)
            is BookmarkUpdated -> BookmarkState(action.isBookmarked)
            is ReposLoaded -> ShowReposState(action.repos)
            is UsersLoaded -> ShowUsersState(action.users)
            is ShowSnack -> SnackState(true, action.message)
            is HideSnack -> SnackState(false)
            else -> state
        }
    }

    private fun loadReposSideEffect(actions: Observable<Action>, state: StateAccessor<State>): Observable<Action> {
        return actions.ofType(LoadRepos::class.java)
                .flatMap<Action> {
                    repository.getSquareRepos()
                            .map<Action> { ReposLoaded(it) }
                            .startWith(ShowLoading)
                            .onErrorReturn { ShowError("Error loading repos") }
                }
    }

//    private fun refreshBookmarksSideEffect(actions: Observable<Action>, state: StateAccessor<State>): Observable<Action> {
//        return actions.ofType(ReposLoaded::class.java)
//                .map<Action> {
//                    RefreshBookmarks
//                }
//    }

    private fun loadStargazersSideEffect(actions: Observable<Action>, state: StateAccessor<State>): Observable<Action> {
        return actions.ofType(ShowRepo::class.java)
                .flatMap { action ->
                    repository.getStargazers(action.repo.name)
                            .map<Action> { UsersLoaded(it) }
                }
                .onErrorReturn {
                    ShowError("Error loading users")
                }
    }

    private fun loadBookmarksSideEffect(actions: Observable<Action>, state: StateAccessor<State>): Observable<Action> {
        return actions.ofType(RefreshBookmarks::class.java)
                .flatMap {
                    prefsRepository.getBookmarks()
                            .map<Action> {
                                BookmarksLoaded(it.map { it.id })
                            }

                }
    }

    private fun updateBookmarkSideEffect(actions: Observable<Action>, state: StateAccessor<State>): Observable<Action> {
        return actions.ofType(UpdateBookmark::class.java)
                .flatMap<Action> { action ->
                    val bookmark = !action.isBookmarked
                    val result = if (bookmark) prefsRepository.saveBookmark(action.repo) else prefsRepository.removeBookmark(action.repo)
                    result.andThen(Observable.just(bookmark))
                            .map { BookmarkUpdated(it) }
                }
    }

    private fun showAndHideSnackSideEffect(actions: Observable<Action>, state: StateAccessor<State>): Observable<Action> {
        return actions.ofType(BookmarkUpdated::class.java)
                .switchMap { action ->
                    Observable.timer(3, TimeUnit.SECONDS)
                            .map<Action> { HideSnack }
                            .startWith(ShowSnack(
                                    if (action.isBookmarked) "Repository saved to bookmarks"
                                    else "Repository removed from bookmarks"
                            ))
                            .onErrorReturn {
                                ShowSnack("Error saving bookmark!")
                            }
                }
    }


    private inline fun<reified T: Action> sideEffect (crossinline block:(Observable<Action>) -> ObservableSource<Action>) : SideEffect<State,Action> {
        return object : SideEffect<State,Action> {
            override fun invoke(actions: Observable<Action>, state: StateAccessor<State>): Observable<out Action> {
                return actions
                        .ofType(T::class.java)
                        .cast(Action::class.java)
                        .compose(ObservableTransformer {
                            return@ObservableTransformer block(it)
                        })
            }
        }
    }
}