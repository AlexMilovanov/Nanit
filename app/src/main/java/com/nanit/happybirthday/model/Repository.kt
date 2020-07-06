package com.nanit.happybirthday.model

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(private val babyDao: BabyDao) {

    suspend fun saveBabyData(data: Baby) {
        babyDao.updateData(data)
    }

    suspend fun getBabyData(): Baby? {
        return babyDao.loadBaby()
    }

    suspend fun updateBabyPhoto(uri: String) {
        getBabyData()?.name?.run {
            babyDao.updatePhoto(uri, this)
        }
    }

    fun getBabyPhotoPath(): LiveData<Uri?> =
        Transformations.map(babyDao.loadBabyPhoto()) { path ->
            if (path.isNullOrEmpty()) { null } else { Uri.parse(path) }
        }

}