package com.production.vedantwatersupply.ui.trips

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.production.vedantwatersupply.R
import com.production.vedantwatersupply.databinding.ItemTripBinding

class TripsAdapter(context: Context): RecyclerView.Adapter<TripsAdapter.ViewHolder>() {


    inner class ViewHolder(val binding : ItemTripBinding): RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemTripBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_trip, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = 3

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }
}