package com.production.vedantwatersupply.ui.maintenance

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.production.vedantwatersupply.R
import com.production.vedantwatersupply.databinding.ItemMaintenanceBinding
import com.production.vedantwatersupply.listener.RecyclerViewClickListener
import com.production.vedantwatersupply.model.response.MaintenanceData
import com.production.vedantwatersupply.utils.formatPriceWithDecimal
import com.production.vedantwatersupply.utils.formatPriceWithoutDecimal

class MaintenanceAdapter(context: Context, private var maintenanceList: List<MaintenanceData>, val clickListener: RecyclerViewClickListener): RecyclerView.Adapter<MaintenanceAdapter.ViewHolder>() {

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

    override fun getItemCount() = maintenanceList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = maintenanceList[position]
        holder.binding.apply {
            tvTripCode.text = response.reference
            tvTruck.text = response.tanker?.tankerNumber
            tvMaintenanceAmount.text = response.amount.toString().formatPriceWithDecimal()
            tvScheduledDate.text = response.dateReadable.toString()
        }
    }

    fun getItemAt(pos: Int): MaintenanceData {
        return maintenanceList[pos]
    }
}