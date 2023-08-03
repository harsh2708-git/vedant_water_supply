package com.production.vedantwatersupply.ui.dashboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import com.production.vedantwatersupply.R
import com.production.vedantwatersupply.core.BaseFragment
import com.production.vedantwatersupply.databinding.FragmentDashboardBinding
import com.production.vedantwatersupply.databinding.LayoutOptionsBinding
import com.production.vedantwatersupply.listener.RecyclerViewClickListener
import com.production.vedantwatersupply.model.request.DriverIdRequest
import com.production.vedantwatersupply.model.request.IdRequest
import com.production.vedantwatersupply.model.request.MaintenanceIdRequest
import com.production.vedantwatersupply.model.response.DashboardResponse
import com.production.vedantwatersupply.model.response.DriverData
import com.production.vedantwatersupply.model.response.MaintenanceData
import com.production.vedantwatersupply.model.response.TripData
import com.production.vedantwatersupply.ui.dialog.AlertDialogFragment
import com.production.vedantwatersupply.ui.driver.DriverAdapter
import com.production.vedantwatersupply.ui.login.LoginActivity
import com.production.vedantwatersupply.ui.maintenance.MaintenanceAdapter
import com.production.vedantwatersupply.ui.trips.TripsAdapter
import com.production.vedantwatersupply.utils.AppConstants
import com.production.vedantwatersupply.utils.AppConstants.Bundle.Companion.ARG_DRIVER_ID
import com.production.vedantwatersupply.utils.AppConstants.Bundle.Companion.ARG_MAINTENANCE_ID
import com.production.vedantwatersupply.utils.CommonUtils
import com.production.vedantwatersupply.utils.SharedPreferenceUtil
import com.production.vedantwatersupply.utils.UserUtils
import com.production.vedantwatersupply.utils.formatPriceWithDecimal
import com.production.vedantwatersupply.utils.formatPriceWithoutDecimal
import com.production.vedantwatersupply.webservice.baseresponse.WebServiceSetting

class DashboardFragment : BaseFragment<FragmentDashboardBinding, DashboardViewModel>(), View.OnClickListener, RecyclerViewClickListener {

    private var tripAdapter: TripsAdapter? = null
    private var maintenanceAdapter: MaintenanceAdapter? = null
    private var dashboardResponse: DashboardResponse? = null
    private var driverAdapter: DriverAdapter? = null

    override val layoutId: Int
        get() = R.layout.fragment_dashboard

    override fun getViewModel(): Class<DashboardViewModel> = DashboardViewModel::class.java

    override fun init() {

        binding?.clHeader?.ivBack?.visibility = View.GONE
//        setTripsAdapter()
//        setMaintenanceAdpter()
//        setDriverAdapter()

        callDashboardApi()
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

        binding?.nsView?.viewTreeObserver?.addOnScrollChangedListener {
            val scrollY = binding?.nsView?.scrollY
            if (scrollY != null)
                binding?.ivUp?.visibility = if (scrollY > 0) View.VISIBLE else View.GONE
        }

        binding?.srlDashboard?.setOnRefreshListener {
            callDashboardApi()
        }
    }

    private fun callDashboardApi() {
        baseActivity?.showProgress()
        viewModel?.callDashboardApi()
    }

    override fun addObserver() {
        viewModel?.dashboardRepository?.dashboardResponseLiveData?.observe(this) {
            baseActivity?.hideProgress()
            when (it?.webServiceSetting?.success) {
                WebServiceSetting.SUCCESS -> {
                    binding?.srlDashboard?.isRefreshing = false
                    dashboardResponse = it
//                    CommonUtils.showToast(requireContext(), it.webServiceSetting?.message)
                    updateUI()
                }

                WebServiceSetting.FAILURE -> {
                    CommonUtils.showToast(requireContext(), it.webServiceSetting?.message)
                }

                WebServiceSetting.NO_INTERNET -> {
                    CommonUtils.showToast(requireContext(), getString(R.string.no_internet_title))
                }
            }
        }

        viewModel?.tripRepository?.tripDeleteResponseMutableLiveData?.observe(this) {
            when (it.webServiceSetting?.success) {
                WebServiceSetting.SUCCESS -> {
                    CommonUtils.showToast(requireContext(), it.webServiceSetting?.message)
                    callDashboardApi()
                }

                WebServiceSetting.FAILURE -> {
                    CommonUtils.showToast(requireContext(), it.webServiceSetting?.message)
                }

                WebServiceSetting.NO_INTERNET -> {
                    CommonUtils.showToast(requireContext(), getString(R.string.no_internet_title))
                }
            }
            hideProgress()
        }

        viewModel?.tripRepository?.tripCancelResponseMutableLiveData?.observe(this) {
            when (it.webServiceSetting?.success) {
                WebServiceSetting.SUCCESS -> {
                    CommonUtils.showToast(requireContext(), it.webServiceSetting?.message)
                    callDashboardApi()
                }

                WebServiceSetting.FAILURE -> {
                    CommonUtils.showToast(requireContext(), it.webServiceSetting?.message)
                }

                WebServiceSetting.NO_INTERNET -> {
                    CommonUtils.showToast(requireContext(), getString(R.string.no_internet_title))
                }
            }
            hideProgress()
        }

        viewModel?.maintenanceRepository?.maintenanceDeleteResponseMutableLiveData?.observe(this) {
            when (it.webServiceSetting?.success) {
                WebServiceSetting.SUCCESS -> {
                    CommonUtils.showToast(requireContext(), it.webServiceSetting?.message)
                    callDashboardApi()
                }

                WebServiceSetting.FAILURE -> {
                    CommonUtils.showToast(requireContext(), it.webServiceSetting?.message)
                }

                WebServiceSetting.NO_INTERNET -> {
                    CommonUtils.showToast(requireContext(), getString(R.string.no_internet_title))
                }
            }
            hideProgress()
        }

        viewModel?.driverRepository?.deleteDriverExpenseResponseMutableLiveData?.observe(this) {
            when (it.webServiceSetting?.success) {
                WebServiceSetting.SUCCESS -> {
                    CommonUtils.showToast(requireContext(), it.webServiceSetting?.message)
                    callDashboardApi()
                }

                WebServiceSetting.FAILURE -> {
                    CommonUtils.showToast(requireContext(), it.webServiceSetting?.message)
                }

                WebServiceSetting.NO_INTERNET -> {
                    CommonUtils.showToast(requireContext(), getString(R.string.no_internet_title))
                }
            }
            hideProgress()
        }
    }

    private fun updateUI() {
        binding?.tvTotalTrip?.text = dashboardResponse?.totalTripsCount.toString().formatPriceWithoutDecimal()
        binding?.tvTotalFuelAmount?.text = getString(R.string.indian_rupee).plus(dashboardResponse?.totalTripsAmount.toString().formatPriceWithDecimal())
        binding?.tvTotalMaintanance?.text = dashboardResponse?.totalMaintainanceCount.toString().formatPriceWithoutDecimal()
        binding?.tvTotalMaintananceAmount?.text = getString(R.string.indian_rupee).plus(dashboardResponse?.totalMaintainanceAmount.toString().formatPriceWithDecimal())

        if (dashboardResponse?.tripData?.isEmpty() == true) {
            hideData()
        } else {
            showData()
            setTripsAdapter()
        }

        if (dashboardResponse?.maintainanceData?.isEmpty() == true) {
            hideMaintenanceData()
        } else {
            showMaintenanceData()
            setMaintenanceAdpter()
        }

        if (dashboardResponse?.driverExpenceData?.isEmpty() == true) {
            hideDriverData()
        } else {
            showDriverData()
            setDriverAdapter()
        }
    }

    private fun showData(){
        binding?.rvTrips?.visibility = View.VISIBLE
        binding?.tripNoData?.tvNoData?.visibility = View.GONE
    }

    private fun hideData(){
        binding?.rvTrips?.visibility = View.GONE
        binding?.tripNoData?.tvNoData?.visibility = View.VISIBLE
        binding?.tripNoData?.tvNoData?.text = getString(R.string.no_trips_data_found)
    }

    private fun showMaintenanceData(){
        binding?.rvMaintanance?.visibility = View.VISIBLE
        binding?.maintenanceNoData?.tvNoData?.visibility = View.GONE
    }

    private fun hideMaintenanceData(){
        binding?.rvMaintanance?.visibility = View.GONE
        binding?.maintenanceNoData?.tvNoData?.visibility = View.VISIBLE
        binding?.maintenanceNoData?.tvNoData?.text = getString(R.string.no_maintenance_data_found)
    }

    private fun showDriverData() {
        binding?.rvDrivers?.visibility = View.VISIBLE
        binding?.driverNoData?.tvNoData?.visibility = View.GONE
    }

    private fun hideDriverData() {
        binding?.rvDrivers?.visibility = View.GONE
        binding?.driverNoData?.tvNoData?.visibility = View.VISIBLE
        binding?.driverNoData?.tvNoData?.text = getString(R.string.no_maintenance_data_found)
    }

    private fun setTripsAdapter() {
        tripAdapter = dashboardResponse?.tripData?.let { TripsAdapter(requireContext(), it, this) }
        binding?.rvTrips?.adapter = tripAdapter
    }

    private fun setMaintenanceAdpter() {
        maintenanceAdapter = dashboardResponse?.maintainanceData?.let { MaintenanceAdapter(requireContext(), it, this) }
        binding?.rvMaintanance?.adapter = maintenanceAdapter
    }

    private fun setDriverAdapter() {
        driverAdapter = dashboardResponse?.driverExpenceData?.let { DriverAdapter(requireContext(), it, this) }
        binding?.rvDrivers?.adapter = driverAdapter
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fabLogout -> {
                CommonUtils.showAlert(childFragmentManager, R.drawable.ic_data_not_found, getString(R.string.logout), getString(R.string.confirmation_logout), getString(R.string.logout), getString(R.string.cancel), true, object : AlertDialogFragment.IAlertDialogFragment {
                    override fun onPositiveButtonClicked() {
                        UserUtils.resetUserData(requireContext())
                        SharedPreferenceUtil.clearData(requireContext())
                        baseActivity?.navigateActivity(Intent(requireContext(), LoginActivity::class.java))
                        baseActivity?.finishAffinity()
                    }

                    override fun onNegativeButtonClicked() {}
                })

            }

            R.id.llTrip,
            R.id.tvViewAllTrips,
            R.id.ivViewAllTrips -> navigateFragment(v, R.id.nav_trip_listing)


            R.id.llMaintanance,
            R.id.tvViewAllTripsMaintanance,
            R.id.ivViewAllMaintanance -> navigateFragment(v, R.id.nav_maintenance_listing)

            R.id.llDrivers,
            R.id.tvViewAllTripsDrivers,
            R.id.ivViewAllDrivers -> navigateFragment(v, R.id.nav_driver_listing)

            R.id.ivUp -> binding?.nsView?.smoothScrollTo(0, 0)
        }
    }

    override fun onClick(view: View?, position: Int) {
        val tripResponse = tripAdapter?.getItemAt(position)
        val maintenanceResponse = maintenanceAdapter?.getItemAt(position)
        val driverResponse = driverAdapter?.getItemAt(position)
        when (view?.id) {
            R.id.cvTripMain -> {
                val bundle = Bundle()
                bundle.putString(AppConstants.Bundle.ARG_TRIP_ID, tripResponse?.id)
                navigateFragment(view, R.id.nav_trip_detail, bundle)
            }

            R.id.cvMaintenanceMain ->{
                val bundle = Bundle()
                bundle.putString(ARG_MAINTENANCE_ID, maintenanceResponse?.id)
                navigateFragment(view, R.id.nav_maintenance_detail,bundle)
            }
            R.id.cvDriverMain -> {
                val bundle = Bundle()
                bundle.putString(ARG_DRIVER_ID, driverResponse?.id)
                navigateFragment(view, R.id.nav_driver_detail,bundle)
            }
            R.id.ivTripOptions -> showTripOptionMenu(view, tripResponse)
            R.id.ivMaintenanceOptions -> showMaintenanceOptionMenu(view,maintenanceResponse)
            R.id.ivDriverOptions -> showDriverOptionMenu(view,driverResponse)
        }
    }

    private fun showTripOptionMenu(view: View, response: TripData?) {
        val inflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val vieww = inflater.inflate(R.layout.layout_options, null)
        val popupWindow = PopupWindow(vieww, requireContext().resources.getDimensionPixelSize(R.dimen._250sdp), LinearLayout.LayoutParams.WRAP_CONTENT, true)
        val binding: LayoutOptionsBinding = LayoutOptionsBinding.bind(vieww)
        popupWindow.showAsDropDown(view)

        binding.llCancel.visibility = View.VISIBLE
        binding.tvEdit.text = getString(R.string.edit_trip)
        binding.tvDelete.text = getString(R.string.delete_trip)

        binding.llEdit.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(AppConstants.Bundle.ARG_TRIP_ID, response?.id)
            bundle.putBoolean(AppConstants.Bundle.ARG_IS_FOR_TRIP_UPDATE, true)
            navigateFragment(view, R.id.nav_add_trip, bundle)
            popupWindow.dismiss()
        }

        binding.llDelete.setOnClickListener {
            callTripDeleteApi(response?.id)
            popupWindow.dismiss()
        }

        binding.llCancel.setOnClickListener {
            callTripCancelApi(response?.id)
            popupWindow.dismiss()
        }
    }

    private fun showMaintenanceOptionMenu(view: View, response: MaintenanceData?) {
        val inflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val vieww = inflater.inflate(R.layout.layout_options, null)
        val popupWindow = PopupWindow(vieww, requireContext().resources.getDimensionPixelSize(R.dimen._250sdp), LinearLayout.LayoutParams.WRAP_CONTENT, true)
        val binding: LayoutOptionsBinding = LayoutOptionsBinding.bind(vieww)
        popupWindow.showAsDropDown(view)

        binding.llCancel.visibility = View.GONE
        binding.tvEdit.text = getString(R.string.edit_maintenance)
        binding.tvDelete.text = getString(R.string.delete_maintenance)

        binding.llEdit.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(ARG_MAINTENANCE_ID, response?.id)
            bundle.putBoolean(AppConstants.Bundle.ARG_IS_FOR_MAINTENANCE_UPDATE, true)
            navigateFragment(view, R.id.nav_add_maintenance, bundle)
            popupWindow.dismiss()
        }

        binding.llDelete.setOnClickListener {
            callMaintenanceDeleteApi(response?.id)
            popupWindow.dismiss()
        }
    }

    private fun showDriverOptionMenu(view: View, driverResponse: DriverData?) {
        val inflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val vieww = inflater.inflate(R.layout.layout_options, null)
        val popupWindow = PopupWindow(vieww, requireContext().resources.getDimensionPixelSize(R.dimen._250sdp), LinearLayout.LayoutParams.WRAP_CONTENT, true)
        val binding: LayoutOptionsBinding = LayoutOptionsBinding.bind(vieww)
        popupWindow.showAsDropDown(view)

        binding.llCancel.visibility = View.GONE
        binding.tvEdit.text = getString(R.string.edit_driver_expense)
        binding.tvDelete.text = getString(R.string.delete_driver_expense)

        binding.llEdit.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(ARG_DRIVER_ID, driverResponse?.id)
            bundle.putBoolean(AppConstants.Bundle.ARG_IS_FOR_DRIVER_UPDATE, true)
            navigateFragment(view, R.id.nav_add_driver, bundle)
            popupWindow.dismiss()
        }

        binding.llDelete.setOnClickListener {
            callDriverDeleteApi(driverResponse?.id)
            popupWindow.dismiss()
        }
    }

    private fun callTripDeleteApi(id: String?) {
        baseActivity?.showProgress()
        val tripId = IdRequest()
        tripId.id = id.toString()
        viewModel?.callTripDeleteApi(tripId)
    }

    private fun callTripCancelApi(id: String?) {
        baseActivity?.showProgress()
        val tripId = IdRequest()
        tripId.id = id.toString()
        viewModel?.callTripCancelApi(tripId)
    }

    private fun callMaintenanceDeleteApi(id: String?){
        baseActivity?.showProgress()
        val tripId = MaintenanceIdRequest()
        tripId.maintainanceId = id.toString()
        viewModel?.callMaintenanceDeleteApi(tripId)
    }

    private fun callDriverDeleteApi(id: String?){
        baseActivity?.showProgress()
        val driverId = DriverIdRequest()
        driverId.driverExpenceId = id.toString()
        viewModel?.callDriverDeleteApi(driverId)
    }
}