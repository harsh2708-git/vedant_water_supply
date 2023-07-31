package com.production.vedantwatersupply.ui.driver

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.production.vedantwatersupply.R
import com.production.vedantwatersupply.databinding.ItemDriverBinding
import com.production.vedantwatersupply.listener.RecyclerViewClickListener
import com.production.vedantwatersupply.model.response.DriverData
import com.production.vedantwatersupply.utils.formatPriceWithoutDecimal

class DriverAdapter(private var context: Context, private var driverList: List<DriverData>, val clickListener: RecyclerViewClickListener) : RecyclerView.Adapter<DriverAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemDriverBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            binding.cvDriverMain.setOnClickListener(this)
            binding.ivDriverOptions.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            when (v?.id) {
                R.id.ivDriverOptions,
                R.id.cvDriverMain -> clickListener.onClick(v, adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemDriverBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_driver, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = driverList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = driverList[position]

        holder.binding.apply {
            tvTripCode.text = response.reference
            tvDriver.text = response.driver?.driverName
            tvAmount.text = response.amount?.toString()?.formatPriceWithoutDecimal()
            tvDate.text = response.dateReadable
        }
    }

    fun getItemAt(pos: Int) = driverList[pos]
}