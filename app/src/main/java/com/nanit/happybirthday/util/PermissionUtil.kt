package com.nanit.happybirthday.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Build.VERSION
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat

fun appRequiresRuntimePermissions(): Boolean {
    return VERSION.SDK_INT >= 23
}

fun isWriteExtStoragePermissionGranted(context: Context): Boolean {
    return ActivityCompat.checkSelfPermission(
        context, Manifest.permission.WRITE_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED
}

@RequiresApi(Build.VERSION_CODES.M)
fun requestWriteExtStoragePermission(activity: Activity, reqCode: Int) {
    activity.requestPermissions(
        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
        reqCode
    )
}
