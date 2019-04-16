package com.anton.mercaritest.extensions

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide

fun ImageView.loadImage(url: String?, @DrawableRes placeholder: Int) {
    Glide.with(this).load(url)
        .placeholder(placeholder)
        .error(placeholder)
        .into(this)
}