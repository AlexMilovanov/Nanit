package com.nanit.happybirthday.model

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface BabyDao {

    @Query("SELECT * FROM Baby LIMIT 1")
    suspend fun loadBaby(): Baby?

    @Query("SELECT photoPath FROM Baby LIMIT 1")
    fun loadBabyPhoto(): LiveData<String?>

    @Transaction
    suspend fun updateData(baby: Baby) {
        deleteAllEntries()
        insertData(baby)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(baby: Baby)

    @Query("DELETE FROM Baby")
    suspend fun deleteAllEntries()

    @Query("UPDATE Baby SET photoPath = :uri WHERE name = :name")
    suspend fun updatePhoto(uri: String, name: String)
}