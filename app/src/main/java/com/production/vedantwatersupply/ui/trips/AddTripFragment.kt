package com.production.vedantwatersupply.ui.trips

import android.graphics.Path.FillType
import android.view.View
import android.widget.AdapterView
import com.production.vedantwatersupply.R
import com.production.vedantwatersupply.core.BaseFragment
import com.production.vedantwatersupply.custome.VWSSpinnerAdapter
import com.production.vedantwatersupply.databinding.FragmentAddTripBinding
import com.transportermanger.util.filter.FilterItem

class AddTripFragment : BaseFragment<FragmentAddTripBinding, TripViewModel>(), View.OnClickListener {

    private var tankerNoId = ""
    private var selectedTankerNo = ""

    private var paymentModeList = ArrayList<FilterItem>()
    private var paymentModeId = ""
    private var selectedPaymentMode = ""

    private var driverList = ArrayList<FilterItem>()
    private var driverId = ""
    private var selectedDriver = ""
    private var fillingSiteId = ""
    private var selectedFillingSite = ""

    private var fuelFilledById = ""
    private var selectedFuelFilledBy = ""

    override val layoutId: Int
        get() = R.layout.fragment_add_trip

    override fun getViewModel(): Class<TripViewModel> = TripViewModel::class.java

    override fun init() {
        binding?.clScreenSummary?.tvTitle?.text = getString(R.string.add_trip_details)
        binding?.clScreenSummary?.tvDescription?.text = getString(R.string.here_you_can_add_trip_details)
        binding?.clRadio?.rb1?.text = getString(R.string.own_tankers)
        binding?.clRadio?.rb2?.text = getString(R.string.other_tanker)
        binding?.clDriverRadio?.rb1?.text = getString(R.string.permanent_drivers)
        binding?.clDriverRadio?.rb2?.text = getString(R.string.other_driver)

        setTankerNoSpinner()
        setPaymentModeSpinner()
        setDriverSpinner()
        setFillingSiteSpinner()
        setFuelFilledBy()
    }

    override fun initListener() {
        binding?.clRadio?.rgCustom?.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == binding?.clRadio?.rb1?.id) {
                binding?.grpTruckSpinner?.visibility = View.VISIBLE
                binding?.etTankerNo?.visibility = View.GONE
            } else {
                binding?.grpTruckSpinner?.visibility = View.GONE
                binding?.etTankerNo?.visibility = View.VISIBLE
            }
        }
        binding?.clDriverRadio?.rgCustom?.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == binding?.clRadio?.rb1?.id) {
                binding?.grpDriverSpinner?.visibility = View.VISIBLE
                binding?.grpDriverNameMoNo?.visibility = View.GONE
            } else {
                binding?.grpDriverSpinner?.visibility = View.GONE
                binding?.grpDriverNameMoNo?.visibility = View.VISIBLE
            }
        }
        binding?.clHeader?.ivBack?.setOnClickListener(this)
        binding?.btnAdd?.setOnClickListener(this)
        binding?.btnDiscard?.setOnClickListener(this)
        binding?.tvTankerNo?.setOnClickListener(this)
        binding?.tvPaymentMode?.setOnClickListener(this)
        binding?.tvDriverName?.setOnClickListener(this)
        binding?.tvFillingSiteName?.setOnClickListener(this)
        binding?.tvFuelFilledBy?.setOnClickListener(this)
        binding?.ivUp?.setOnClickListener(this)

        binding?.nsView?.viewTreeObserver?.addOnScrollChangedListener {
            val scrollY = binding?.nsView?.scrollY
            if (scrollY != null)
                binding?.ivUp?.visibility = if (scrollY > 0) View.VISIBLE else View.GONE
        }
    }

    private fun setTankerNoSpinner() {

        val tankerNoList = ArrayList<FilterItem>()

        if (tankerNoList.isEmpty()) {
            tankerNoList.add(0, FilterItem("", getString(R.string.please_select_tanker_no)))
            tankerNoList.add(FilterItem(getString(R.string.mh_04_ds_6501), getString(R.string.mh_04_ds_6501)))
            tankerNoList.add(FilterItem(getString(R.string.mh_04_el_0809), getString(R.string.mh_04_el_0809)))
            tankerNoList.add(FilterItem(getString(R.string.mh_48_t_7857), getString(R.string.mh_48_t_7857)))
            tankerNoList.add(FilterItem(getString(R.string.mh_48_j_0158), getString(R.string.mh_48_j_0158)))
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

    private fun setDriverSpinner() {

        if (driverList.isEmpty()) {
            driverList.add(0, FilterItem("", getString(R.string.please_select_driver)))
            driverList.add(FilterItem(getString(R.string.bala), getString(R.string.bala)))
            driverList.add(FilterItem(getString(R.string.somar), getString(R.string.somar)))
            driverList.add(FilterItem(getString(R.string.mukund), getString(R.string.mukund)))
            driverList.add(FilterItem(getString(R.string.sagar), getString(R.string.sagar)))
            driverList.add(FilterItem(getString(R.string.wasim), getString(R.string.wasim)))
        }
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
    private fun setFillingSiteSpinner() {
        val fillingSiteList = ArrayList<FilterItem>()

        if (fillingSiteList.isEmpty()) {
            fillingSiteList.add(0, FilterItem("", getString(R.string.please_select_filling_site)))
            fillingSiteList.add(FilterItem(getString(R.string.cidco_water), getString(R.string.cidco_water)))
            fillingSiteList.add(FilterItem(getString(R.string.midc_water), getString(R.string.midc_water)))
            fillingSiteList.add(FilterItem(getString(R.string.borewell_water), getString(R.string.borewell_water)))
        }
        val adapter = VWSSpinnerAdapter(requireContext(), R.layout.simple_dropdown_item, fillingSiteList)
        binding?.spFillingSiteName?.adapter = adapter

        if (fillingSiteId.isNotEmpty()) {
            val fillingSite: FilterItem? = fillingSiteList.find { it.dbValue.equals(fillingSiteId, false) }
            val spinnerPosition: Int = fillingSiteList.indexOf(fillingSite)
            selectedFillingSite = fillingSite.toString()
            spinnerPosition.let { binding?.spFillingSiteName?.setSelection(it) }
        }

        binding?.spFillingSiteName?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                selectedFillingSite = fillingSiteList[binding?.spFillingSiteName?.selectedItemPosition!!].dbValue
                if (selectedFillingSite.isNotEmpty()) {
                    adapter.setSelectedPosition(i)
                    binding?.tvFillingSiteName?.text = fillingSiteList[binding?.spFillingSiteName?.selectedItemPosition!!].displayValue
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }
    }

    private fun setFuelFilledBy(){

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

    override fun addObserver() {}
    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.ivBack,
            R.id.btnDiscard -> baseActivity?.onBackPressed()

            R.id.btnAdd -> {}

            R.id.tvTankerNo -> binding?.spTankerNo?.performClick()
            R.id.tvPaymentMode -> binding?.spPaymentMode?.performClick()
            R.id.tvDriverName -> binding?.spDriverName?.performClick()
            R.id.tvFillingSiteName -> binding?.spFillingSiteName?.performClick()
            R.id.tvFuelFilledBy -> binding?.spFuelFilledBy?.performClick()

            R.id.ivUp -> binding?.nsView?.smoothScrollTo(0, 0)
        }
    }


}