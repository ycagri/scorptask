package com.ycagri.scorptask.di

import androidx.lifecycle.ViewModel
import com.ycagri.scorptask.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindChatFragmentViewModel(mainViewModel: MainViewModel): ViewModel
}