package com.leodeleon.domain.repositories

import com.leodeleon.domain.entities.Repo
import io.reactivex.Completable
import io.reactivex.Observable

interface ILocalRepository {
    fun getBookmarks(): Observable<List<Repo>>
    fun saveBookmark(repo: Repo): Completable
    fun removeBookmark(repo: Repo): Completable
}