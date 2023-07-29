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
import com.production.vedantwatersupply.listener.TripFilterClickListener
import com.production.vedantwatersupply.utils.AppConstants.Trip.Companion.OTHER_TANKER
import com.production.vedantwatersupply.utils.AppConstants.Trip.Companion.OWN_TANKER
import com.production.vedantwatersupply.utils.CommonUtils
import com.production.vedantwatersupply.utils.calendar.CaldroidListener
import com.production.vedantwatersupply.utils.filter.FilterItem
import java.util.Date

class FilterDialogFragment : BaseDialogFragment(), View.OnClickListener {

    private var binding: FragmentFilterDialogBinding? = null

    private var yearList = ArrayList<FilterItem>()
    private var tankerNoList = ArrayList<FilterItem>()
    private var paymentModeList = ArrayList<FilterItem>()
    private var driverList = ArrayList<FilterItem>()
    private var waterTypeList = ArrayList<FilterItem>()
    private var addedByList = ArrayList<FilterItem>()

    private var yearId = ""
    private var tankerTypeId = ""
    private var tankerNoId = ""
    private var paymentModeId = ""
    private var driverTypeId = ""
    private var driverId = ""
    private var fillingSiteId = ""
    private var waterTypeId = ""
    private var addedById = ""
    private var statusId = ""
    private var fuelFilledById = ""

    private var selectedYear = ""
    private var selectedTankerType = ""
    private var selectedTankerNo = ""
    private var selectedPaymentMode = ""
    private var selectedDriverType = ""
    private var selectedDriver = ""
    private var selectedFillingSite = ""
    private var selectedWaterType = ""
    private var selectedAddedBy = ""
    private var selectedStatus = ""
    private var selectedFuelFilledBy = ""

    private var fromDate = ""
    private var toDate = ""
    private var displayFromDate = ""
    private var displayToDate = ""

    private var callBack: TripFilterClickListener? = null

    companion object {
        fun getInstance(
            selectedYear: String,
            selectedTankerType: String,
            selectedTankerNo: String,
            selectedPaymentMode: String,
            selectedDriverType: String,
            selectedDriver: String,
            selectedFillingSite: String,
            selectedWaterType: String,
            selectedAddedBy: String,
            selectedStatus: String,
            selectedFuelFilledBy: String,
            fromDate: String,
            toDate: String,
            displayFromDate: String,
            displayToDate: String,
            tankerList: ArrayList<FilterItem>,
            driverList: ArrayList<FilterItem>,
            waterType: ArrayList<FilterItem>,
            addedBy: ArrayList<FilterItem>,
            yearList: ArrayList<FilterItem>,
            listener: TripFilterClickListener
        ): FilterDialogFragment {
            val fragment = FilterDialogFragment()
            fragment.yearId = selectedYear
            fragment.tankerTypeId = selectedTankerType
            fragment.tankerNoId = selectedTankerNo
            fragment.paymentModeId = selectedPaymentMode
            fragment.driverTypeId = selectedDriverType
            fragment.driverId = selectedDriver
            fragment.fillingSiteId = selectedFillingSite
            fragment.waterTypeId = selectedWaterType
            fragment.addedById = selectedAddedBy
            fragment.statusId = selectedStatus
            fragment.fuelFilledById = selectedFuelFilledBy
            fragment.fromDate = fromDate
            fragment.toDate = toDate
            fragment.displayFromDate = displayFromDate
            fragment.displayToDate = displayToDate
            fragment.tankerNoList = tankerList
            fragment.driverList = driverList
            fragment.waterTypeList = waterType
            fragment.addedByList = addedBy
            fragment.yearList = yearList
            fragment.callBack = listener
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.DialogTheme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFilterDialogBinding.inflate(inflater, container, false)
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
        binding?.tvPaymentMode?.setOnClickListener(this)
        binding?.tvDriverType?.setOnClickListener(this)
        binding?.tvDriver?.setOnClickListener(this)
        binding?.tvFillingSite?.setOnClickListener(this)
        binding?.tvWaterType?.setOnClickListener(this)
        binding?.tvAddedBy?.setOnClickListener(this)
        binding?.tvStatus?.setOnClickListener(this)
        binding?.tvFromDate?.setOnClickListener(this)
        binding?.tvToDate?.setOnClickListener(this)
        binding?.tvFuelFilledBy?.setOnClickListener(this)
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
        setTankerTypeSpinner()
        setTankerNoSpinner()
        setPaymentModeSpinner()
        setDriverTypeSpinner()
        setDriverSpinner()
        setFillingSiteSpinner()
        setWaterTypeSpinner()
        setAddedBySpinner()
        setStatusSpinner()
        setFuelFilledBy()
    }

    private fun setYearSpinner() {
        /*if (yearList.isEmpty()) {
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

    private fun setTankerTypeSpinner() {
        val tankerTypeList = ArrayList<FilterItem>()

        if (tankerTypeList.isEmpty()) {
            tankerTypeList.add(0, FilterItem("", getString(R.string.please_select_tanker_type)))
            tankerTypeList.add(FilterItem(OWN_TANKER, getString(R.string.own_tanker)))
            tankerTypeList.add(FilterItem(OTHER_TANKER, getString(R.string.other_tanker)))
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

       /* if (tankerNoList.isEmpty()) {

            tankerNoList.add(FilterItem("MH 01 AB  4545", "MH 01 AB  4545"))
            tankerNoList.add(FilterItem("MH 02 AV  4545", "MH 02 AV  4545"))
        }*/

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

    private fun setFillingSiteSpinner() {
        val fillingSiteList = ArrayList<FilterItem>()

        if (fillingSiteList.isEmpty()) {
            fillingSiteList.add(0, FilterItem("", getString(R.string.please_select_filling_site)))
            fillingSiteList.add(FilterItem(getString(R.string.cidco_water), getString(R.string.cidco_water)))
            fillingSiteList.add(FilterItem(getString(R.string.midc_water), getString(R.string.midc_water)))
            fillingSiteList.add(FilterItem(getString(R.string.borewell_water), getString(R.string.borewell_water)))
        }
        val adapter = VWSSpinnerAdapter(requireContext(), R.layout.simple_dropdown_item, fillingSiteList)
        binding?.spFillingSite?.adapter = adapter

        if (fillingSiteId.isNotEmpty()) {
            val fillingSite: FilterItem? = fillingSiteList.find { it.dbValue.equals(fillingSiteId, false) }
            val spinnerPosition: Int = fillingSiteList.indexOf(fillingSite)
            selectedFillingSite = fillingSite.toString()
            spinnerPosition.let { binding?.spFillingSite?.setSelection(it) }
        }

        binding?.spFillingSite?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                selectedFillingSite = fillingSiteList[binding?.spFillingSite?.selectedItemPosition!!].dbValue
                if (selectedFillingSite.isNotEmpty()) {
                    adapter.setSelectedPosition(i)
                    binding?.tvFillingSite?.text = fillingSiteList[binding?.spFillingSite?.selectedItemPosition!!].displayValue
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }
    }

    private fun setWaterTypeSpinner() {

        /*if (waterTypeList.isEmpty()) {

            waterTypeList.add(FilterItem("Drinking Water", "Drinking Water"))
            waterTypeList.add(FilterItem("Normal Water", "Normal Water"))
        }*/


        val adapter = VWSSpinnerAdapter(requireContext(), R.layout.simple_dropdown_item, waterTypeList)
        binding?.spWaterType?.adapter = adapter

        if (waterTypeId.isNotEmpty()) {
            val waterType: FilterItem? = waterTypeList.find { it.dbValue.equals(waterTypeId, false) }
            val spinnerPosition: Int = waterTypeList.indexOf(waterType)
            selectedWaterType = waterType.toString()
            spinnerPosition.let { binding?.spWaterType?.setSelection(it) }
        }

        binding?.spWaterType?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                selectedWaterType = waterTypeList[binding?.spWaterType?.selectedItemPosition!!].dbValue
                if (selectedWaterType.isNotEmpty()) {
                    adapter.setSelectedPosition(i)
                    binding?.tvWaterType?.text = waterTypeList[binding?.spWaterType?.selectedItemPosition!!].displayValue
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }
    }

    private fun setAddedBySpinner() {

        /*if (addedByList.isEmpty()) {
            addedByList.add(0, FilterItem("", getString(R.string.please_select_added_by)))
            addedByList.add(FilterItem("Harsh Prajapati", "Harsh Prajapati"))
            addedByList.add(FilterItem("Ravi Paramar", "Harsh Paramar"))
        }*/

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

    private fun setStatusSpinner() {

        val statusList = ArrayList<FilterItem>()

        if (statusList.isEmpty()) {
            statusList.add(0, FilterItem("", getString(R.string.please_select_status)))
            statusList.add(FilterItem(getString(R.string.cancelled), getString(R.string.cancelled)))
        }
        val adapter = VWSSpinnerAdapter(requireContext(), R.layout.simple_dropdown_item, statusList)
        binding?.spStatus?.adapter = adapter

        if (statusId.isNotEmpty()) {
            val status: FilterItem? = statusList.find { it.dbValue.equals(statusId, false) }
            val spinnerPosition: Int = statusList.indexOf(status)
            selectedStatus = status.toString()
            spinnerPosition.let { binding?.spStatus?.setSelection(it) }
        }

        binding?.spStatus?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                selectedStatus = statusList[binding?.spStatus?.selectedItemPosition!!].dbValue
                if (selectedStatus.isNotEmpty()) {
                    adapter.setSelectedPosition(i)
                    binding?.tvStatus?.text = statusList[binding?.spStatus?.selectedItemPosition!!].displayValue
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }
    }

    private fun setFuelFilledBy() {

        val fuelFilledByList = ArrayList<FilterItem>()
        if (fuelFilledByList.isEmpty()) {
            fuelFilledByList.add(0, FilterItem("", getString(R.string.please_select_fuel_filled_by)))
            fuelFilledByList.add(FilterItem(getString(R.string.by_ajit), getString(R.string.by_ajit)))
            fuelFilledByList.add(FilterItem(getString(R.string.by_nitish), getString(R.string.by_nitish)))
            fuelFilledByList.add(FilterItem(getString(R.string.by_bussiness_card), getString(R.string.by_bussiness_card)))
        }
        val adapter = VWSSpinnerAdapter(requireContext(), R.layout.simple_dropdown_item, fuelFilledByList)
        binding?.spFuelFilledBy?.adapter = adapter

        if (fuelFilledById.isNotEmpty()) {
            val filledBy: FilterItem? = fuelFilledByList.find { it.dbValue.equals(fuelFilledById, false) }
            val spinnerPosition: Int = fuelFilledByList.indexOf(filledBy)
            selectedFuelFilledBy = filledBy.toString()
            spinnerPosition.let { binding?.spFuelFilledBy?.setSelection(it) }
        }

        binding?.spFuelFilledBy?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                selectedFuelFilledBy = fuelFilledByList[binding?.spFuelFilledBy?.selectedItemPosition!!].dbValue
                if (selectedFuelFilledBy.isNotEmpty()) {
                    adapter.setSelectedPosition(i)
                    binding?.tvFuelFilledBy?.text = fuelFilledByList[binding?.spFuelFilledBy?.selectedItemPosition!!].displayValue
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
                    selectedYear, selectedTankerType, selectedTankerNo,
                    selectedPaymentMode, selectedFuelFilledBy, selectedDriverType,
                    selectedDriver, selectedWaterType, selectedAddedBy,
                    selectedStatus, selectedFillingSite
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

            R.id.tvTanker -> binding?.spTanker?.performClick()
            R.id.tvTankerNo -> binding?.spTankerNo?.performClick()
            R.id.tvPaymentMode -> binding?.spPaymentMode?.performClick()
            R.id.tvDriverType -> binding?.spDriverType?.performClick()
            R.id.tvDriver -> binding?.spDriver?.performClick()
            R.id.tvFillingSite -> binding?.spFillingSite?.performClick()
            R.id.tvWaterType -> binding?.spWaterType?.performClick()
            R.id.tvAddedBy -> binding?.spAddedBy?.performClick()
            R.id.tvStatus -> binding?.spStatus?.performClick()
            R.id.tvFuelFilledBy -> binding?.spFuelFilledBy?.performClick()
        }
    }

    private fun resetFilter() {
        binding?.spYear?.setSelection(0)
        binding?.spTanker?.setSelection(0)
        binding?.spTankerNo?.setSelection(0)
        binding?.spPaymentMode?.setSelection(0)
        binding?.spDriverType?.setSelection(0)
        binding?.spDriver?.setSelection(0)
        binding?.spFillingSite?.setSelection(0)
        binding?.spWaterType?.setSelection(0)
        binding?.spAddedBy?.setSelection(0)
        binding?.spStatus?.setSelection(0)
        binding?.spFuelFilledBy?.setSelection(0)

        binding?.tvYear?.isEnabled = true
        binding?.tvFromDate?.isEnabled = true
        binding?.tvToDate?.isEnabled = true

        yearId = ""
        tankerTypeId = ""
        tankerNoId = ""
        paymentModeId = ""
        driverTypeId = ""
        driverId = ""
        fillingSiteId = ""
        waterTypeId = ""
        addedById = ""
        statusId = ""
        fuelFilledById = ""

        selectedYear = ""
        selectedTankerType = ""
        selectedTankerNo = ""
        selectedPaymentMode = ""
        selectedDriverType = ""
        selectedDriver = ""
        selectedFillingSite = ""
        selectedWaterType = ""
        selectedAddedBy = ""
        selectedStatus = ""
        selectedFuelFilledBy = ""

        fromDate = ""
        toDate = ""
        displayFromDate = ""
        displayToDate = ""
    }

}