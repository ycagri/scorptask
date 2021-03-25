package com.ycagri.scorptask.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ycagri.scorptask.MainViewModel
import com.ycagri.scorptask.utils.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(mainViewModel: MainViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}