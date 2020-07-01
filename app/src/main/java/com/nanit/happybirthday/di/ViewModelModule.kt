package com.nanit.happybirthday.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nanit.happybirthday.view_model.DetailsViewModel
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton
import kotlin.reflect.KClass

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(DetailsViewModel::class)
    internal abstract fun provideDetailsViewModel(viewModel: DetailsViewModel): ViewModel
}