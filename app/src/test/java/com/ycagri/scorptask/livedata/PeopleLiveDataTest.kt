package com.ycagri.scorptask.livedata

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.ycagri.scorptask.datasource.DataSource
import com.ycagri.scorptask.datasource.FetchError
import com.ycagri.scorptask.datasource.FetchResponse
import com.ycagri.scorptask.util.TestUtil
import com.ycagri.scorptask.util.mock
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.*

@RunWith(JUnit4::class)
class PeopleLiveDataTest {

    @Rule
    @JvmField
    val instantTaskExecutor = InstantTaskExecutorRule()

    private val dataSource = mock(DataSource::class.java)

    private val load: MutableLiveData<Boolean> = mock()

    private val refresh: MutableLiveData<Boolean> = mock()

    private val error: MutableLiveData<String?> = mock()

    private val liveData = PeopleLiveData(dataSource, load, refresh, error)

    @Test
    fun loadTrueTest() {
        `when`(dataSource.fetch(null, liveData.callback)).then {
            liveData.callback.invoke(FetchResponse(TestUtil.createPeopleList(), "next"), null)
        }

        `when`(load.postValue(true)).then {
            liveData.loadObserver.onChanged(true)
        }

        load.postValue(true)
        verify(dataSource).fetch(null, liveData.callback)
        verify(refresh).value = false
        verify(load).value = false
        Assert.assertEquals("next", liveData.next)
        Assert.assertEquals(2, liveData.userMap.size)
    }

    @Test
    fun loadMoreTest() {
        `when`(dataSource.fetch(null, liveData.callback)).then {
            liveData.callback.invoke(FetchResponse(TestUtil.createPeopleList(), "next"), null)
        }

        `when`(dataSource.fetch("next", liveData.callback)).then {
            liveData.callback.invoke(FetchResponse(TestUtil.createPeopleList(), null), null)
        }

        `when`(load.setValue(true)).then {
            liveData.loadObserver.onChanged(true)
        }

        load.value = true
        verify(dataSource).fetch(null, liveData.callback)
        Assert.assertEquals("next", liveData.next)
        Assert.assertEquals(2, liveData.userMap.size)

        load.value = true
        verify(dataSource).fetch("next", liveData.callback)
        Assert.assertEquals(PeopleLiveData.DONE, liveData.next)
        Assert.assertEquals(2, liveData.userMap.size)

        verify(refresh, times(2)).value = false
        verify(load, times(2)).value = false
    }

    @Test
    fun refreshTrueTest() {
        `when`(dataSource.fetch(null, liveData.callback)).then {
            liveData.callback.invoke(FetchResponse(TestUtil.createPeopleList(), "next"), null)
        }

        `when`(load.postValue(true)).then {
            liveData.loadObserver.onChanged(true)
        }

        `when`(refresh.postValue(true)).then {
            liveData.refreshObserver.onChanged(true)
        }

        refresh.postValue(true)
        verify(dataSource).fetch(null, liveData.callback)
        verify(refresh).value = false
        verify(load).value = false
        Assert.assertEquals("next", liveData.next)
        Assert.assertEquals(2, liveData.userMap.size)
    }

    @Test
    fun loadErrorTest() {
        `when`(dataSource.fetch(null, liveData.callback)).then {
            liveData.callback.invoke(null, FetchError("Test Error"))
        }

        `when`(load.postValue(true)).then {
            liveData.loadObserver.onChanged(true)
        }

        load.postValue(true)
        verify(dataSource).fetch(null, liveData.callback)
        verify(error).value = "Test Error"
    }
}