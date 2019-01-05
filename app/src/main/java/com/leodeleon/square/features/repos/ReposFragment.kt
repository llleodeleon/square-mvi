package com.leodeleon.square.features.repos

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.koin.android.viewmodel.ext.android.viewModel

class ReposFragment: Fragment() {
    private val viewModel: ReposViewModel by viewModel ()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return viewModel.inflateView(context!!)
    }
}