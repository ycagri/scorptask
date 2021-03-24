package com.ycagri.scorptask

import com.ycagri.scorptask.datasource.DataSource
import com.ycagri.scorptask.di.AppComponent
import com.ycagri.scorptask.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

class ScorpTaskApplication: DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        val component: AppComponent = DaggerAppComponent.builder()
            .application(this)
            .dataSource(DataSource())
            .build()

        component.inject(this)

        return component
    }
}