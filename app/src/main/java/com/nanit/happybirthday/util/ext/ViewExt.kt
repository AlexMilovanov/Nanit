package com.nanit.happybirthday.util.ext

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.view.View

fun View.takeScreenshot(): Bitmap {
    val bitmap = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    if (background != null) {
        background.draw(canvas)
    } else {
        canvas.drawColor(Color.WHITE)
    }
    draw(canvas)

    return bitmap
}