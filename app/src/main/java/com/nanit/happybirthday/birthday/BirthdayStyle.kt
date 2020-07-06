package com.nanit.happybirthday.birthday

import com.nanit.happybirthday.R

enum class BirthdayStyle {
    YELLOW, BLUE, GREEN;

    val backgroundImageResId: Int
        get() = when (this) {
            YELLOW -> R.drawable.img_popup_elephant
            BLUE ->  R.drawable.img_popup_pelican
            GREEN ->  R.drawable.img_popup_fox
        }

    val photoPlaceholderResId: Int
        get() = when (this) {
            YELLOW -> R.drawable.img_placeholder_yellow
            BLUE ->  R.drawable.img_placeholder_blue
            GREEN ->  R.drawable.img_placeholder_green
        }

    val cameraIconResId: Int
        get() = when (this) {
            YELLOW -> R.drawable.ic_camera_yellow
            BLUE ->  R.drawable.ic_camera_blue
            GREEN ->  R.drawable.ic_camera_green
        }
}