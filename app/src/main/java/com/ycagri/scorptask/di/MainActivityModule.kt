package com.ycagri.scorptask.di

import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ycagri.scorptask.MainActivity
import dagger.Module
import dagger.Provides

@Module
class MainActivityModule {

    @Module
    companion object {
        @Provides
        @JvmStatic
        fun provideLayoutManager(activity: MainActivity): RecyclerView.LayoutManager {
            return LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }

        @Provides
        @JvmStatic
        fun provideItemDecoration(activity: MainActivity): RecyclerView.ItemDecoration {
            return DividerItemDecoration(activity, LinearLayoutManager.VERTICAL)
        }
    }
}