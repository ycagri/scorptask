package com.ycagri.scorptask

import androidx.lifecycle.ViewModel
import com.ycagri.scorptask.datasource.DataSource
import javax.inject.Inject

class MainViewModel @Inject constructor(private val dataSource: DataSource): ViewModel() {
}