package com.leodeleon.square.entities

import android.annotation.SuppressLint
import android.os.Parcelable
import com.leodeleon.domain.entities.Repo
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
internal data class RepoEntity(
        val id : Long,
        val name : String,
        val stars : Int
): Parcelable {

    constructor(repo: Repo): this(repo.id,repo.name,repo.stars)

    fun unwrap() = Repo(id,name,stars)
}