package com.leodeleon.domain.states

import com.leodeleon.domain.entities.*

sealed class State
object LoadingState : State()
object EmptyState : State()
data class ErrorState(val message: String) : State()
//REPOS
data class ShowReposState(val repos: List<Repo>) : State()
data class BookmarksState(val bookmarkIds: List<Long>) : State()
//DETAILS
data class ShowUsersState(val users: List<User>) : State()
data class SnackState(val shouldShow: Boolean, val message: String = "") : State()
data class ShowRepoState(val repo: Repo, val isBookmarked: Boolean) : State()
data class BookmarkState(val isBookmarked: Boolean) : State()
