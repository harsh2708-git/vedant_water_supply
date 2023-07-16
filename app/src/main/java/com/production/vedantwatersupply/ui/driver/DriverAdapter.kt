package com.production.vedantwatersupply.ui.driver

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.production.vedantwatersupply.R
import com.production.vedantwatersupply.databinding.ItemDriverBinding
import com.production.vedantwatersupply.databinding.ItemMaintenanceBinding
import com.production.vedantwatersupply.databinding.ItemTripBinding

class DriverAdapter(context: Context): RecyclerView.Adapter<DriverAdapter.ViewHolder>() {


    inner class ViewHolder(val binding : ItemDriverBinding): RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemDriverBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_driver, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = 3

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }
}