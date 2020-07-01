package com.nanit.happybirthday.model

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
}