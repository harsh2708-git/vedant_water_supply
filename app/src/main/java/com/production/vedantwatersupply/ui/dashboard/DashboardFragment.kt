package com.production.vedantwatersupply.ui.dashboard

import android.content.Intent
import android.view.View
import com.production.vedantwatersupply.R
import com.production.vedantwatersupply.core.BaseFragment
import com.production.vedantwatersupply.databinding.FragmentDashboardBinding
import com.production.vedantwatersupply.ui.driver.DriverAdapter
import com.production.vedantwatersupply.ui.login.LoginActivity
import com.production.vedantwatersupply.ui.maintenance.MaintenanceAdapter
import com.production.vedantwatersupply.ui.trips.TripsAdapter

class DashboardFragment : BaseFragment<FragmentDashboardBinding, DashboardViewModel>(), View.OnClickListener {

    override val layoutId: Int
        get() = R.layout.fragment_dashboard

    override fun getViewModel(): Class<DashboardViewModel> = DashboardViewModel::class.java

    override fun init() {
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
        val tripAdapter = TripsAdapter(requireContext())
        binding?.rvTrips?.adapter = tripAdapter
    }

    private fun setMaintenanceAdpter() {
        val maintenanceAdapter = MaintenanceAdapter(requireContext())
        binding?.rvMaintanance?.adapter = maintenanceAdapter
    }

    private fun setDriverAdapter() {
        val driverAdapter = DriverAdapter(requireContext())
        binding?.rvDrivers?.adapter = driverAdapter
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fabLogout -> baseActivity?.navigateActivity(Intent(requireContext(), LoginActivity::class.java))

            R.id.llTrip,
            R.id.tvViewAllTrips,
            R.id.ivViewAllTrips -> {
                navigateFragment(v, R.id.nav_trip_listing)
            }

            R.id.llMaintanance,
            R.id.tvViewAllTripsMaintanance,
            R.id.ivViewAllMaintanance -> {
                navigateFragment(v, R.id.nav_maintenance_listing)
            }

            R.id.llDrivers,
            R.id.tvViewAllTripsDrivers,
            R.id.ivViewAllDrivers -> {
                navigateFragment(v, R.id.nav_driver_listing)
            }

            R.id.ivUp -> {}
        }
    }

}