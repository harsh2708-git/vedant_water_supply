package com.production.vedantwatersupply.ui.trips

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.core.content.ContextCompat
import com.production.vedantwatersupply.R
import com.production.vedantwatersupply.core.BaseFragment
import com.production.vedantwatersupply.databinding.FragmentTripDetailBinding
import com.production.vedantwatersupply.databinding.LayoutOptionsBinding
import com.production.vedantwatersupply.model.request.TripDetailRequest
import com.production.vedantwatersupply.model.response.TripData
import com.production.vedantwatersupply.utils.AppConstants.Bundle.Companion.ARG_TRIP_ID
import com.production.vedantwatersupply.utils.AppConstants.Trip.Companion.OEN_TANKER_DISPLAY
import com.production.vedantwatersupply.utils.AppConstants.Trip.Companion.OTHER_DRIVER_DISPLAY
import com.production.vedantwatersupply.utils.AppConstants.Trip.Companion.OTHER_TANKER_DISPLAY
import com.production.vedantwatersupply.utils.AppConstants.Trip.Companion.PERMANENT_DRIVER_DISPLAY
import com.production.vedantwatersupply.utils.CommonUtils
import com.production.vedantwatersupply.utils.formatPriceWithDecimal
import com.production.vedantwatersupply.webservice.baseresponse.WebServiceSetting


class TripDetailFragment : BaseFragment<FragmentTripDetailBinding, TripViewModel>(), View.OnClickListener {

    private var tripId = ""
    override val layoutId: Int
        get() = R.layout.fragment_trip_detail

    override fun getViewModel(): Class<TripViewModel> = TripViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            tripId = arguments?.getString(ARG_TRIP_ID).toString()
        }
    }

    override fun init() {

        binding?.clScreenTitle?.tvTitle?.text = getString(R.string.trip_detail)
        binding?.clScreenTitle?.tvDescription?.text = getString(R.string.here_you_can_check_trip_detail)

        callTripDetailApi()
    }

    override fun initListener() {
        binding?.clHeader?.ivBack?.setOnClickListener(this)
        binding?.ivOptions?.setOnClickListener(this)
        binding?.ivUp?.setOnClickListener(this)
        binding?.nsView?.viewTreeObserver?.addOnScrollChangedListener {
            val scrollY = binding?.nsView?.scrollY
            if (scrollY != null)
                binding?.ivUp?.visibility = if (scrollY > 0) View.VISIBLE else View.GONE
        }
    }

    private fun callTripDetailApi() {
        baseActivity?.showProgress()
        val tripDetailRequest = TripDetailRequest()
        tripDetailRequest.id = tripId
        viewModel?.callTripDetailApi(tripDetailRequest)
    }

    private fun callTripDeleteApi() {
        baseActivity?.showProgress()
        val tripId = TripDetailRequest()
        tripId.id = tripId.toString()
        viewModel?.callTripDeleteApi(tripId)
    }

    private fun callTripCancelApi() {
        baseActivity?.showProgress()
        val tripId = TripDetailRequest()
        tripId.id = tripId.toString()
        viewModel?.callTripCancelApi(tripId)
    }

    override fun addObserver() {
        viewModel?.tripRepository?.tripDetailResponseMutableLiveData?.observe(this) {
            baseActivity?.hideProgress()
            when (it?.webServiceSetting?.success) {
                WebServiceSetting.SUCCESS -> {
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

        viewModel?.tripRepository?.tripDeleteResponseMutableLiveData?.observe(this) {
            when (it.webServiceSetting?.success) {
                WebServiceSetting.SUCCESS -> {
                    CommonUtils.showToast(requireContext(), it.webServiceSetting?.message)
                    baseActivity?.onBackPressed()
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
                    callTripDetailApi()
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

    private fun updateUI(it: TripData) {
        binding?.clMain?.visibility = View.VISIBLE

        binding?.tvTripCode?.text = it.reference
        binding?.tvStatus?.text = it.status
        binding?.tvStatus?.visibility = if (it.status?.isEmpty() == true) View.GONE else View.VISIBLE
        binding?.tvAddedByDate?.text = CommonUtils.getSpannableThreeText(
            "By ",
            it.addedBy?.firstName.plus(" " + it.addedBy?.lastName),
            " "+it.tripDateReadable.toString(),
            ContextCompat.getColor(requireContext(), R.color.color_dark),
            ContextCompat.getColor(requireContext(), R.color.color_dark),
            ContextCompat.getColor(requireContext(), R.color.body_text)
        )
        binding?.tvTruckNo?.text = it.tanker?.tankerNumber
        binding?.tvTankerType?.text = if (it.tanker?.isOwned == true) OEN_TANKER_DISPLAY else OTHER_TANKER_DISPLAY
        binding?.tvWaterType?.text = it.waterType?.ifEmpty { getString(R.string.na) }
        binding?.tvFuelAmount?.text = it.fuelAmount.toString().formatPriceWithDecimal()
        binding?.tvPaymentMode?.text = it.paymentMode
        binding?.tvFuelFilledBy?.text = it.fuelFilledBy
        binding?.tvDriveType?.text = if (it.driver?.isParmanent == true) PERMANENT_DRIVER_DISPLAY else OTHER_DRIVER_DISPLAY
        binding?.tvDriverName?.text = it.driver?.driverName
        binding?.tvDriverMoNo?.text = it.driver?.driverMobile?.ifEmpty { getString(R.string.na) }
        binding?.tvFilledSiteName?.text = it.fillingSite
        binding?.tvDestination?.text = it.destinationSite
        binding?.tvCustomerName?.text = it.customerName
        binding?.tvCustomerMoNo?.text = it.customerMobile?.ifEmpty { getString(R.string.na) }
        binding?.tvDescription?.text = it.description?.ifEmpty { getString(R.string.na) }

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ivBack -> baseActivity?.onBackPressed()
            R.id.ivOptions -> showOptionMenu(v)
            R.id.ivUp -> binding?.nsView?.smoothScrollTo(0, 0)
        }
    }

    private fun showOptionMenu(view: View) {
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
            callTripDeleteApi()
            popupWindow.dismiss()
        }

        binding.llCancel.setOnClickListener {
            callTripCancelApi()
            popupWindow.dismiss()
        }
    }

}