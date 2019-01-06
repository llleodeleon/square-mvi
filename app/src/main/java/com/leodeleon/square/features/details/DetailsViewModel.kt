package com.leodeleon.square.features.details

import android.content.Context
import android.databinding.ObservableArrayList
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import com.leodeleon.square.Action.*
import com.leodeleon.square.BR
import com.leodeleon.square.R
import com.leodeleon.square.StateMachine
import com.leodeleon.square.StateMachine.State.*
import com.leodeleon.square.databinding.FragmentDetailsBinding
import com.leodeleon.square.entities.Repo
import com.leodeleon.square.entities.User
import com.leodeleon.square.features.BaseViewModel
import com.leodeleon.square.utils.gone
import com.leodeleon.square.utils.snack
import com.leodeleon.square.utils.visible
import me.tatarka.bindingcollectionadapter2.ItemBinding

class DetailsViewModel(stateMachine: StateMachine): BaseViewModel(stateMachine) {

    private lateinit var binding: FragmentDetailsBinding
    private var snackbar: Snackbar? = null

    val itemBinding = ItemBinding.of<User>(BR.user, R.layout.item_user)
    val items = ObservableArrayList<User>()

    interface  OnUpdateRepo {
        fun onClick(view:View, repo: Repo)
    }

    private val clickListener = object: OnUpdateRepo {
        override fun onClick(view: View, repo: Repo) {
            input.accept(UpdateBookmark(repo, view.isSelected))
        }
    }

    override fun inflateView(context: Context): View {
        binding = FragmentDetailsBinding.inflate(LayoutInflater.from(context))
        binding.viewModel = this
        binding.clickListener = clickListener
        return binding.root
    }

    override fun render(state: StateMachine.State) {
        binding.apply {
            if(state !is ErrorState){
                if(errorBinding!!.errorGroup.isVisible)
                    errorBinding.errorGroup.gone()
            }
            when(state){
                is ShowRepoState -> {
                    this.repo = state.repo
                    fab.isSelected = state.isBookmarked
                }
                is BookmarkState -> {
                    fab.isSelected = state.isBookmarked
                }
                is ShowUsersState -> {
                    usersRecycler.visible()
                    if(items.isEmpty()){
                        state.users.toCollection(items)
                    }
                }
                is ErrorState -> {
                    errorBinding!!.errorGroup.visible()
                    usersRecycler.gone()
                }
                is SnackState -> {
                    if(state.shouldShow){
                        snackbar = root.snack(state.message)
                    } else {
                        snackbar?.dismiss()
                    }
                }
            }
        }    }
}