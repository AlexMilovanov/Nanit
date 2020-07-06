package com.nanit.happybirthday.di

import com.nanit.happybirthday.birthday.BirthdayDialogFragment
import com.nanit.happybirthday.detail.DetailsActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ViewModelModule::class, RepositoryModule::class])
interface AppComponent {

    fun inject(activity: DetailsActivity)

    fun inject(fragment: BirthdayDialogFragment)
}