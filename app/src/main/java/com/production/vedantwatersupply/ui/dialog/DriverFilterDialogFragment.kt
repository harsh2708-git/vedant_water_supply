package com.production.vedantwatersupply.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import com.production.vedantwatersupply.R
import com.production.vedantwatersupply.core.BaseDialogFragment
import com.production.vedantwatersupply.custome.VWSSpinnerAdapter
import com.production.vedantwatersupply.databinding.FragmentDriverFilterDialogBinding
import com.production.vedantwatersupply.listener.DriverFilterClickListener
import com.production.vedantwatersupply.utils.CommonUtils
import com.production.vedantwatersupply.utils.calendar.CaldroidListener
import com.production.vedantwatersupply.utils.filter.FilterItem
import java.util.Date

class DriverFilterDialogFragment : BaseDialogFragment(), View.OnClickListener {

    private var binding: FragmentDriverFilterDialogBinding? = null

    private var yearList = ArrayList<FilterItem>()
    private var driverList = ArrayList<FilterItem>()
    private var paymentModeList = ArrayList<FilterItem>()
    private var addedByList = ArrayList<FilterItem>()

    private var yearId = ""
    private var driverTypeId = ""
    private var driverId = ""
    private var paymentModeId = ""
    private var addedById = ""

    private var selectedYear = ""
    private var selectedDriverType = ""
    private var selectedDriver = ""
    private var selectedPaymentMode = ""
    private var selectedAddedBy = ""

    private var fromDate = ""
    private var toDate = ""
    private var displayFromDate = ""
    private var displayToDate = ""

    private var callBack: DriverFilterClickListener? = null

    companion object {
        fun getInstance(
            selectedYear: String,
            selectedDriverType: String,
            selectedDriver: String,
            selectedPaymentMode: String,
            selectedAddedBy: String,
            fromDate: String,
            toDate: String,
            displayFromDate: String,
            displayToDate: String,
            yearList: ArrayList<FilterItem>,
            driverList: ArrayList<FilterItem>,
            addedBy: ArrayList<FilterItem>,
            listener: DriverFilterClickListener
        ): DriverFilterDialogFragment {
            val fragment = DriverFilterDialogFragment()
            fragment.yearId = selectedYear
            fragment.driverTypeId = selectedDriverType
            fragment.driverId = selectedDriver
            fragment.paymentModeId = selectedPaymentMode
            fragment.addedById = selectedAddedBy
            fragment.fromDate = fromDate
            fragment.toDate = toDate
            fragment.displayFromDate = displayFromDate
            fragment.displayToDate = displayToDate
            fragment.yearList= yearList
            fragment.driverList = driverList
            fragment.addedByList = addedBy
            fragment.callBack = listener
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.DialogTheme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDriverFilterDialogBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initListener()
    }

    private fun initListener() {
        binding?.btnClose?.setOnClickListener(this)
        binding?.btnApply?.setOnClickListener(this)
        binding?.btnClear?.setOnClickListener(this)
        binding?.tvYear?.setOnClickListener(this)
        binding?.tvDriverType?.setOnClickListener(this)
        binding?.tvDriver?.setOnClickListener(this)
        binding?.tvFromDate?.setOnClickListener(this)
        binding?.tvToDate?.setOnClickListener(this)
        binding?.tvPaymentMode?.setOnClickListener(this)
        binding?.tvAddedBy?.setOnClickListener(this)
    }

    private fun init() {
        if (!CommonUtils.isEmpty(displayFromDate)) {
            binding?.tvYear?.isEnabled = false
            binding?.tvYear?.alpha = 0.5f
            binding?.tvFromDate?.setText(displayFromDate)
        }
        if (!CommonUtils.isEmpty(displayToDate)) {
            binding?.tvToDate?.setText(displayToDate)
        }
        setYearSpinner()
        setPaymentModeSpinner()
        setDriverTypeSpinner()
        setDriverSpinner()
        setAddedBySpinner()
    }

    private fun setYearSpinner() {
       /* if (yearList.isEmpty()) {
            yearList.add(0, FilterItem("", getString(R.string.please_select_year)))
            yearList.add(FilterItem("2023", "2023"))
            yearList.add(FilterItem("2022", "2022"))
            yearList.add(FilterItem("2021", "2021"))
        }*/
        val adapter = VWSSpinnerAdapter(requireContext(), R.layout.simple_dropdown_item, yearList)
        binding?.spYear?.adapter = adapter

        if (yearId.isNotEmpty()) {
            binding?.tvFromDate?.isEnabled = false
            binding?.tvToDate?.isEnabled = false
            binding?.tvFromDate?.alpha = 0.5f
            binding?.tvToDate?.alpha = 0.5f

            val statusName: FilterItem? = yearList.find { it.dbValue.equals(yearId, false) }
            val spinnerPosition: Int = yearList.indexOf(statusName)
            selectedYear = statusName.toString()
            spinnerPosition.let { binding?.spYear?.setSelection(it) }
        }

        binding?.spYear?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                selectedYear = yearList[binding?.spYear?.selectedItemPosition!!].dbValue
                if (selectedYear.isNotEmpty()) {
                    adapter.setSelectedPosition(i)
                    binding?.tvYear?.text = yearList[binding?.spYear?.selectedItemPosition!!].displayValue
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }
    }

    private fun setDriverTypeSpinner() {
        val driverTypeList = ArrayList<FilterItem>()
        if (driverTypeList.isEmpty()) {
            driverTypeList.add(0, FilterItem("", getString(R.string.please_select_driver_type)))
            driverTypeList.add(FilterItem(getString(R.string.permanent_driver), getString(R.string.permanent_driver)))
            driverTypeList.add(FilterItem(getString(R.string.other_driver), getString(R.string.other_driver)))
        }
        val adapter = VWSSpinnerAdapter(requireContext(), R.layout.simple_dropdown_item, driverTypeList)
        binding?.spDriverType?.adapter = adapter

        if (driverTypeId.isNotEmpty()) {
            val driverType: FilterItem? = driverTypeList.find { it.dbValue.equals(driverTypeId, false) }
            val spinnerPosition: Int = driverTypeList.indexOf(driverType)
            selectedDriverType = driverType.toString()
            spinnerPosition.let { binding?.spDriverType?.setSelection(it) }
        }

        binding?.spDriverType?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                selectedDriverType = driverTypeList[binding?.spDriverType?.selectedItemPosition!!].dbValue
                if (selectedDriverType.isNotEmpty()) {
                    adapter.setSelectedPosition(i)
                    binding?.tvDriverType?.text = driverTypeList[binding?.spDriverType?.selectedItemPosition!!].displayValue
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }
    }

    private fun setDriverSpinner() {

        /*if (driverList.isEmpty()) {
            driverList.add(0, FilterItem("", getString(R.string.please_select_driver)))
            driverList.add(FilterItem("Harsh Prajapati", "Harsh Prajapati"))
            driverList.add(FilterItem("Ravi Paramar", "Harsh Paramar"))
        }*/
        val adapter = VWSSpinnerAdapter(requireContext(), R.layout.simple_dropdown_item, driverList)
        binding?.spDriver?.adapter = adapter

        if (driverId.isNotEmpty()) {
            val driver: FilterItem? = driverList.find { it.dbValue.equals(driverId, false) }
            val spinnerPosition: Int = driverList.indexOf(driver)
            selectedDriver = driver.toString()
            spinnerPosition.let { binding?.spDriver?.setSelection(it) }
        }

        binding?.spDriver?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                selectedDriver = driverList[binding?.spDriver?.selectedItemPosition!!].dbValue
                if (selectedDriver.isNotEmpty()) {
                    adapter.setSelectedPosition(i)
                    binding?.tvDriver?.text = driverList[binding?.spDriver?.selectedItemPosition!!].displayValue
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

    private fun setAddedBySpinner() {
        val adapter = VWSSpinnerAdapter(requireContext(), R.layout.simple_dropdown_item, addedByList)
        binding?.spAddedBy?.adapter = adapter

        if (addedById.isNotEmpty()) {
            val addedBy: FilterItem? = addedByList.find { it.dbValue.equals(addedById, false) }
            val spinnerPosition: Int = addedByList.indexOf(addedBy)
            selectedAddedBy = addedBy.toString()
            spinnerPosition.let { binding?.spAddedBy?.setSelection(it) }
        }

        binding?.spAddedBy?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                selectedAddedBy = addedByList[binding?.spAddedBy?.selectedItemPosition!!].dbValue
                if (selectedAddedBy.isNotEmpty()) {
                    adapter.setSelectedPosition(i)
                    binding?.tvAddedBy?.text = addedByList[binding?.spAddedBy?.selectedItemPosition!!].displayValue
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }
    }

    override fun onClick(v: View?) {
        var selectedDate: Date? = null
        when (v?.id) {

            R.id.tvFromDate -> {
                binding?.tvYear?.isEnabled = false
                binding?.tvYear?.alpha = 0.5f
                selectedDate = CommonUtils.getDateFromDisplay(fromDate)

                baseActivity?.setNormalCalender(
                    object : CaldroidListener() {
                        override fun onSelectDate(date1: Date?, view: View?) {
                            binding?.tvFromDate?.setText(date1.toString())
                            selectedDate = date1
                            fromDate = CommonUtils.getFormattedDateFromV2(date1).toString()
                            displayFromDate = CommonUtils.getFormattedDateFrom(date1).toString()
                            binding?.tvFromDate?.setText(displayFromDate)
                        }
                    },
                    selectedDate, null,
                    if (CommonUtils.isEmpty(binding?.tvToDate?.text.toString())) Date() else CommonUtils.getDateFromDisplay(binding?.tvToDate?.text.toString())
                )
            }

            R.id.tvToDate -> {

                selectedDate = CommonUtils.getDateFromDisplay(toDate)
                baseActivity?.setNormalCalender(
                    object : CaldroidListener() {
                        override fun onSelectDate(date1: Date?, view: View?) {
                            binding?.tvToDate?.setText(date1.toString())
                            selectedDate = date1
                            toDate = CommonUtils.getFormattedDateFromV2(date1).toString()
                            displayToDate = CommonUtils.getFormattedDateFrom(date1).toString()
                            binding?.tvToDate?.setText(displayToDate)
                        }
                    },
                    selectedDate, CommonUtils.getDateFromDisplay(binding?.tvFromDate?.text.toString()), Date()
                )

            }

            R.id.btnClose -> dismiss()

            R.id.btnApply -> {
                callBack?.onApply(
                    fromDate, displayFromDate, toDate, displayToDate,
                    selectedYear, selectedDriverType, selectedDriver,
                    selectedPaymentMode,selectedAddedBy
                )
                dismiss()
            }

            R.id.btnClear -> {
                callBack?.onClear()
                resetFilter()
                dismiss()
            }

            R.id.tvYear -> {
                binding?.tvFromDate?.isEnabled = false
                binding?.tvToDate?.isEnabled = false
                binding?.tvFromDate?.alpha = 0.5f
                binding?.tvToDate?.alpha = 0.5f
                binding?.spYear?.performClick()
            }

            R.id.tvDriverType -> binding?.spDriverType?.performClick()
            R.id.tvDriver -> binding?.spDriver?.performClick()
            R.id.tvPaymentMode -> binding?.spPaymentMode?.performClick()
            R.id.tvAddedBy -> binding?.spAddedBy?.performClick()
        }
    }

    private fun resetFilter() {
        binding?.spYear?.setSelection(0)
        binding?.spDriverType?.setSelection(0)
        binding?.spDriver?.setSelection(0)
        binding?.spPaymentMode?.setSelection(0)

        yearId = ""
        driverTypeId = ""
        driverId = ""
        paymentModeId = ""
        addedById = ""

        selectedYear = ""
        selectedDriverType = ""
        selectedDriver = ""
        selectedPaymentMode = ""
        selectedAddedBy = ""

        fromDate = ""
        toDate = ""
        displayFromDate = ""
        displayToDate = ""
    }

}