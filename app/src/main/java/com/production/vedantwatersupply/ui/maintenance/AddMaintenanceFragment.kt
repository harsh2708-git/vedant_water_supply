package com.production.vedantwatersupply.ui.maintenance

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.production.vedantwatersupply.R
import com.production.vedantwatersupply.core.BaseFragment
import com.production.vedantwatersupply.custome.VWSSpinnerAdapter
import com.production.vedantwatersupply.databinding.FragmentAddMaintenanceBinding
import com.production.vedantwatersupply.model.request.AddMaintenanceRequest
import com.production.vedantwatersupply.model.request.MaintenanceIdRequest
import com.production.vedantwatersupply.model.response.FilterResponse
import com.production.vedantwatersupply.model.response.MaintenanceData
import com.production.vedantwatersupply.ui.MainActivity
import com.production.vedantwatersupply.utils.AppConstants
import com.production.vedantwatersupply.utils.AppConstants.Bundle.Companion.ARG_IS_FOR_MAINTENANCE_UPDATE
import com.production.vedantwatersupply.utils.AppConstants.Bundle.Companion.ARG_MAINTENANCE_ID
import com.production.vedantwatersupply.utils.AppConstants.Trip.Companion.OTHER_TANKER
import com.production.vedantwatersupply.utils.AppConstants.Trip.Companion.OWN_TANKER
import com.production.vedantwatersupply.utils.CommonUtils
import com.production.vedantwatersupply.utils.calendar.CaldroidListener
import com.production.vedantwatersupply.utils.filter.FilterItem
import com.production.vedantwatersupply.utils.formatPriceWithoutDecimal
import com.production.vedantwatersupply.utils.setFilter
import com.production.vedantwatersupply.webservice.baseresponse.WebServiceSetting
import java.util.Date

class AddMaintenanceFragment : BaseFragment<FragmentAddMaintenanceBinding, MaintenanceViewModel>(), View.OnClickListener {

    private var addUpdateMaintenanceRequest = AddMaintenanceRequest()

    private var isOwnTanker = true
    private var isOtherTanker = false

    private var getTankerAndDriverFixed = FilterResponse()
    private var tankerList = ArrayList<FilterItem>()

    private var tankerNoId = ""
    private var selectedTankerNo = ""

    private var fromDate = ""
    private var displayFromDate = ""

    private var paymentModeId = ""
    private var selectedPaymentMode = ""

    private var maintenanceId = ""
    private var isForMaintenanceUpdate = false

    override val layoutId: Int
        get() = R.layout.fragment_add_maintenance

    override fun getViewModel(): Class<MaintenanceViewModel> = MaintenanceViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null) {
            maintenanceId = arguments?.getString(ARG_MAINTENANCE_ID).toString()
            isForMaintenanceUpdate = arguments?.getBoolean(ARG_IS_FOR_MAINTENANCE_UPDATE, false) == true
        }
    }

    override fun init() {

        if (isForMaintenanceUpdate) {
            binding?.clScreenSummary?.tvTitle?.text = getString(R.string.update_maintenance)
            binding?.clScreenSummary?.tvDescription?.text = getString(R.string.here_you_can_update_maintenance_details)
            binding?.btnAdd?.text = getString(R.string.update)
            callMaintenanceDetailApi()
        } else {
            binding?.clScreenSummary?.tvTitle?.text = getString(R.string.add_maintenance)
            binding?.clScreenSummary?.tvDescription?.text = getString(R.string.here_you_can_add_maintenance_details)
            binding?.btnAdd?.text = getString(R.string.add)
        }


        binding?.clRadio?.rb1?.text = getString(R.string.own_tankers)
        binding?.clRadio?.rb2?.text = getString(R.string.other_tanker)

        callGetTankerAndDriverFixed()

        setPaymentModeSpinner()

    }

    override fun initListener() {
        binding?.clRadio?.rgCustom?.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == binding?.clRadio?.rb1?.id) {
                binding?.grpTruckSpinner?.visibility = View.VISIBLE
                binding?.etTankerNo?.visibility = View.GONE
                addUpdateMaintenanceRequest.tankerType = OWN_TANKER
                isOwnTanker = true
                isOtherTanker = false
                binding?.etTankerNo?.setText("")
            } else {
                binding?.grpTruckSpinner?.visibility = View.GONE
                binding?.etTankerNo?.visibility = View.VISIBLE
                addUpdateMaintenanceRequest.tankerType = OTHER_TANKER
                isOwnTanker = false
                isOtherTanker = true
                binding?.spTankerNo?.setSelection(0)
                binding?.tvTankerNo?.text = ""
            }
        }
        binding?.clHeader?.ivBack?.setOnClickListener(this)
        binding?.btnAdd?.setOnClickListener(this)
        binding?.btnDiscard?.setOnClickListener(this)
        binding?.ivUp?.setOnClickListener(this)

        binding?.nsView?.viewTreeObserver?.addOnScrollChangedListener {
            val scrollY = binding?.nsView?.scrollY
            if (scrollY != null)
                binding?.ivUp?.visibility = if (scrollY > 0) View.GONE else View.GONE
        }

        binding?.tvTankerNo?.setOnClickListener(this)
        binding?.tvDate?.setOnClickListener(this)
        binding?.tvPaymentMode?.setOnClickListener(this)
    }

    private fun callGetTankerAndDriverFixed() {
        showProgress()
        viewModel?.callGetTankerAndDriverFixed()
    }

    private fun callAddUpdateMaintenanceApi() {
        addUpdateMaintenanceRequest.maintainanceId = if (isForMaintenanceUpdate) maintenanceId else ""
        addUpdateMaintenanceRequest.tankerId = if (isOwnTanker) selectedTankerNo else ""
        addUpdateMaintenanceRequest.tankerNumber = if (isOtherTanker) binding?.etTankerNo?.getTrimmedText().toString() else ""
        addUpdateMaintenanceRequest.description = binding?.etDescription?.text.toString()
        addUpdateMaintenanceRequest.amount = binding?.etMaintenanceAmount?.numericValue.toString()
        addUpdateMaintenanceRequest.paymentMode = selectedPaymentMode
        addUpdateMaintenanceRequest.date = fromDate
        Log.e("Add Update Request", "callAddUpdateMaintenanceApi: " + Gson().toJson(addUpdateMaintenanceRequest))

        viewModel?.callAddUpdateMaintenanceApi(addUpdateMaintenanceRequest)
    }

    private fun callMaintenanceDetailApi() {
        baseActivity?.showProgress()
        val maintenanceDetailRequest = MaintenanceIdRequest()
        maintenanceDetailRequest.maintainanceId = maintenanceId
        viewModel?.callMaintenanceDetailApi(maintenanceDetailRequest)
    }

    override fun addObserver() {
        viewModel?.tripRepository?.getTankerAndDriverFixedResponseMutableLiveData?.observe(this) {
            hideProgress()
            when (it?.webServiceSetting?.success) {
                WebServiceSetting.SUCCESS -> {
                    getTankerAndDriverFixed = it

                    tankerList.clear()
                    tankerList.add(0, FilterItem("", getString(R.string.please_select_tanker_no)))
                    getTankerAndDriverFixed.vehicle?.let { tanker -> tankerList.addAll(tanker) }

                    setTankerNoSpinner()
                }

                WebServiceSetting.FAILURE -> {
                    CommonUtils.showToast(requireContext(), it.webServiceSetting?.message)
                }

                WebServiceSetting.NO_INTERNET -> {
                    CommonUtils.showToast(requireContext(), getString(R.string.no_internet_title))
                }
            }
        }

        viewModel?.maintenanceRepository?.addUpdateMaintenanceResponseMutableLiveData?.observe(this) {
            hideProgress()
            when (it?.webServiceSetting?.success) {
                WebServiceSetting.SUCCESS -> {
                    CommonUtils.showToast(requireContext(), it.webServiceSetting?.message)
                    if (isForMaintenanceUpdate) {
                        (baseActivity as MainActivity).getNavController().popBackStack(R.id.nav_maintenance_listing, false)
                    } else baseActivity?.onBackPressed()
                }

                WebServiceSetting.FAILURE -> {
                    CommonUtils.showToast(requireContext(), it.webServiceSetting?.message)
                }

                WebServiceSetting.NO_INTERNET -> {
                    CommonUtils.showToast(requireContext(), getString(R.string.no_internet_title))
                }
            }
        }

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
    }

    override fun onClick(v: View?) {
        var selectedDate: Date? = null
        when (v?.id) {
            R.id.ivBack,
            R.id.btnDiscard -> baseActivity?.onBackPressed()

            R.id.btnAdd -> {
                if (isValid()) {
                    showProgress()
                    callAddUpdateMaintenanceApi()
                }
            }

            R.id.ivUp -> binding?.nsView?.smoothScrollTo(0, 0)

            R.id.tvTankerNo -> binding?.spTankerNo?.performClick()
            R.id.tvPaymentMode -> binding?.spPaymentMode?.performClick()

            R.id.tvDate -> {
                selectedDate = CommonUtils.getDateFromDisplay(fromDate)

                baseActivity?.setNormalCalender(
                    object : CaldroidListener() {
                        override fun onSelectDate(date1: Date?, view: View?) {
                            binding?.tvDate?.setText(date1.toString())
                            selectedDate = date1
                            fromDate = CommonUtils.getFormattedDateFromV2(date1).toString()
                            displayFromDate = CommonUtils.getFormattedDateFrom(date1).toString()
                            binding?.tvDate?.setText(displayFromDate)
                        }
                    }, selectedDate, null, Date()
                )

                /*val dialogLicense = DatePickerDialog(
                    requireContext(), 0,
                    { view, year, month, dayOfMonth ->
                        scheduledDate?.set(Calendar.YEAR, year)
                        scheduledDate?.set(Calendar.MONTH, month)
                        scheduledDate?.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        binding?.tvDate?.let { CommonUtils.showFormattedDate(it, scheduledDate?.time) }
                    },
                    scheduledDate!![Calendar.YEAR],
                    scheduledDate!![Calendar.MONTH],
                    scheduledDate!![Calendar.DAY_OF_MONTH]
                )
                dialogLicense.datePicker.maxDate = Calendar.getInstance().timeInMillis
                dialogLicense.show()*/
            }

        }
    }

    private fun setTankerNoSpinner() {
        val adapter = VWSSpinnerAdapter(requireContext(), R.layout.simple_dropdown_item, tankerList)
        binding?.spTankerNo?.adapter = adapter

        if (tankerNoId.isNotEmpty()) {
            val takerNo: FilterItem? = tankerList.find { it.dbValue.equals(tankerNoId, false) }
            val spinnerPosition: Int = tankerList.indexOf(takerNo)
            selectedTankerNo = takerNo.toString()
            spinnerPosition.let { binding?.spTankerNo?.setSelection(it) }
        }

        binding?.spTankerNo?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                selectedTankerNo = tankerList[binding?.spTankerNo?.selectedItemPosition!!].dbValue
                if (selectedTankerNo.isNotEmpty()) {
                    adapter.setSelectedPosition(i)
                    binding?.tvTankerNo?.text = tankerList[binding?.spTankerNo?.selectedItemPosition!!].displayValue
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }
    }

    private fun setPaymentModeSpinner() {

        val paymentModeList = ArrayList<FilterItem>()
        if (paymentModeList.isEmpty()) {
            paymentModeList.add(0, FilterItem("", getString(R.string.please_select_payment_mode)))
            paymentModeList.add(FilterItem("Cheque", "Cheque"))
            paymentModeList.add(FilterItem("Cash", "Cash"))
        }
        val adapter = VWSSpinnerAdapter(requireContext(), R.layout.simple_dropdown_item, paymentModeList)
        binding?.spPaymentMode?.adapter = adapter

        if (paymentModeId.isNotEmpty()) {
            val paymentMode: FilterItem? = paymentModeList.find { it.dbValue.equals(paymentModeId, false) }
            val spinnerPosition: Int = paymentModeList.indexOf(paymentMode)
            selectedPaymentMode = paymentMode.toString()
            spinnerPosition.let { binding?.spPaymentMode?.setSelection(it) }
        }

        binding?.spPaymentMode?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                selectedPaymentMode = paymentModeList[binding?.spPaymentMode?.selectedItemPosition!!].dbValue
                if (selectedPaymentMode.isNotEmpty()) {
                    adapter.setSelectedPosition(i)
                    binding?.tvPaymentMode?.text = paymentModeList[binding?.spPaymentMode?.selectedItemPosition!!].displayValue
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }
    }

    private fun isValid(): Boolean {
        return if (isOwnTanker && binding?.tvTankerNo?.text?.isEmpty() == true) {
            CommonUtils.showToast(requireContext(), getString(R.string.please_select_tanker_no))
            false
        } else if (addUpdateMaintenanceRequest.tankerType.equals(OTHER_TANKER, ignoreCase = true) && binding?.etTankerNo?.text?.isEmpty() == true) {
            CommonUtils.showToast(requireContext(), getString(R.string.please_enter_tanker_no))
            false
        } else if (binding?.tvDate?.text?.isEmpty() == true) {
            CommonUtils.showToast(requireContext(), getString(R.string.please_select_date))
            false
        } else if (binding?.etMaintenanceAmount?.text?.isEmpty() == true) {
            CommonUtils.showToast(requireContext(), getString(R.string.please_enter_fuel_amount))
            false
        } else if (binding?.spPaymentMode?.selectedItemPosition == 0) {
            CommonUtils.showToast(requireContext(), getString(R.string.please_select_payment_mode))
            false
        } else {
            true
        }
    }

    private fun updateUI(it: MaintenanceData) {
        if (it.tanker?.isOwned == true) {
            binding?.clRadio?.rb1?.isChecked = true
            tankerNoId = it.tanker?.id.toString()
            setTankerNoSpinner()
        } else {
            binding?.clRadio?.rb2?.isChecked = true
            binding?.etTankerNo?.setText(it.tanker?.tankerNumber)
        }

        binding?.tvDate?.text = it.dateReadable.toString()
        fromDate = CommonUtils.getServerDateFromDisplay(it.dateReadable).toString()
        binding?.etMaintenanceAmount?.setText(it.amount.toString())

        paymentModeId = it.paymentMode.toString()
        setPaymentModeSpinner()

        binding?.etDescription?.setText(it.description)
    }

}