package com.ycagri.scorptask

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.ycagri.scorptask.utils.AppExecutors
import com.ycagri.scorptask.utils.ViewModelFactory
import dagger.android.AndroidInjection
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var layoutManager: RecyclerView.LayoutManager

    lateinit var adapter: MainAdapter

    @Inject
    lateinit var itemDecoration: RecyclerView.ItemDecoration

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var appExecutors: AppExecutors

    private val viewModel: MainViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        setContentView(R.layout.activity_main)

        adapter = MainAdapter(appExecutors = appExecutors)

        val rvPeople = findViewById<RecyclerView>(R.id.rv_people)
        rvPeople.setHasFixedSize(true)
        rvPeople.layoutManager = layoutManager
        rvPeople.addItemDecoration(itemDecoration)
        rvPeople.adapter = adapter
        rvPeople.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val p =
                    (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                if (p >= adapter.itemCount - 5)
                    viewModel.loadMore()
            }
        })

        viewModel.people.observe(this) {
            adapter.submitList(it)
        }

        val swRefresh = findViewById<SwipeRefreshLayout>(R.id.sw_refresh)
        viewModel.refresh.observe(this) {
            swRefresh.isRefreshing = it
        }

        swRefresh.setOnRefreshListener {
            viewModel.refresh.postValue(true)
        }

        viewModel.loadMore()
    }
}