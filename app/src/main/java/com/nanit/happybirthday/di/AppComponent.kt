package com.nanit.happybirthday.di

import com.nanit.happybirthday.DetailsActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ViewModelModule::class, RepositoryModule::class])
interface AppComponent {

    fun inject(activity: DetailsActivity)
}