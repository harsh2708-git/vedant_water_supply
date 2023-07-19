package com.production.vedantwatersupply.ui.maintenance

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.production.vedantwatersupply.R
import com.production.vedantwatersupply.databinding.ItemMaintenanceBinding
import com.production.vedantwatersupply.databinding.ItemTripBinding
import com.production.vedantwatersupply.listener.RecyclerViewClickListener

class MaintenanceAdapter(context: Context, val clickListener: RecyclerViewClickListener): RecyclerView.Adapter<MaintenanceAdapter.ViewHolder>() {

    inner class ViewHolder(val binding : ItemMaintenanceBinding): RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            binding.cvMaintenanceMain.setOnClickListener(this)
            binding.ivMaintenanceOptions.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            when (v?.id) {
                R.id.ivMaintenanceOptions,
                R.id.cvMaintenanceMain -> clickListener.onClick(v, adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemMaintenanceBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_maintenance, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = 3

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }
}