package com.production.vedantwatersupply.ui.dashboard

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import com.production.vedantwatersupply.R
import com.production.vedantwatersupply.core.BaseFragment
import com.production.vedantwatersupply.databinding.FragmentDashboardBinding
import com.production.vedantwatersupply.databinding.LayoutOptionsBinding
import com.production.vedantwatersupply.listener.RecyclerViewClickListener
import com.production.vedantwatersupply.ui.driver.DriverAdapter
import com.production.vedantwatersupply.ui.login.LoginActivity
import com.production.vedantwatersupply.ui.maintenance.MaintenanceAdapter
import com.production.vedantwatersupply.ui.trips.TripsAdapter

class DashboardFragment : BaseFragment<FragmentDashboardBinding, DashboardViewModel>(), View.OnClickListener, RecyclerViewClickListener {

    override val layoutId: Int
        get() = R.layout.fragment_dashboard

    override fun getViewModel(): Class<DashboardViewModel> = DashboardViewModel::class.java

    override fun init() {

        binding?.clHeader?.ivBack?.visibility = View.GONE
        setTripsAdapter()
        setMaintenanceAdpter()
        setDriverAdapter()
    }

    override fun initListener() {
        binding?.fabLogout?.setOnClickListener(this)
        binding?.llTrip?.setOnClickListener(this)
        binding?.llMaintanance?.setOnClickListener(this)
        binding?.llDrivers?.setOnClickListener(this)
        binding?.tvViewAllTrips?.setOnClickListener(this)
        binding?.ivViewAllTrips?.setOnClickListener(this)
        binding?.tvViewAllTripsMaintanance?.setOnClickListener(this)
        binding?.ivViewAllMaintanance?.setOnClickListener(this)
        binding?.tvViewAllTripsDrivers?.setOnClickListener(this)
        binding?.ivViewAllDrivers?.setOnClickListener(this)
        binding?.ivUp?.setOnClickListener(this)
    }

    override fun addObserver() {}

    private fun setTripsAdapter() {
        val tripAdapter = TripsAdapter(requireContext(), this)
        binding?.rvTrips?.adapter = tripAdapter
    }

    private fun setMaintenanceAdpter() {
        val maintenanceAdapter = MaintenanceAdapter(requireContext(), this)
        binding?.rvMaintanance?.adapter = maintenanceAdapter
    }

    private fun setDriverAdapter() {
        val driverAdapter = DriverAdapter(requireContext(), this)
        binding?.rvDrivers?.adapter = driverAdapter
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fabLogout -> baseActivity?.navigateActivity(Intent(requireContext(), LoginActivity::class.java))

            R.id.llTrip,
            R.id.tvViewAllTrips,
            R.id.ivViewAllTrips -> navigateFragment(v, R.id.nav_trip_listing)


            R.id.llMaintanance,
            R.id.tvViewAllTripsMaintanance,
            R.id.ivViewAllMaintanance -> navigateFragment(v, R.id.nav_maintenance_listing)

            R.id.llDrivers,
            R.id.tvViewAllTripsDrivers,
            R.id.ivViewAllDrivers -> navigateFragment(v, R.id.nav_driver_listing)

            R.id.ivUp -> {}
        }
    }

    override fun onClick(view: View?, position: Int) {
        when (view?.id) {
            R.id.cvTripMain -> navigateFragment(view, R.id.nav_trip_detail)
            R.id.cvMaintenanceMain -> navigateFragment(view, R.id.nav_maintenance_detail)
            R.id.cvDriverMain -> navigateFragment(view, R.id.nav_driver_detail)
            R.id.ivTripOptions -> showTripOptionMenu(view)
            R.id.ivMaintenanceOptions -> showMaintenanceOptionMenu(view)
            R.id.ivDriverOptions -> showDriverOptionMenu(view)
        }
    }

    private fun showTripOptionMenu(view: View) {
        val inflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val vieww = inflater.inflate(R.layout.layout_options, null)
        val popupWindow = PopupWindow(vieww, requireContext().resources.getDimensionPixelSize(R.dimen._250sdp), LinearLayout.LayoutParams.WRAP_CONTENT, true)
        val binding: LayoutOptionsBinding = LayoutOptionsBinding.bind(vieww)
        popupWindow.showAsDropDown(view)

        binding.llCancel.visibility = View.VISIBLE
        binding.tvEdit.text = getString(R.string.edit_trip)
        binding.tvDelete.text = getString(R.string.delete_trip)

        binding.llEdit.setOnClickListener {
            popupWindow.dismiss()
        }

        binding.llDelete.setOnClickListener {
            popupWindow.dismiss()
        }

        binding.llCancel.setOnClickListener {
            popupWindow.dismiss()
        }
    }

    private fun showMaintenanceOptionMenu(view: View) {
        val inflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val vieww = inflater.inflate(R.layout.layout_options, null)
        val popupWindow = PopupWindow(vieww, requireContext().resources.getDimensionPixelSize(R.dimen._250sdp), LinearLayout.LayoutParams.WRAP_CONTENT, true)
        val binding: LayoutOptionsBinding = LayoutOptionsBinding.bind(vieww)
        popupWindow.showAsDropDown(view)

        binding.llCancel.visibility = View.GONE
        binding.tvEdit.text = getString(R.string.edit_maintenance)
        binding.tvDelete.text = getString(R.string.delete_maintenance)

        binding.llEdit.setOnClickListener {
            popupWindow.dismiss()
        }

        binding.llDelete.setOnClickListener {
            popupWindow.dismiss()
        }
    }

    private fun showDriverOptionMenu(view: View) {
        val inflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val vieww = inflater.inflate(R.layout.layout_options, null)
        val popupWindow = PopupWindow(vieww, requireContext().resources.getDimensionPixelSize(R.dimen._250sdp), LinearLayout.LayoutParams.WRAP_CONTENT, true)
        val binding: LayoutOptionsBinding = LayoutOptionsBinding.bind(vieww)
        popupWindow.showAsDropDown(view)

        binding.llCancel.visibility = View.GONE
        binding.tvEdit.text = getString(R.string.edit_driver_expense)
        binding.tvDelete.text = getString(R.string.delete_driver_expense)

        binding.llEdit.setOnClickListener {
            popupWindow.dismiss()
        }

        binding.llDelete.setOnClickListener {
            popupWindow.dismiss()
        }
    }

}