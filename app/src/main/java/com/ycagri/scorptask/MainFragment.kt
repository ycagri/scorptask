package com.ycagri.scorptask

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.ycagri.scorptask.di.Injectable
import com.ycagri.scorptask.utils.AppExecutors
import javax.inject.Inject

class MainFragment : Fragment(), Injectable {

    lateinit var adapter: MainAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    private val viewModel: MainViewModel by viewModels { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MainAdapter(appExecutors = appExecutors)

        val emptyTextTV: View = view.findViewById(R.id.tv_empty_text)

        val rvPeople = view.findViewById<RecyclerView>(R.id.rv_people)
        rvPeople.setHasFixedSize(true)
        rvPeople.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        rvPeople.adapter = adapter
        rvPeople.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val p =
                    (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                if (p >= adapter.itemCount - 5)
                    viewModel.setLoad(true)
            }
        })

        viewModel.people.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        val swRefresh = view.findViewById<SwipeRefreshLayout>(R.id.sw_refresh)
        viewModel.load.observe(viewLifecycleOwner) {
            swRefresh.isRefreshing = it
        }

        viewModel.error.observe(viewLifecycleOwner) {
            Snackbar.make(view, it ?: "Unknown Error", Snackbar.LENGTH_LONG).show()
        }

        viewModel.emptyText.observe(viewLifecycleOwner) {
            if (it) {
                rvPeople.visibility = View.GONE
                emptyTextTV.visibility = View.VISIBLE
            } else {
                rvPeople.visibility = View.VISIBLE
                emptyTextTV.visibility = View.GONE
            }
        }

        swRefresh.setOnRefreshListener {
            viewModel.setRefresh(true)
        }

        viewModel.setLoad(true)
    }
}