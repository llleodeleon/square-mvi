package com.leodeleon.square.features.repos

import androidx.databinding.ObservableBoolean
import android.view.View
import com.leodeleon.domain.entities.Repo
import com.leodeleon.square.utils.Navigator

class RepoViewBinding(val repo: Repo) {
    val isBookmark = ObservableBoolean()

    val clickListener = View.OnClickListener {
        Navigator.showDetails(it,repo,isBookmark.get())
    }
}