package com.leodeleon.data.entities

import com.leodeleon.domain.entities.User
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserEntity(
        val id: Long,
        val login: String,
        val avatar_url: String
) {
    fun unwrap() = User(id,login,avatar_url)
}
