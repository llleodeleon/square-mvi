package com.leodeleon.domain.repositories

import com.leodeleon.domain.entities.Repo
import com.leodeleon.domain.entities.User
import io.reactivex.Observable

interface IRemoteRepository {
    fun getSquareRepos(): Observable<List<Repo>>
    fun getStargazers(name: String): Observable<List<User>>
}