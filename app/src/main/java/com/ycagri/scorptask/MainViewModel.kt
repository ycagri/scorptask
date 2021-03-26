package com.ycagri.scorptask

import androidx.lifecycle.*
import com.ycagri.scorptask.datasource.*
import com.ycagri.scorptask.livedata.PeopleLiveData
import com.ycagri.scorptask.testing.OpenForTesting
import javax.inject.Inject

@OpenForTesting
class MainViewModel @Inject constructor(val dataSource: DataSource) : ViewModel() {

    private val _load = MutableLiveData(false)

    private val _error = MutableLiveData<String?>()

    private val _refresh = MutableLiveData(false)

    val people = PeopleLiveData(dataSource, load = _load, refresh = _refresh, error = _error)

    val emptyText: LiveData<Boolean> = people.map {
        return@map it.isEmpty()
    }

    val load: LiveData<Boolean>
        get() = _load

    val error: LiveData<String?>
        get() = _error

    fun setLoad(load: Boolean) {
        if (people.isDone()) {
            _load.value = false
        } else if (_load.value != load) {
            _load.value = load
        }
    }

    fun setRefresh(refresh: Boolean) {
        if (_refresh.value != refresh) {
            _refresh.value = refresh
        }
    }
}