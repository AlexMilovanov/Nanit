package com.nanit.happybirthday.model

import androidx.room.*

@Dao
interface BabyDao {

    @Query("SELECT * FROM Baby LIMIT 1")
    suspend fun loadBaby(): Baby?

    @Transaction
    suspend fun updateData(baby: Baby) {
        deleteAllEntries()
        insertData(baby)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(baby: Baby)

    @Query("DELETE FROM Baby")
    suspend fun deleteAllEntries()
}