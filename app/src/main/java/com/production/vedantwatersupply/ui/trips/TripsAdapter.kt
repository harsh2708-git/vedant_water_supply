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
import com.production.vedantwatersupply.model.response.DriverData
import com.production.vedantwatersupply.model.response.TripData
import com.production.vedantwatersupply.utils.AppConstants.CANCELLED
import com.production.vedantwatersupply.utils.CommonUtils
import com.production.vedantwatersupply.utils.formatPriceWithDecimal
import java.util.Date

class TripsAdapter(context: Context, private var tripDataList: ArrayList<TripData>, val clickListener: RecyclerViewClickListener) : RecyclerView.Adapter<TripsAdapter.ViewHolder>() {

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

    override fun getItemCount() = tripDataList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = tripDataList[position]
        holder.binding.apply {
            tvTripCode.text = response.reference
            tvStatus.text = response.status

            tvStatus.visibility = if (response.status?.isEmpty() == true) View.GONE else View.VISIBLE
            tvTruck.text = response.tanker?.tankerNumber
            tvDriver.text = response.driver?.driverName
            tvFuelAmount.text = response.fuelAmount.toString().formatPriceWithDecimal()
            tvScheduledDate.text = response.tripDateReadable.toString()
            tvFilledSiteName.text = response.fillingSite
            tvDestination.text = response.destinationSite

            ivTripOptions.visibility = if (response.status?.equals(CANCELLED) == true) View.GONE else View.VISIBLE

        }
    }

    fun getItemAt(position: Int): TripData {
        return tripDataList[position]
    }

    fun addRecords(data: List<TripData>) {
        val lastPos: Int = tripDataList.size
        tripDataList.clear()
        tripDataList.addAll(data)
        notifyDataSetChanged()
    }
}