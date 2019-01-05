package com.leodeleon.square.data

import com.leodeleon.square.utils.SQUARE_ID
import com.leodeleon.square.utils.TYPE_PUBLIC

class  GithubRepository(val service: GithubService){

    fun getSquareRepos() = service.getOrgRepos(SQUARE_ID, TYPE_PUBLIC)
    fun getStargazers(name: String) = service.getStargazers(SQUARE_ID, name)

}