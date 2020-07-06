package com.nanit.happybirthday.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nanit.happybirthday.birthday.BirthdayViewModel
import com.nanit.happybirthday.detail.DetailsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(DetailsViewModel::class)
    internal abstract fun provideDetailsViewModel(viewModel: DetailsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BirthdayViewModel::class)
    internal abstract fun provideBirthdayViewModel(viewModel: BirthdayViewModel): ViewModel
}