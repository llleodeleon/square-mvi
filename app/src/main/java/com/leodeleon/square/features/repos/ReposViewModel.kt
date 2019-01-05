package com.leodeleon.square.features.repos

import android.content.Context
import android.databinding.ObservableArrayList
import android.view.LayoutInflater
import android.view.View
import com.leodeleon.square.Action.*
import com.leodeleon.square.BR
import com.leodeleon.square.R
import com.leodeleon.square.StateMachine
import com.leodeleon.square.StateMachine.State.*
import com.leodeleon.square.StateMachine.State
import com.leodeleon.square.databinding.FragmentReposBinding
import com.leodeleon.square.features.BaseViewModel
import com.leodeleon.square.utils.*
import me.tatarka.bindingcollectionadapter2.ItemBinding

class ReposViewModel(stateMachine: StateMachine): BaseViewModel(stateMachine) {
    private lateinit var binding: FragmentReposBinding

    val itemBinding= ItemBinding.of<RepoViewBinding>(BR.viewBinding, R.layout.item_repo)
    val items = ObservableArrayList<RepoViewBinding>()

    override fun inflateView(context: Context): View {
        binding = FragmentReposBinding.inflate(LayoutInflater.from(context))
        binding.viewModel = this
        input.accept( if(items.isEmpty()) LoadRepos else RefreshBookmarks)
        return  binding.root
    }

    override fun render(state: State){
        logd("Rendering state $state")
        binding.apply{
            if(state !is LoadingState){
                loading.gone()
            }
            if(state !is ErrorState) {
                errorBinding!!.errorGroup.gone()
            }
            when(state){
                is ShowReposState -> {
                    repoRecycler.visible()
                    state.repos.mapTo(items) {
                        RepoViewBinding(it)
                    }
                }
                is BookmarksState -> {
                    items.map {
                        it.isBookmark.set(state.bookmarkIds.contains(it.repo.id))
                    }
                }
                is LoadingState -> {
                    loading.visible()
                    repoRecycler.gone()
                }
                is ErrorState -> {
                    errorBinding!!.errorGroup.visible()
                    errorBinding.errorMessage.text = state.message
                    repoRecycler.gone()
                }
            }
        }
    }
}