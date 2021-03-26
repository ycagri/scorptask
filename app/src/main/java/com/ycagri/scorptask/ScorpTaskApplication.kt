package com.ycagri.scorptask

import android.app.Activity
import android.app.Application
import com.ycagri.scorptask.datasource.DataSource
import com.ycagri.scorptask.di.AppComponent
import com.ycagri.scorptask.di.AppInjector
import com.ycagri.scorptask.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.support.DaggerApplication
import javax.inject.Inject

class ScorpTaskApplication: Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()

        AppInjector.init(this)
    }

    override fun activityInjector() = dispatchingAndroidInjector
}