package com.leodeleon.data.entities

import com.leodeleon.domain.entities.Repo
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RepoEntity(
        val id : Long,
        val name : String,
        @Json(name="stargazers_count") val stars : Int
) {
    fun unwrap() = Repo(id, name, stars)
}