package com.production.vedantwatersupply.ui.maintenance

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.production.vedantwatersupply.R
import com.production.vedantwatersupply.databinding.ItemMaintenanceBinding
import com.production.vedantwatersupply.databinding.ItemTripBinding

class MaintenanceAdapter(context: Context): RecyclerView.Adapter<MaintenanceAdapter.ViewHolder>() {


    inner class ViewHolder(val binding : ItemMaintenanceBinding): RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemMaintenanceBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_maintenance, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = 3

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }
}