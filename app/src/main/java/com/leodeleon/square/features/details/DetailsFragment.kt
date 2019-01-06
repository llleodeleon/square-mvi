package com.leodeleon.square.features.details

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.leodeleon.domain.states.ShowRepo
import com.leodeleon.square.MainActivity
import com.leodeleon.square.R
import com.leodeleon.square.entities.RepoEntity
import com.leodeleon.square.utils.EXTRA_BOOKMARK
import com.leodeleon.square.utils.EXTRA_REPO
import com.leodeleon.square.utils.booleanArg
import com.leodeleon.square.utils.parcelableArg
import org.koin.android.viewmodel.ext.android.viewModel

class  DetailsFragment: Fragment() {
    private val repo by parcelableArg<RepoEntity>(EXTRA_REPO)
    private val bookmark by booleanArg(EXTRA_BOOKMARK)
    private val viewModel: DetailsViewModel by viewModel ()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = viewModel.inflateView(context!!)
        setupToolbar(view.findViewById(R.id.toolbar))
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.input.accept(ShowRepo(repo.unwrap(), bookmark))
    }


    private fun setupToolbar(toolbar: Toolbar){
        (activity as? MainActivity)?.apply {
            setSupportActionBar(toolbar)
            setupActionBar()
        }
    }
}