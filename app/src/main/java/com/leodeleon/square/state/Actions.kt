package com.leodeleon.square.state

import com.leodeleon.square.entities.Repo
import com.leodeleon.square.entities.User

sealed class Action
data class ShowError(val message: String) : Action()
object ShowLoading : Action()
//REPOS
object LoadRepos : Action()
object RefreshBookmarks : Action()
data class ReposLoaded(val repos: List<Repo>) : Action()
data class BookmarksLoaded(val bookmarkIds: List<Long>) : Action()
//DETAILS
data class UpdateBookmark(val repo: Repo, val isBookmarked: Boolean) : Action()
data class ShowRepo(val repo: Repo, val isBookmarked: Boolean) : Action()
data class UsersLoaded(val users: List<User>) : Action()
data class BookmarkUpdated(val isBookmarked: Boolean) : Action()
data class ShowSnack(val message: String) : Action()
object HideSnack : Action()
