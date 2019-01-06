package com.leodeleon.data

import com.leodeleon.data.entities.RepoEntity
import com.leodeleon.data.entities.UserEntity
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubService {
    @GET("orgs/{org}/repos")
    fun getOrgRepos(@Path("org") orgId: String, @Query("type") type: String): Observable<List<RepoEntity>>

    @GET("repos/{org}/{repo}/stargazers")
    fun getStargazers(@Path("org") orgId: String, @Path("repo") name: String): Observable<List<UserEntity>>
}