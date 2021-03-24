package com.ycagri.scorptask

import androidx.lifecycle.*
import com.ycagri.scorptask.datasource.*
import com.ycagri.scorptask.utils.AbsentLiveData
import javax.inject.Inject

class MainViewModel @Inject constructor(private val dataSource: DataSource) : ViewModel() {

    private var next: String? = null

    private val userMap = LinkedHashMap<Int, Person>()

    val refresh = MutableLiveData(false)

    private val _error = MutableLiveData<String?>()

    val _people = MutableLiveData<List<Person>>()

    val people: LiveData<List<Person>> = refresh.switchMap {
        if (it) {
            userMap.clear()
            return@switchMap AbsentLiveData.create()
        } else {
            return@switchMap _people
        }
    }

    val error: LiveData<String> = Transformations.switchMap(_error) {
        if (it == null) {
            return@switchMap AbsentLiveData.create()
        } else {
            return@switchMap _error
        }
    }

    fun loadMore() {
        dataSource.fetch(next, object : FetchCompletionHandler {
            override fun invoke(response: FetchResponse?, error: FetchError?) {
                if (response != null) {
                    for (p in response.people)
                        userMap[p.id] = p

                    _people.postValue(userMap.values.toList())
                    refresh.value = false
                    next = response.next
                } else {
                    _error.postValue(error?.errorDescription)
                }
            }
        })
    }
}