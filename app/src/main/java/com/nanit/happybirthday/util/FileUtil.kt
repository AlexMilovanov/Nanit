package com.nanit.happybirthday.util

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION_CODES
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import com.nanit.happybirthday.BuildConfig
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

private const val FILENAME_TIMESTAMP = "yyyyMMdd_HHmmss"
private const val DCIM_FOLDER_NAME = "DCIM/Nanit"
private const val TMP_PHOTO_FILE_NAME = "tmp_profile_photo_file.jpg";


fun createImageFile(context: Context): Uri? {
    // Create an image file name
    val imageFileName = "JPEG_" + getTimestamp() + "_"

    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, imageFileName)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        if (Build.VERSION.SDK_INT >= VERSION_CODES.Q) {
            put(MediaStore.MediaColumns.RELATIVE_PATH, DCIM_FOLDER_NAME)
        }
    }

    return context.contentResolver.insert(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        contentValues
    )
}

fun saveScreenshot(bitmap: Bitmap, context: Context): Uri? {
    try {
        val path: String = context.externalCacheDir.toString() + "/" + getTimestamp() + ".jpg"
        val imageFile = File(path)
        val outputStream = FileOutputStream(imageFile)
        val quality = 100
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        outputStream.flush()
        outputStream.close()
        return FileProvider.getUriForFile(
            context,
            BuildConfig.APPLICATION_ID + ".provider",
            imageFile
        )
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}

private fun createTempFile(inStream: InputStream, cacheDir: File): String {
    val tempFile = File(cacheDir, TMP_PHOTO_FILE_NAME)
    var os: OutputStream? = null
    var result = ""
    try {
        os = FileOutputStream(tempFile)
        val buffer = ByteArray(8192)
        var length: Int
        while ((inStream.read(buffer).also { length = it }) > 0) {
            os.write(buffer, 0, length);
        }
        result = tempFile.absolutePath;
        tempFile.deleteOnExit();
    } catch (e: IOException) {
        Log.e("TAG", "Error creating tmp file: " + e.message)
    } finally {
        inStream.close()
        os?.flush()
        os?.close()
        return result
    }
}

private fun getTimestamp(): String {
    return SimpleDateFormat(FILENAME_TIMESTAMP, Locale.getDefault()).format(Date())
}

