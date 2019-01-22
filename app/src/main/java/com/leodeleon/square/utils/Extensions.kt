package com.leodeleon.square.utils

import android.content.res.Resources
import android.os.Parcelable
import com.google.android.material.snackbar.Snackbar
import androidx.fragment.app.Fragment
import android.util.Log
import android.view.View

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

inline fun<reified T: Parcelable> Fragment.parcelableArg(key: String) = lazy {
    arguments!!.getParcelable<T>(key)!!
}

fun Fragment.booleanArg(key: String) = lazy {
    arguments?.getBoolean(key)?: false
}