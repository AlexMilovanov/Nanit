package com.nanit.happybirthday.ext

import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.nanit.happybirthday.R

fun ImageView.loadImage(
    uri: Uri,
    circleCrop: Boolean = false,
    errorResId: Int = R.drawable.ic_camera_blue,
    placeholderResId: Int = R.drawable.ic_camera_blue
) {
    RequestOptions()
        .error(errorResId)
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