package com.leodeleon.square.data

import com.leodeleon.square.entities.Repo
import com.leodeleon.square.entities.User
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubService {
    @GET("orgs/{org}/repos")
    fun getOrgRepos(@Path("org") orgId: String, @Query("type") type: String): Observable<List<Repo>>

    @GET("repos/{org}/{repo}/stargazers")
    fun getStargazers(@Path("org") orgId: String, @Path("repo") name: String): Observable<List<User>>
}