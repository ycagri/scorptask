package com.ycagri.scorptask

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.recyclerview.widget.RecyclerView
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

    private val viewModel: MainActivity by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        setContentView(R.layout.activity_main)

        adapter = MainAdapter(appExecutors = appExecutors)

        val rv_people = findViewById<RecyclerView>(R.id.rv_people)
        rv_people.setHasFixedSize(true)
        rv_people.layoutManager = layoutManager
        rv_people.addItemDecoration(itemDecoration)
        rv_people.adapter = adapter
    }
}