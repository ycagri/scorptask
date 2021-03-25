package com.ycagri.scorptask.di

import com.ycagri.scorptask.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {

    @ContributesAndroidInjector(modules = [FragmentBindingModule::class])
    abstract fun mainActivity(): MainActivity
}