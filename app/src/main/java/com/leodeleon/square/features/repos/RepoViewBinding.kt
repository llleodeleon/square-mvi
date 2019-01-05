package com.leodeleon.square.features.repos

import android.databinding.ObservableBoolean
import android.view.View
import com.leodeleon.square.entities.Repo
import com.leodeleon.square.utils.Navigator

class RepoViewBinding(val repo: Repo) {
    val isBookmark = ObservableBoolean()

    val clickListener = View.OnClickListener {
        Navigator.showDetails(it,repo,isBookmark.get())
    }
}