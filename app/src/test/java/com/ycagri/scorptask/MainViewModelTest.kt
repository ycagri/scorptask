package com.ycagri.scorptask

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.ycagri.scorptask.datasource.DataSource
import com.ycagri.scorptask.datasource.FetchError
import com.ycagri.scorptask.datasource.FetchResponse
import com.ycagri.scorptask.datasource.Person
import com.ycagri.scorptask.util.TestUtil
import com.ycagri.scorptask.util.any
import com.ycagri.scorptask.util.mock
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.*

@RunWith(JUnit4::class)
class MainViewModelTest {

    @Rule
    @JvmField
    val instantTaskExecutor = InstantTaskExecutorRule()

    private val dataSource = mock(DataSource::class.java)

    private val viewModel = MainViewModel(dataSource)

    @Test
    fun testNotNull() {
        assertThat(viewModel.dataSource, notNullValue())
        verify(dataSource, never()).fetch(anyString(), any())
        viewModel.setLoad(true)
        verify(dataSource, never()).fetch(anyString(), any())
        viewModel.setRefresh(true)
        verify(dataSource, never()).fetch(anyString(), any())
    }

    @Test
    fun testLoad() {
        viewModel.people.observeForever(mock())
        viewModel.setLoad(true)
        verify(dataSource).fetch(eq(null), any())
    }

    @Test
    fun testRefresh() {
        viewModel.people.observeForever(mock())
        viewModel.setRefresh(true)
        verify(dataSource).fetch(eq(null), any())
    }

    @Test
    fun loadSendToUiTest() {
        val people = TestUtil.createPeopleList()
        `when`(dataSource.fetch(null, viewModel.people.callback)).then {
            viewModel.people.callback.invoke(FetchResponse(people, "next"), null)
        }

        val observer = mock<Observer<List<Person>>>()
        viewModel.people.observeForever(observer)

        val emptyObserver = mock<Observer<Boolean>>()
        viewModel.emptyText.observeForever(emptyObserver)

        val errorObserver = mock<Observer<String?>>()
        viewModel.error.observeForever(errorObserver)

        viewModel.setLoad(true)
        verify(observer).onChanged(people)
        verify(emptyObserver).onChanged(false)
        verify(errorObserver, never()).onChanged(anyString())
    }

    @Test
    fun emptyLoadSendToUiTest() {
        val people = emptyList<Person>()
        `when`(dataSource.fetch(null, viewModel.people.callback)).then {
            viewModel.people.callback.invoke(FetchResponse(people, "next"), null)
        }

        val observer = mock<Observer<List<Person>>>()
        viewModel.people.observeForever(observer)

        val emptyObserver = mock<Observer<Boolean>>()
        viewModel.emptyText.observeForever(emptyObserver)

        val errorObserver = mock<Observer<String?>>()
        viewModel.error.observeForever(errorObserver)

        viewModel.setLoad(true)
        verify(observer).onChanged(people)
        verify(emptyObserver).onChanged(true)
        verify(errorObserver, never()).onChanged(anyString())
    }

    @Test
    fun errorSendToUiTest() {
        `when`(dataSource.fetch(null, viewModel.people.callback)).then {
            viewModel.people.callback.invoke(null, FetchError("Test Error"))
        }

        val observer = mock<Observer<List<Person>>>()
        viewModel.people.observeForever(observer)

        val emptyObserver = mock<Observer<Boolean>>()
        viewModel.emptyText.observeForever(emptyObserver)

        val errorObserver = mock<Observer<String?>>()
        viewModel.error.observeForever(errorObserver)

        viewModel.setLoad(true)
        verify(observer, never()).onChanged(anyList())
        verify(emptyObserver, never()).onChanged(anyBoolean())
        verify(errorObserver).onChanged("Test Error")
    }
}