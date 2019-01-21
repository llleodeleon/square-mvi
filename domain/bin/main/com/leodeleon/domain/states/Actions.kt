package com.leodeleon.domain.states

import com.leodeleon.domain.entities.*

sealed class Action
internal data class ShowError(val message: String) : Action()
internal object ShowLoading : Action()
//REPOS
object LoadRepos : Action()
object RefreshBookmarks : Action()
internal data class ReposLoaded(val repos: List<Repo>) : Action()
internal data class BookmarksLoaded(val bookmarkIds: List<Long>) : Action()
//DETAILS
data class UpdateBookmark(val repo: Repo, val isBookmarked: Boolean) : Action()
data class ShowRepo(val repo: Repo, val isBookmarked: Boolean) : Action()
internal data class UsersLoaded(val users: List<User>) : Action()
internal data class BookmarkUpdated(val isBookmarked: Boolean) : Action()
internal data class ShowSnack(val message: String) : Action()
internal object HideSnack : Action()
