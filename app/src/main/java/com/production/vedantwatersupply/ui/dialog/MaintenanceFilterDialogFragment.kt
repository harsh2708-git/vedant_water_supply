package com.production.vedantwatersupply.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import com.production.vedantwatersupply.R
import com.production.vedantwatersupply.core.BaseDialogFragment
import com.production.vedantwatersupply.custome.VWSSpinnerAdapter
import com.production.vedantwatersupply.databinding.FragmentFilterDialogBinding
import com.production.vedantwatersupply.databinding.FragmentMaintenanceFilterDialogBinding
import com.production.vedantwatersupply.utils.CommonUtils
import com.production.vedantwatersupply.utils.calendar.CaldroidListener
import com.transportermanger.util.filter.FilterItem
import java.util.Date

class MaintenanceFilterDialogFragment : BaseDialogFragment(), View.OnClickListener {

    private var binding: FragmentMaintenanceFilterDialogBinding? = null

    private var yearList = ArrayList<FilterItem>()
    private var yearId = ""
    private var selectedYear = ""

    private var tankerTypeId = ""
    private var selectedTankerType = ""

    private var tankerNoList = ArrayList<FilterItem>()
    private var tankerNoId = ""
    private var selectedTankerNo = ""

    private var driverTypeId = ""
    private var selectedDriverType = ""

    private var fromDate = ""
    private var toDate = ""

    private var displayFromDate: String? = null
    private var displayToDate: String? = null

    private var paymentModeList = ArrayList<FilterItem>()
    private var paymentModeId = ""
    private var selectedPaymentMode = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.DialogTheme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMaintenanceFilterDialogBinding.inflate(inflater, container, false)
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
        binding?.tvTanker?.setOnClickListener(this)
        binding?.tvTankerNo?.setOnClickListener(this)
        binding?.tvFromDate?.setOnClickListener(this)
        binding?.tvToDate?.setOnClickListener(this)
    }

    private fun init() {
        setYearSpinner()
        setTankerTypeSpinner()
        setTankerNoSpinner()
        setPaymentModeSpinner()
    }

    private fun setYearSpinner() {
        if (yearList.isEmpty()) {
            yearList.add(0, FilterItem("", getString(R.string.please_select_year)))
            yearList.add(FilterItem("2023", "2023"))
            yearList.add(FilterItem("2022", "2022"))
            yearList.add(FilterItem("2021", "2021"))
        }
        val adapter = VWSSpinnerAdapter(requireContext(), R.layout.simple_dropdown_item, yearList)
        binding?.spYear?.adapter = adapter

        if (yearId.isNotEmpty()) {
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

    private fun setTankerTypeSpinner() {
        val tankerTypeList = ArrayList<FilterItem>()

        if (tankerTypeList.isEmpty()) {
            tankerTypeList.add(0, FilterItem("", getString(R.string.please_select_tanker_type)))
            tankerTypeList.add(FilterItem(getString(R.string.own_tanker), getString(R.string.own_tanker)))
            tankerTypeList.add(FilterItem(getString(R.string.other_tanker), getString(R.string.other_tanker)))
        }
        val adapter = VWSSpinnerAdapter(requireContext(), R.layout.simple_dropdown_item, tankerTypeList)
        binding?.spTanker?.adapter = adapter

        if (tankerTypeId.isNotEmpty()) {
            val year: FilterItem? = tankerTypeList.find { it.dbValue.equals(tankerTypeId, false) }
            val spinnerPosition: Int = tankerTypeList.indexOf(year)
            selectedTankerType = year.toString()
            spinnerPosition.let { binding?.spTanker?.setSelection(it) }
        }

        binding?.spTanker?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                selectedTankerType = tankerTypeList[binding?.spTanker?.selectedItemPosition!!].dbValue
                if (selectedTankerType.isNotEmpty()) {
                    adapter.setSelectedPosition(i)
                    binding?.tvTanker?.text = tankerTypeList[binding?.spTanker?.selectedItemPosition!!].displayValue
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }
    }

    private fun setTankerNoSpinner() {

        if (tankerNoList.isEmpty()) {
            tankerNoList.add(0, FilterItem("", getString(R.string.please_select_tanker_no)))
            tankerNoList.add(FilterItem("MH 01 AB  4545", "MH 01 AB  4545"))
            tankerNoList.add(FilterItem("MH 02 AV  4545", "MH 02 AV  4545"))
        }
        val adapter = VWSSpinnerAdapter(requireContext(), R.layout.simple_dropdown_item, tankerNoList)
        binding?.spTankerNo?.adapter = adapter

        if (tankerNoId.isNotEmpty()) {
            val takerNo: FilterItem? = tankerNoList.find { it.dbValue.equals(tankerNoId, false) }
            val spinnerPosition: Int = tankerNoList.indexOf(takerNo)
            selectedTankerNo = takerNo.toString()
            spinnerPosition.let { binding?.spTankerNo?.setSelection(it) }
        }

        binding?.spTankerNo?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                selectedTankerNo = tankerNoList[binding?.spTankerNo?.selectedItemPosition!!].dbValue
                if (selectedTankerNo.isNotEmpty()) {
                    adapter.setSelectedPosition(i)
                    binding?.tvTankerNo?.text = tankerNoList[binding?.spTankerNo?.selectedItemPosition!!].displayValue
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

            R.id.tvFromDate -> {
                selectedDate = CommonUtils.getDateFromDisplay(fromDate)

                baseActivity?.setNormalCalender(
                    object : CaldroidListener() {
                        override fun onSelectDate(date1: Date?, view: View?) {
                            binding?.tvFromDate?.setText(date1.toString())
                            selectedDate = date1
                            fromDate = CommonUtils.getFormattedDateFromV2(date1).toString()
                            displayFromDate = CommonUtils.getFormattedDateFrom(date1)
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
                            displayToDate = CommonUtils.getFormattedDateFrom(date1)
                            binding?.tvToDate?.setText(displayToDate)
                        }
                    },
                    selectedDate, CommonUtils.getDateFromDisplay(binding?.tvFromDate?.text.toString()), Date()
                )

            }

            R.id.btnClose -> dismiss()

            R.id.btnApply -> {
                dismiss()
            }

            R.id.btnClear -> {
                resetFilter()
                dismiss()
            }

            R.id.tvYear -> binding?.spYear?.performClick()
            R.id.tvTanker -> binding?.spTanker?.performClick()
            R.id.tvTankerNo -> binding?.spTankerNo?.performClick()
            R.id.tvPaymentMode -> binding?.spPaymentMode?.performClick()
        }
    }

    private fun resetFilter() {
        binding?.spYear?.setSelection(0)
        binding?.spTanker?.setSelection(0)
        binding?.spTankerNo?.setSelection(0)
        binding?.spPaymentMode?.setSelection(0)
    }

}