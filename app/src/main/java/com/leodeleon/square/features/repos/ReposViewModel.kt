package com.leodeleon.square.features.repos

import android.content.Context
import android.databinding.ObservableArrayList
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import com.leodeleon.square.BR
import com.leodeleon.square.R
import com.leodeleon.square.databinding.FragmentReposBinding
import com.leodeleon.square.features.BaseViewModel
import com.leodeleon.square.state.*
import com.leodeleon.square.utils.*
import me.tatarka.bindingcollectionadapter2.ItemBinding

class ReposViewModel(stateMachine: ReposStateMachine): BaseViewModel(stateMachine) {
    private lateinit var binding: FragmentReposBinding

    val itemBinding: ItemBinding<RepoViewBinding> = ItemBinding.of(BR.viewBinding, R.layout.item_repo)
    val items = ObservableArrayList<RepoViewBinding>()

    override fun inflateView(context: Context): View {
        binding = FragmentReposBinding.inflate(LayoutInflater.from(context))
        binding.viewModel = this
        input.accept( if(items.isEmpty()) LoadRepos else RefreshBookmarks)
        return  binding.root
    }

    override fun render(state: State){
        binding.apply{
            if(state !is LoadingState){
                if(loading.isVisible)
                    loading.gone()
            }
            if(state !is ErrorState) {
               if(errorBinding!!.errorGroup.isVisible)
                    errorBinding.errorGroup.gone()
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