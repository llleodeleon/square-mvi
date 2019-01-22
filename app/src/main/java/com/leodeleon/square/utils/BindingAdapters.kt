package com.leodeleon.square.utils

import androidx.databinding.BindingAdapter
import android.widget.ImageView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.leodeleon.square.R

object BindingAdapters {

    @BindingAdapter(value = ["imageUrl"])
    @JvmStatic
    fun loadImage(view: ImageView, imageUrl: String){
        val requestOptions = RequestOptions()
                .transforms(RoundedCorners(8.dp))

        GlideApp.with(view)
                .load(imageUrl)
                .placeholder(R.drawable.placeholder_user)
                .apply(requestOptions)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(view)
    }

}