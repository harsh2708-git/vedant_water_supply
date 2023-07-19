package com.production.vedantwatersupply.ui.trips

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.production.vedantwatersupply.R
import com.production.vedantwatersupply.listener.RecyclerViewClickListener
import com.production.vedantwatersupply.databinding.ItemTripBinding

class TripsAdapter(context: Context, val clickListener: RecyclerViewClickListener) : RecyclerView.Adapter<TripsAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemTripBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            binding.cvTripMain.setOnClickListener(this)
            binding.ivTripOptions.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            when (v?.id) {
                R.id.ivTripOptions,
                R.id.cvTripMain -> clickListener.onClick(v, adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemTripBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_trip, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = 3

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }
}