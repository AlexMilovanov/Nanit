package com.nanit.happybirthday.util

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION_CODES
import android.os.Environment
import android.provider.MediaStore
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

private const val FILENAME_TIMESTAMP = "yyyyMMdd_HHmmss"
private const val DCIM_FOLDER_NAME = "DCIM/Nanit"

fun createImageFile(context: Context): Uri? {
    // Create an image file name
    val timeStamp = SimpleDateFormat(FILENAME_TIMESTAMP, Locale.getDefault()).format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"

    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, imageFileName)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        if (Build.VERSION.SDK_INT >= VERSION_CODES.Q) {
            put(MediaStore.MediaColumns.RELATIVE_PATH, DCIM_FOLDER_NAME)
        }
    }

    return context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
}

