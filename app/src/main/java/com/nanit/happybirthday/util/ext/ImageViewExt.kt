package com.nanit.happybirthday.util.ext

import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

fun ImageView.loadImage(
    uri: Uri,
    circleCrop: Boolean = false,
    placeholderResId: Int
) {
    RequestOptions()
        .error(placeholderResId)
        .placeholder(placeholderResId).apply {
            if (circleCrop) {
                circleCrop()
            } else {
                fitCenter()
            }
        }.also {
            Glide.with(this)
                .load(uri)
                .apply(it)
                .into(this)
        }
}