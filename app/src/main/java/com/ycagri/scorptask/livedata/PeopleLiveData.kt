package com.ycagri.scorptask.livedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.ycagri.scorptask.datasource.*

class PeopleLiveData(
    private val dataSource: DataSource,
    val load: MutableLiveData<Boolean>,
    val refresh: MutableLiveData<Boolean>,
    val error: MutableLiveData<String?>
) :
    MutableLiveData<List<Person>>() {

    val loadObserver = Observer<Boolean> { t ->
        if (t == true) {
            dataSource.fetch(next, callback)
        }
    }

    val refreshObserver = Observer<Boolean> { t ->
        if (t == true) {
            userMap.clear()
            next = null
            dataSource.fetch(next, callback)
        }
    }

    var next: String? = null

    val userMap = LinkedHashMap<Int, Person>()

    val callback = object : FetchCompletionHandler {
        override fun invoke(response: FetchResponse?, e: FetchError?) {
            if (response != null) {
                for (p in response.people)
                    userMap[p.id] = p

                value = userMap.values.toList()
                next = response.next
            } else {
                error.value = e?.errorDescription
            }

            refresh.value = false
            load.value = false
        }
    }

    override fun onActive() {
        super.onActive()
        load.observeForever(loadObserver)
        refresh.observeForever(refreshObserver)
    }

    override fun onInactive() {
        load.removeObserver(loadObserver)
        refresh.removeObserver(refreshObserver)
        super.onInactive()
    }
}