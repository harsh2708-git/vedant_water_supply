package com.production.vedantwatersupply.ui.driver

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
import com.production.vedantwatersupply.databinding.FragmentDriverDetailBinding
import com.production.vedantwatersupply.databinding.LayoutOptionsBinding
import com.production.vedantwatersupply.model.request.DriverIdRequest
import com.production.vedantwatersupply.model.request.MaintenanceIdRequest
import com.production.vedantwatersupply.model.response.DriverData
import com.production.vedantwatersupply.ui.MainActivity
import com.production.vedantwatersupply.utils.AppConstants
import com.production.vedantwatersupply.utils.CommonUtils
import com.production.vedantwatersupply.utils.formatPriceWithoutDecimal
import com.production.vedantwatersupply.webservice.baseresponse.WebServiceSetting

class DriverDetailFragment : BaseFragment<FragmentDriverDetailBinding, DriverViewModel>(), View.OnClickListener {

    private var driverId = ""
    override val layoutId: Int
        get() = R.layout.fragment_driver_detail

    override fun getViewModel(): Class<DriverViewModel> = DriverViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            driverId = arguments?.getString(AppConstants.Bundle.ARG_DRIVER_ID).toString()
        }
    }

    override fun init() {
        binding?.clScreenTitle?.tvTitle?.text = getString(R.string.driver_expense_detail)
        binding?.clScreenTitle?.tvDescription?.text = getString(R.string.here_you_can_check_driver_expense_detail)

        callDriverDetailApi()
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

    private fun callDriverDetailApi() {
        showProgress()
        val driverIdRequest = DriverIdRequest()
        driverIdRequest.driverExpenceId = driverId
        viewModel?.callDriverDetailApi(driverIdRequest)
    }

    override fun addObserver() {
        viewModel?.driverRepository?.driverExpensesDetailResponseMutableLiveData?.observe(this) {
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

        viewModel?.driverRepository?.deleteDriverExpenseResponseMutableLiveData?.observe(this) {
            when (it.webServiceSetting?.success) {
                WebServiceSetting.SUCCESS -> {
                    CommonUtils.showToast(requireContext(), it.webServiceSetting?.message)
                    (baseActivity as MainActivity).getNavController().popBackStack(R.id.nav_driver_listing, false)
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
        binding.tvEdit.text = getString(R.string.edit_driver_expense)
        binding.tvDelete.text = getString(R.string.delete_driver_expense)

        binding.llEdit.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(AppConstants.Bundle.ARG_DRIVER_ID, driverId)
            bundle.putBoolean(AppConstants.Bundle.ARG_IS_FOR_DRIVER_UPDATE, true)
            navigateFragment(view, R.id.nav_add_driver, bundle)
            popupWindow.dismiss()
        }

        binding.llDelete.setOnClickListener {
            callDriverDeleteApi(driverId)
            popupWindow.dismiss()
        }

    }

    private fun updateUI(it: DriverData) {
        binding?.clMain?.visibility = View.VISIBLE

        binding?.tvDriverCode?.text = it.reference
        binding?.tvAddedBy?.text = CommonUtils.getSpannableThreeText(
            "By ",
            it.addedBy?.firstName.plus(" " + it.addedBy?.lastName),
            " " + it.dateReadable.toString(),
            ContextCompat.getColor(requireContext(), R.color.color_dark),
            ContextCompat.getColor(requireContext(), R.color.color_dark),
            ContextCompat.getColor(requireContext(), R.color.body_text)
        )
        binding?.tvDriverName?.text = it.driver?.driverName
        binding?.tvDriverType?.text = if (it.driver?.isParmanent == true) AppConstants.Trip.PERMANENT_DRIVER_DISPLAY else AppConstants.Trip.OTHER_DRIVER_DISPLAY
        binding?.tvPaidAmount?.text = it.amount.toString().formatPriceWithoutDecimal()
        binding?.tvExtraPaidAmount?.text = it.extraPaymentAmount.toString().formatPriceWithoutDecimal().ifEmpty { getString(R.string.na) }
        binding?.tvPaymentMode?.text = it.paymentMode
        binding?.tvExtraPayment?.text = if (it.isExtrapayment  == 1) "Yes" else "No"
        binding?.tvDescription?.text = it.description.toString().ifEmpty { getString(R.string.na) }
    }

    private fun callDriverDeleteApi(id: String?) {
        baseActivity?.showProgress()
        val driverId = DriverIdRequest()
        driverId.driverExpenceId = id.toString()
        viewModel?.callDriverDeleteApi(driverId)
    }

}