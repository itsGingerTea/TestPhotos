package com.example.unsplashapp.utils

import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

internal fun Fragment.snackbar(view: View, text: Int, length: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(view, text, length).show()
}

internal fun View.makeGone() {
    if (!isGone) visibility = View.GONE
}

internal fun View.makeVisible() {
    if (!isVisible) visibility = View.VISIBLE
}

internal fun View.makeInvisible() {
    if (!isInvisible) visibility = View.INVISIBLE
}