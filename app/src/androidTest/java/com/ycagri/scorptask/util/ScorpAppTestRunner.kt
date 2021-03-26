package com.ycagri.scorptask.util

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import com.ycagri.scorptask.TestApplication

/**
 * Custom runner to disable dependency injection.
 */
class ScorpAppTestRunner : AndroidJUnitRunner() {
    override fun newApplication(cl: ClassLoader, className: String, context: Context): Application {
        return super.newApplication(cl, TestApplication::class.java.name, context)
    }
}