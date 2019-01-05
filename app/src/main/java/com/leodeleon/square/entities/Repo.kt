package com.leodeleon.square.entities

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class Repo(
        val id : Long,
        val name : String,
        @Json(name="stargazers_count") val stars : Int
): Parcelable