package com.yeongil.focusaid.utils.recyclerViewUtils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapter(private val parentLifecycle: LifecycleOwner? = null) :
    ListAdapter<RecyclerItem, RecyclerViewAdapter.BindingViewHolder>(DiffCallback()) {
    override fun getItemViewType(position: Int): Int {
        return getItem(position).layoutId
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ViewDataBinding = DataBindingUtil.inflate(inflater, viewType, parent, false)
        if (parentLifecycle != null) binding.lifecycleOwner = parentLifecycle

        return BindingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
        getItem(position).bind(holder.binding)
        holder.binding.executePendingBindings()
    }

    class BindingViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root)

    class DiffCallback : DiffUtil.ItemCallback<RecyclerItem>() {
        override fun areItemsTheSame(oldItem: RecyclerItem, newItem: RecyclerItem): Boolean {
            val oldData = oldItem.viewModel
            val newData = newItem.viewModel

            return if (oldData is RecyclerItemViewModel && newData is RecyclerItemViewModel) {
                oldData.isSameItem(newData)
            } else oldData == newData
        }

        override fun areContentsTheSame(oldItem: RecyclerItem, newItem: RecyclerItem): Boolean {
            val oldData = oldItem.viewModel
            val newData = newItem.viewModel

            return if (oldData is RecyclerItemViewModel && newData is RecyclerItemViewModel) {
                oldData.isSameItem(newData)
            } else oldItem == newItem
        }
    }
}