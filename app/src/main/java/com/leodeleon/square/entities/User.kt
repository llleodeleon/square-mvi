package com.leodeleon.square.entities

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User(
        val id: Long,
        val login: String,
        val avatar_url: String
)
