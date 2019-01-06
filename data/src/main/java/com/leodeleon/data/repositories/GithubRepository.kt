package com.leodeleon.data.repositories

import com.leodeleon.data.utils.SQUARE_ID
import com.leodeleon.data.utils.TYPE_PUBLIC
import com.leodeleon.domain.entities.Repo
import com.leodeleon.domain.entities.User
import com.leodeleon.domain.repositories.IRemoteRepository
import io.reactivex.Observable

class  GithubRepository(val api: GithubAPI): IRemoteRepository {

    override fun getSquareRepos(): Observable<List<Repo>> =
            api.service.getOrgRepos(SQUARE_ID, TYPE_PUBLIC)
            .map { it.map { it.unwrap() }}

    override fun getStargazers(name: String): Observable<List<User>> =
            api.service.getStargazers(SQUARE_ID, name)
            .map { it.map { it.unwrap() } }

}