package com.nanit.happybirthday

import android.app.Application
import com.nanit.happybirthday.di.AppComponent
import com.nanit.happybirthday.di.DaggerAppComponent
import com.nanit.happybirthday.di.RepositoryModule

class NanitApp : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .repositoryModule(RepositoryModule(applicationContext))
            .build()
    }
}