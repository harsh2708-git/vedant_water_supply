package com.production.vedantwatersupply.ui.maintenance

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.core.content.ContextCompat
import com.production.vedantwatersupply.R
import com.production.vedantwatersupply.core.BaseFragment
import com.production.vedantwatersupply.databinding.FragmentMaintenanceDetailBinding
import com.production.vedantwatersupply.databinding.LayoutOptionsBinding
import com.production.vedantwatersupply.model.request.MaintenanceIdRequest
import com.production.vedantwatersupply.model.response.MaintenanceData
import com.production.vedantwatersupply.ui.MainActivity
import com.production.vedantwatersupply.utils.AppConstants
import com.production.vedantwatersupply.utils.AppConstants.Bundle.Companion.ARG_MAINTENANCE_ID
import com.production.vedantwatersupply.utils.AppConstants.Trip.Companion.OEN_TANKER_DISPLAY
import com.production.vedantwatersupply.utils.AppConstants.Trip.Companion.OTHER_TANKER_DISPLAY
import com.production.vedantwatersupply.utils.CommonUtils
import com.production.vedantwatersupply.utils.formatPriceWithoutDecimal
import com.production.vedantwatersupply.webservice.baseresponse.WebServiceSetting


class MaintenanceDetailFragment : BaseFragment<FragmentMaintenanceDetailBinding, MaintenanceViewModel>(), View.OnClickListener {

    private var maintenanceId = ""

    override val layoutId: Int
        get() = R.layout.fragment_maintenance_detail

    override fun getViewModel(): Class<MaintenanceViewModel> = MaintenanceViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            maintenanceId = arguments?.getString(ARG_MAINTENANCE_ID).toString()
        }
    }

    override fun init() {
        binding?.clScreenTitle?.tvTitle?.text = getString(R.string.maintenance_detail)
        binding?.clScreenTitle?.tvDescription?.text = getString(R.string.here_you_can_check_tanker_maintenance_detail)

        callMaintenanceDetailApi()
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

    private fun callMaintenanceDetailApi() {
        baseActivity?.showProgress()
        val maintenanceDetailRequest = MaintenanceIdRequest()
        maintenanceDetailRequest.maintainanceId = maintenanceId
        viewModel?.callMaintenanceDetailApi(maintenanceDetailRequest)
    }

    override fun addObserver() {
        viewModel?.maintenanceRepository?.maintenanceDetailResponseMutableLiveData?.observe(this) {
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

        viewModel?.maintenanceRepository?.maintenanceDeleteResponseMutableLiveData?.observe(this) {
            when (it.webServiceSetting?.success) {
                WebServiceSetting.SUCCESS -> {
                    CommonUtils.showToast(requireContext(), it.webServiceSetting?.message)
                    (baseActivity as MainActivity).getNavController().popBackStack(R.id.nav_maintenance_listing, false)
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

    private fun updateUI(it: MaintenanceData) {
        binding?.clMain?.visibility = View.VISIBLE

        binding?.tvTripCode?.text = it.reference
        binding?.tvAddedBy?.text = CommonUtils.getSpannableThreeText(
            "By ",
            it.addedBy?.firstName.plus(" " + it.addedBy?.lastName),
            " " + it.dateReadable.toString(),
            ContextCompat.getColor(requireContext(), R.color.color_dark),
            ContextCompat.getColor(requireContext(), R.color.color_dark),
            ContextCompat.getColor(requireContext(), R.color.body_text)
        )
        binding?.tvTruckNo?.text = it.tanker?.tankerNumber
        binding?.tvTankerType?.text = if (it.tanker?.isOwned == true) OEN_TANKER_DISPLAY else OTHER_TANKER_DISPLAY
        binding?.tvMaintenanceAmount?.text = it.amount.toString().formatPriceWithoutDecimal()
        binding?.tvPaymentMode?.text = it.paymentMode
        binding?.tvDescription?.text = it.description.toString().ifEmpty { getString(R.string.na) }
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

        binding.llCancel.visibility = View.GONE
        binding.tvEdit.text = getString(R.string.edit_maintenance)
        binding.tvDelete.text = getString(R.string.delete_maintenance)

        binding.llEdit.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(ARG_MAINTENANCE_ID, maintenanceId)
            bundle.putBoolean(AppConstants.Bundle.ARG_IS_FOR_MAINTENANCE_UPDATE, true)
            navigateFragment(view, R.id.nav_add_maintenance, bundle)
            popupWindow.dismiss()
        }

        binding.llDelete.setOnClickListener {
            callMaintenanceDeleteApi(maintenanceId)
            popupWindow.dismiss()
        }

    }

    private fun callMaintenanceDeleteApi(id: String?){
        baseActivity?.showProgress()
        val tripId = MaintenanceIdRequest()
        tripId.maintainanceId = id.toString()
        viewModel?.callMaintenanceDeleteApi(tripId)
    }
}