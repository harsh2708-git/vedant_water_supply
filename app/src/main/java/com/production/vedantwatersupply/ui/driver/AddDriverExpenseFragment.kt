package com.production.vedantwatersupply.ui.driver

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.production.vedantwatersupply.R
import com.production.vedantwatersupply.core.BaseFragment
import com.production.vedantwatersupply.custome.VWSSpinnerAdapter
import com.production.vedantwatersupply.databinding.FragmentAddDriverExpenseBinding
import com.production.vedantwatersupply.model.request.AddDriverExpensesRequest
import com.production.vedantwatersupply.model.request.DriverIdRequest
import com.production.vedantwatersupply.model.request.MaintenanceIdRequest
import com.production.vedantwatersupply.model.request.SearchDriverRequest
import com.production.vedantwatersupply.model.response.DriverData
import com.production.vedantwatersupply.model.response.FilterResponse
import com.production.vedantwatersupply.ui.MainActivity
import com.production.vedantwatersupply.utils.AppConstants
import com.production.vedantwatersupply.utils.CommonUtils
import com.production.vedantwatersupply.utils.calendar.CaldroidListener
import com.production.vedantwatersupply.utils.filter.FilterItem
import com.production.vedantwatersupply.utils.formatPriceWithoutDecimal
import com.production.vedantwatersupply.webservice.baseresponse.WebServiceSetting
import java.util.Date


class AddDriverExpenseFragment : BaseFragment<FragmentAddDriverExpenseBinding, DriverViewModel>(), View.OnClickListener {

    private var isPermanentDriver = true
    private var isOtherDriver = false

    private var addUpdateDriverExpenseRequest = AddDriverExpensesRequest()

    private var getTankerAndDriverFixed = FilterResponse()
    private var driverList = ArrayList<FilterItem>()
    private var driverId = ""
    private var selectedDriver = ""
    private var driverNameId = ""
    private var driverNameList = ArrayList<FilterItem>()
    private var apiPaymentDate = ""
    private var displayPaymentDate = ""

    private var paymentModeList = ArrayList<FilterItem>()
    private var paymentModeId = ""
    private var selectedPaymentMode = ""
    private var isExtraPayment = true

    private var driverUpdateId = ""
    private var isForDriverUpdate = false

    private var extraPayment = 1

    private var driverPaymentDoneById = ""
    private var selectedDriverPaymentDoneBy = ""

    override val layoutId: Int
        get() = R.layout.fragment_add_driver_expense

    override fun getViewModel(): Class<DriverViewModel> = DriverViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            driverUpdateId = arguments?.getString(AppConstants.Bundle.ARG_DRIVER_ID).toString()
            isForDriverUpdate = arguments?.getBoolean(AppConstants.Bundle.ARG_IS_FOR_DRIVER_UPDATE, false) == true
        }
    }

    override fun init() {

        if (isForDriverUpdate) {
            binding?.clScreenSummary?.tvTitle?.text = getString(R.string.update_driver_expense)
            binding?.clScreenSummary?.tvDescription?.text = getString(R.string.here_you_can_update_driver_expense_details)
            binding?.btnAdd?.text = getString(R.string.update)
            callDriverDetailApi()
        } else {
            binding?.clScreenSummary?.tvTitle?.text = getString(R.string.add_driver_expense)
            binding?.clScreenSummary?.tvDescription?.text = getString(R.string.here_you_can_add_driver_expense_details)
            binding?.btnAdd?.text = getString(R.string.add)
        }

        callGetTankerAndDriverFixed()

        binding?.clDriverTypeRadio?.rb1?.text = getString(R.string.permanent_driver)
        binding?.clDriverTypeRadio?.rb2?.text = getString(R.string.other_driver)
        binding?.clExtraPaymentRadio?.rb1?.text = getString(R.string.yes)
        binding?.clExtraPaymentRadio?.rb2?.text = getString(R.string.no)

        setPaymentModeSpinner()
        setDriverPaymentDoneBy()
    }


    override fun initListener() {
        binding?.clDriverTypeRadio?.rgCustom?.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == binding?.clDriverTypeRadio?.rb1?.id) {
                binding?.grpDriverSpinner?.visibility = View.VISIBLE
                binding?.etDriverName?.visibility = View.GONE

                addUpdateDriverExpenseRequest.driverType = AppConstants.Trip.PERMANENT_DRIVER
                isPermanentDriver = true
                isOtherDriver = false
                binding?.etDriverName?.setText("")

            } else {
                binding?.grpDriverSpinner?.visibility = View.GONE
                binding?.etDriverName?.visibility = View.VISIBLE
                addUpdateDriverExpenseRequest.driverType = AppConstants.Trip.OTHER_DRIVER
                isPermanentDriver = false
                isOtherDriver = true
                binding?.spDriverName?.setSelection(0)
                binding?.tvDriverName?.text = ""
            }
        }

        binding?.clExtraPaymentRadio?.rgCustom?.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == binding?.clDriverTypeRadio?.rb1?.id) {
                binding?.etExtraPayment?.visibility = View.VISIBLE
                isExtraPayment = true

            } else {
                binding?.etExtraPayment?.visibility = View.GONE
                isExtraPayment = false
                binding?.etExtraPayment?.setText("")
            }
        }
        binding?.clHeader?.ivBack?.setOnClickListener(this)
        binding?.btnAdd?.setOnClickListener(this)
        binding?.btnDiscard?.setOnClickListener(this)
        binding?.ivUp?.setOnClickListener(this)
        binding?.nsView?.viewTreeObserver?.addOnScrollChangedListener {
            val scrollY = binding?.nsView?.scrollY
            if (scrollY != null)
                binding?.ivUp?.visibility = if (scrollY > 0) View.VISIBLE else View.GONE
        }
        binding?.tvDriverName?.setOnClickListener(this)

        binding?.etDriverName?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                binding?.etDriverName?.tag = null
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (binding?.etDriverName?.isPerformingCompletion == true) {
                    return
                }
                if (!TextUtils.isEmpty(s.toString())) callSearchDriverApi(s.toString())
            }
        })

        val autoCompleteItemClick = AdapterView.OnItemClickListener { parent: AdapterView<*>, view1: View?, position: Int, id: Long ->
            val item = parent.getItemAtPosition(position)
            if (item is FilterItem) {
                binding?.etDriverName?.tag = item
                driverNameId = item.dbValue
            }
        }
        binding?.etDriverName?.onItemClickListener = autoCompleteItemClick

        binding?.tvDate?.setOnClickListener(this)
        binding?.tvPaymentMode?.setOnClickListener(this)
        binding?.tvDriverPaymentDoneBy?.setOnClickListener(this)
    }

    private fun callGetTankerAndDriverFixed() {
        showProgress()
        viewModel?.callGetTankerAndDriverFixed()
    }

    private fun callSearchDriverApi(value: String) {
        val searchDriverRequest = SearchDriverRequest()
        searchDriverRequest.driverName = value
        searchDriverRequest.isPermanent = ""
        viewModel?.callSearchDriverApi(searchDriverRequest)
    }

    override fun addObserver() {
        viewModel?.tripRepository?.getTankerAndDriverFixedResponseMutableLiveData?.observe(this) {
            hideProgress()
            when (it?.webServiceSetting?.success) {
                WebServiceSetting.SUCCESS -> {
                    getTankerAndDriverFixed = it
                    driverList.clear()
                    driverList.add(0, FilterItem("", getString(R.string.please_select_driver)))
                    getTankerAndDriverFixed.driver?.let { driver -> driverList.addAll(driver) }

                    setDriverSpinner()
                }

                WebServiceSetting.FAILURE -> {
                    CommonUtils.showToast(requireContext(), it.webServiceSetting?.message)
                }

                WebServiceSetting.NO_INTERNET -> {
                    CommonUtils.showToast(requireContext(), getString(R.string.no_internet_title))
                }
            }
        }

        viewModel?.driverRepository?.searchDriverResponseMutableLiveData?.observe(this) {
            when (it.settings?.success) {
                WebServiceSetting.SUCCESS -> {
                    driverNameList.clear()
                    it?.data?.let { it1 -> driverNameList.addAll(it1) }
                    hideProgress()
                    binding?.etDriverName?.setAdapter(ArrayAdapter(requireContext(), R.layout.simple_dropdown_item, driverNameList))
                    binding?.etDriverName?.showDropDown()
                }

                WebServiceSetting.FAILURE -> {
                    CommonUtils.showToast(requireContext(), it.settings?.message)
                }

                WebServiceSetting.NO_INTERNET -> {
                    CommonUtils.showToast(requireContext(), getString(R.string.no_internet_title))
                }
            }
        }

        viewModel?.driverRepository?.addUpdateDriverExpenseResponseMutableLiveData?.observe(this) {
            hideProgress()
            when (it?.webServiceSetting?.success) {
                WebServiceSetting.SUCCESS -> {
                    CommonUtils.showToast(requireContext(), it.webServiceSetting?.message)
                    if (isForDriverUpdate) {
                        (baseActivity as MainActivity).getNavController().popBackStack(R.id.nav_driver_listing, false)
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
    }

    private fun setDriverSpinner() {
        val adapter = VWSSpinnerAdapter(requireContext(), R.layout.simple_dropdown_item, driverList)
        binding?.spDriverName?.adapter = adapter

        if (driverId.isNotEmpty()) {
            val driver: FilterItem? = driverList.find { it.dbValue.equals(driverId, false) }
            val spinnerPosition: Int = driverList.indexOf(driver)
            selectedDriver = driver.toString()
            spinnerPosition.let { binding?.spDriverName?.setSelection(it) }
        }

        binding?.spDriverName?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                selectedDriver = driverList[binding?.spDriverName?.selectedItemPosition!!].dbValue
                if (selectedDriver.isNotEmpty()) {
                    adapter.setSelectedPosition(i)
                    binding?.tvDriverName?.text = driverList[binding?.spDriverName?.selectedItemPosition!!].displayValue
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }
    }

    private fun setPaymentModeSpinner() {

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

    override fun onClick(v: View?) {
        var selectedDate: Date? = null
        when (v?.id) {

            R.id.ivBack,
            R.id.btnDiscard -> baseActivity?.onBackPressed()

            R.id.btnAdd -> {
                if (isValid()) {
                    showProgress()
                    callAddUpdateDriverExpenseApi()
                }
            }

            R.id.ivUp -> binding?.nsView?.smoothScrollTo(0, 0)

            R.id.tvDriverName -> binding?.spDriverName?.performClick()

            R.id.tvDate -> {
                selectedDate = CommonUtils.getDateFromDisplay(apiPaymentDate)

                baseActivity?.setNormalCalender(
                    object : CaldroidListener() {
                        override fun onSelectDate(date1: Date?, view: View?) {
                            binding?.tvDate?.setText(date1.toString())
                            selectedDate = date1
                            apiPaymentDate = CommonUtils.getFormattedDateFromV2(date1).toString()
                            displayPaymentDate = CommonUtils.getFormattedDateFrom(date1).toString()
                            binding?.tvDate?.setText(displayPaymentDate)
                        }
                    }, selectedDate, null, Date()
                )
            }

            R.id.tvPaymentMode -> binding?.spPaymentMode?.performClick()
            R.id.tvDriverPaymentDoneBy -> binding?.spDriverPaymentDoneBy?.performClick()
        }
    }

    private fun isValid(): Boolean {
        return if (isPermanentDriver && binding?.tvDriverName?.text?.isEmpty() == true) {
            CommonUtils.showToast(requireContext(), getString(R.string.please_select_driver))
            false
        } else if (isOtherDriver && binding?.etDriverName?.text?.isEmpty() == true) {
            CommonUtils.showToast(requireContext(), getString(R.string.please_enter_driver_name))
            false
        } else if (binding?.tvDate?.text?.isEmpty() == true) {
            CommonUtils.showToast(requireContext(), getString(R.string.please_select_date))
            false
        } else if (binding?.etPaymentAmount?.text?.isEmpty() == true) {
            CommonUtils.showToast(requireContext(), getString(R.string.please_enter_payment_amount))
            false
        } else if (isExtraPayment && binding?.etExtraPayment?.text?.isEmpty() == true) {
            CommonUtils.showToast(requireContext(), getString(R.string.please_enter_extra_payment_given))
            false
        } else if (binding?.spPaymentMode?.selectedItemPosition == 0) {
            CommonUtils.showToast(requireContext(), getString(R.string.please_select_payment_mode))
            false
        } else {
            true
        }
    }

    private fun callAddUpdateDriverExpenseApi() {
        addUpdateDriverExpenseRequest.driverExpenceId = if (isForDriverUpdate) driverUpdateId else ""
//        addUpdateDriverExpenseRequest.driverId = if (driverId.isEmpty()) driverId else driverNameId

        addUpdateDriverExpenseRequest.driverId = selectedDriver.ifEmpty { driverNameId }
        addUpdateDriverExpenseRequest.driverName = binding?.etDriverName?.text.toString()
        addUpdateDriverExpenseRequest.date = apiPaymentDate
        addUpdateDriverExpenseRequest.amount = binding?.etPaymentAmount?.numericValue.toString()
        addUpdateDriverExpenseRequest.extraPaymentAmount = binding?.etExtraPayment?.numericValue.toString()
        addUpdateDriverExpenseRequest.paymentMode = selectedPaymentMode
        addUpdateDriverExpenseRequest.description = binding?.etDescription?.text.toString()

        Log.d("Add Driver Request", "callAddUpdateDriverExpenseApi:" + Gson().toJson(addUpdateDriverExpenseRequest))

        viewModel?.callAddUpdateDriverExpenseApi(addUpdateDriverExpenseRequest)
    }

    private fun callDriverDetailApi() {
        baseActivity?.showProgress()
        val driverIdRequest = DriverIdRequest()
        driverIdRequest.driverExpenceId = driverUpdateId
        viewModel?.callDriverDetailApi(driverIdRequest)
    }

    private fun updateUI(it: DriverData) {

        if (it.driver?.isParmanent == true) {
            binding?.clDriverTypeRadio?.rb1?.isChecked = true
            driverId = it.driver?.id.toString()
            setDriverSpinner()
        } else {
            binding?.clDriverTypeRadio?.rb2?.isChecked = true
            binding?.etDriverName?.setText(it.driver?.driverName)
            driverNameId = it.driver?.id.toString()
            binding?.etDriverName?.dismissDropDown()
        }

        binding?.tvDate?.text = it.dateReadable.toString()
        apiPaymentDate = CommonUtils.getServerDateFromDisplay(it.dateReadable).toString()

        binding?.etPaymentAmount?.setText(it.amount.toString())

        if (it.isExtrapayment == 1) {
            binding?.clExtraPaymentRadio?.rb1?.isChecked = true
            extraPayment = 1
        } else {
            binding?.clExtraPaymentRadio?.rb2?.isChecked = true
            extraPayment = 0
        }

        binding?.etExtraPayment?.setText(it.extraPaymentAmount.toString())

        paymentModeId = it.paymentMode.toString()
        setPaymentModeSpinner()

        binding?.etDescription?.setText(it.description)

        driverPaymentDoneById = ""
        setDriverPaymentDoneBy()
    }

    private fun setDriverPaymentDoneBy() {

        val driverPaymentDoneByList = ArrayList<FilterItem>()
        if (driverPaymentDoneByList.isEmpty()) {
            driverPaymentDoneByList.add(0, FilterItem("", getString(R.string.please_select_driver_payment_done_by)))
            driverPaymentDoneByList.add(FilterItem(getString(R.string.by_ajit), getString(R.string.by_ajit)))
            driverPaymentDoneByList.add(FilterItem(getString(R.string.by_nitish), getString(R.string.by_nitish)))
            driverPaymentDoneByList.add(FilterItem(getString(R.string.by_bussiness_card), getString(R.string.by_bussiness_card)))
        }
        val adapter = VWSSpinnerAdapter(requireContext(), R.layout.simple_dropdown_item, driverPaymentDoneByList)
        binding?.spDriverPaymentDoneBy?.adapter = adapter

        if (driverPaymentDoneById.isNotEmpty()) {
            val doneBy: FilterItem? = driverPaymentDoneByList.find { it.dbValue.equals(driverPaymentDoneById, false) }
            val spinnerPosition: Int = driverPaymentDoneByList.indexOf(doneBy)
            selectedDriverPaymentDoneBy = doneBy.toString()
            spinnerPosition.let { binding?.spDriverPaymentDoneBy?.setSelection(it) }
        }

        binding?.spDriverPaymentDoneBy?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                selectedDriverPaymentDoneBy = driverPaymentDoneByList[binding?.spDriverPaymentDoneBy?.selectedItemPosition!!].dbValue
                if (selectedDriverPaymentDoneBy.isNotEmpty()) {
                    adapter.setSelectedPosition(i)
                    binding?.tvDriverPaymentDoneBy?.text = driverPaymentDoneByList[binding?.spDriverPaymentDoneBy?.selectedItemPosition!!].displayValue
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }
    }
}