package com.leodeleon.square.utils

import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import com.leodeleon.square.R
import com.leodeleon.square.entities.Repo

object Navigator {
    fun showDetails(view: View, repo: Repo, isBookmark: Boolean) {
        Navigation.findNavController(view).navigate(R.id.action_repos_to_details,
                bundleOf(EXTRA_REPO to repo, EXTRA_BOOKMARK to isBookmark))
    }
}