package com.leodeleon.square.utils

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.content.res.Resources
import android.os.Parcelable
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import com.freeletics.rxredux.SideEffect

val Int.dp: Int get() = (this * Resources.getSystem().displayMetrics.density).toInt()

fun Any.logd(msg:String, tag: String = this.javaClass.simpleName){
    Log.d(tag, msg)
}

fun View.snack(message: String, length: Int = Snackbar.LENGTH_INDEFINITE): Snackbar {
    val snack = Snackbar.make(this, message, length)
    snack.show()
    return snack
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

inline fun <reified T> Fragment.observe(data: LiveData<T>, crossinline onValue: (T) -> Unit ) {
    data.observe(this, Observer {
            it?.let { onValue(it) }
    })
}

fun Parcelable.toBundle(key: String) = bundleOf(key to this)

inline fun<reified T: Parcelable> Fragment.parcelableArg(key: String) = lazy {
    arguments!!.getParcelable<T>(key)!!
}

fun Fragment.booleanArg(key: String) = lazy {
    arguments?.getBoolean(key)?: false
}