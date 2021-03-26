package com.ycagri.scorptask

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.ycagri.scorptask.databinding.ItemPersonBinding
import com.ycagri.scorptask.datasource.Person
import com.ycagri.scorptask.utils.AppExecutors
import com.ycagri.scorptask.utils.DataBoundListAdapter
import javax.inject.Inject

class MainAdapter @Inject constructor(appExecutors: AppExecutors) :
    DataBoundListAdapter<Person, ItemPersonBinding>(appExecutors,
        object : DiffUtil.ItemCallback<Person>() {
            override fun areItemsTheSame(
                oldItem: Person,
                newItem: Person
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Person,
                newItem: Person
            ): Boolean {
                return oldItem.fullName == newItem.fullName
            }
        }) {

    override fun createBinding(parent: ViewGroup): ItemPersonBinding {
        return ItemPersonBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
    }

    override fun bind(binding: ItemPersonBinding, item: Person) {
        binding.person = item
    }
}
