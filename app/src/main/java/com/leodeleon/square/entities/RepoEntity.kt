package com.leodeleon.square.entities

import android.os.Parcelable
import com.leodeleon.domain.entities.Repo
import kotlinx.android.parcel.Parcelize

@Parcelize
internal data class RepoEntity(
        val id : Long,
        val name : String,
        val stars : Int
): Parcelable {

    constructor(repo: Repo): this(repo.id,repo.name,repo.stars)

    fun unwrap() = Repo(id,name,stars)
}