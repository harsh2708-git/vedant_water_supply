package com.production.vedantwatersupply.ui.dashboard

import android.content.Context
import android.content.DialogInterface
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
import com.production.vedantwatersupply.model.response.DashboardResponse
import com.production.vedantwatersupply.ui.dialog.AlertDialogFragment
import com.production.vedantwatersupply.ui.driver.DriverAdapter
import com.production.vedantwatersupply.ui.login.LoginActivity
import com.production.vedantwatersupply.ui.maintenance.MaintenanceAdapter
import com.production.vedantwatersupply.ui.trips.TripsAdapter
import com.production.vedantwatersupply.utils.AppConstants
import com.production.vedantwatersupply.utils.CommonUtils
import com.production.vedantwatersupply.utils.SharedPreferenceUtil
import com.production.vedantwatersupply.utils.UserUtils
import com.production.vedantwatersupply.utils.formatPriceWithDecimal
import com.production.vedantwatersupply.utils.formatPriceWithoutDecimal
import com.production.vedantwatersupply.webservice.baseresponse.WebServiceSetting

class DashboardFragment : BaseFragment<FragmentDashboardBinding, DashboardViewModel>(), View.OnClickListener, RecyclerViewClickListener {

    override val layoutId: Int
        get() = R.layout.fragment_dashboard

    override fun getViewModel(): Class<DashboardViewModel> = DashboardViewModel::class.java

    override fun init() {

        binding?.clHeader?.ivBack?.visibility = View.GONE
        setTripsAdapter()
        setMaintenanceAdpter()
        setDriverAdapter()

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
//                    CommonUtils.showToast(requireContext(), it.webServiceSetting?.message)
                    updateUI(it)
                }

                WebServiceSetting.FAILURE -> {
                    CommonUtils.showToast(requireContext(), it.webServiceSetting?.message)
                }

                WebServiceSetting.NO_INTERNET -> {
                    CommonUtils.showToast(requireContext(), getString(R.string.no_internet_title))
                }
            }
        }
    }

    private fun updateUI(dashboardResponse: DashboardResponse) {
        binding?.tvTotalTrip?.text = dashboardResponse.totalTripsCount.toString().formatPriceWithoutDecimal()
        binding?.tvTotalFuelAmount?.text = getString(R.string.indian_rupee).plus(dashboardResponse.totalTripsAmount.toString().formatPriceWithDecimal())
        binding?.tvTotalMaintanance?.text = dashboardResponse.totalMaintainanceCount.toString().formatPriceWithoutDecimal()
        binding?.tvTotalMaintananceAmount?.text = getString(R.string.indian_rupee).plus(dashboardResponse.totalMaintainanceAmount.toString().formatPriceWithDecimal())
    }

    private fun setTripsAdapter() {
        val tripAdapter = TripsAdapter(requireContext(),ArrayList(), this)
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
        when (view?.id) {
            R.id.cvTripMain -> {
                val bundle = Bundle()
                bundle.putString(AppConstants.Bundle.ARG_TRIP_ID, "64c27d61b6d1c28ac6248a26")
                navigateFragment(view, R.id.nav_trip_detail, bundle)
            }
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