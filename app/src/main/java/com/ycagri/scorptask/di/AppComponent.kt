package com.ycagri.scorptask.di

import android.app.Application
import com.ycagri.scorptask.ScorpTaskApplication
import com.ycagri.scorptask.datasource.DataSource
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import dagger.android.support.DaggerApplication
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ActivityBindingModule::class,
        ApplicationModule::class,
        AndroidSupportInjectionModule::class,
        ViewModelModule::class
    ]
)
interface AppComponent : AndroidInjector<ScorpTaskApplication> {

    override fun inject(instance: ScorpTaskApplication)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}