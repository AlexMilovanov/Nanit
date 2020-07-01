package com.nanit.happybirthday.di

import android.content.Context
import com.nanit.happybirthday.model.AppDatabase
import com.nanit.happybirthday.model.BabyDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule(private val context: Context) {

    @Singleton
    @Provides
    fun provideAppDatabase(): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Singleton
    @Provides
    fun provideBabyDao(db: AppDatabase): BabyDao {
        return db.babyDao()
    }
}