package com.ycagri.scorptask

import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ycagri.scorptask.datasource.DataSource
import com.ycagri.scorptask.livedata.PeopleLiveData
import com.ycagri.scorptask.util.CountingAppExecutorsRule
import com.ycagri.scorptask.util.RecyclerViewMatcher
import com.ycagri.scorptask.util.SwipeRefreshLayoutMatchers.isRefreshing
import com.ycagri.scorptask.util.TestUtil
import com.ycagri.scorptask.util.ViewModelUtil
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

@RunWith(AndroidJUnit4::class)
class MainFragmentTest {

    @Rule
    @JvmField
    val executorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    val countingAppExecutors = CountingAppExecutorsRule()

    private lateinit var viewModel: MainViewModel

    private val dataSource = mock(DataSource::class.java)

    private val people =
        PeopleLiveData(dataSource, MutableLiveData(), MutableLiveData(), MutableLiveData())

    private val load = MutableLiveData<Boolean>()

    private val error = MutableLiveData<String?>()

    private val emptyText = MutableLiveData<Boolean>()

    @Before
    fun setup() {
        viewModel = mock(MainViewModel::class.java)
        `when`(viewModel.people).thenReturn(people)
        `when`(viewModel.load).thenReturn(load)
        `when`(viewModel.error).thenReturn(error)
        `when`(viewModel.emptyText).thenReturn(emptyText)
        `when`(viewModel.setLoad(true)).then {
            if(people.isDone())
                load.postValue(false) else load.postValue(true)
        }
        `when`(viewModel.setLoad(false)).then { load.postValue(false) }

        launchFragmentInContainer(Bundle(), R.style.Theme_AppCompat) {
            MainFragment().apply {
                appExecutors = countingAppExecutors.appExecutors
                viewModelFactory = ViewModelUtil.createFor(viewModel)
            }
        }
    }

    @Test
    fun testLoaded() {
        val p = TestUtil.createPeopleList()
        people.postValue(p)

        for (pos in p.indices) {
            val text = "${p[pos].fullName} (${p[pos].id})"
            onView(listMatcher().atPosition(pos)).apply {
                check(ViewAssertions.matches(withText(text)))
            }
        }
    }

    @Test
    fun testEmptyTest() {
        emptyText.postValue(true)

        onView(withId(R.id.tv_empty_text)).check(ViewAssertions.matches(isDisplayed()))
    }

    @Test
    fun onError() {
        error.postValue("Test Error")

        onView(
            allOf(
                withId(com.google.android.material.R.id.snackbar_text),
                withText("Test Error")
            )
        )
            .check(ViewAssertions.matches(isDisplayed()))
    }

    @Test
    fun swipeIndicatorTest() {
        onView(withId(R.id.sw_refresh)).check(ViewAssertions.matches(isRefreshing()))

        viewModel.setLoad(false)
        onView(withId(R.id.sw_refresh)).check(ViewAssertions.matches(not(isRefreshing())))
    }

    @Test
    fun loadWithErrorTest() {
        onView(withId(R.id.sw_refresh)).check(ViewAssertions.matches(isRefreshing()))

        error.postValue("Test Error")
        onView(withId(R.id.sw_refresh)).check(ViewAssertions.matches(not(isRefreshing())))
    }

    @Test
    fun loadWithFewTest() {
        onView(withId(R.id.sw_refresh)).check(ViewAssertions.matches(isRefreshing()))

        people.next = PeopleLiveData.DONE
        people.postValue(TestUtil.createPeopleList())
        onView(withId(R.id.sw_refresh)).check(ViewAssertions.matches(not(isRefreshing())))
    }

    private fun listMatcher() = RecyclerViewMatcher(R.id.rv_people)
}