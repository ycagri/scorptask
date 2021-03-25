package com.ycagri.scorptask.di

import com.ycagri.scorptask.MainFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBindingModule {

    @ContributesAndroidInjector
    abstract fun mainFragment(): MainFragment
}