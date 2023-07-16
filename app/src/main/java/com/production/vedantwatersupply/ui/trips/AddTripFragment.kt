package com.production.vedantwatersupply.ui.trips

import android.view.View
import com.production.vedantwatersupply.R
import com.production.vedantwatersupply.core.BaseFragment
import com.production.vedantwatersupply.databinding.FragmentAddTripBinding

class AddTripFragment : BaseFragment<FragmentAddTripBinding, TripViewModel>(), View.OnClickListener {

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
    }

    override fun initListener() {
        binding?.clRadio?.rgCustom?.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == binding?.clRadio?.rb1?.id) {
                binding?.grpTruckSpinner?.visibility = View.VISIBLE
                binding?.edtTruck?.visibility = View.GONE
            } else {
                binding?.grpTruckSpinner?.visibility = View.GONE
                binding?.edtTruck?.visibility = View.VISIBLE
            }
        }
        binding?.clDriverRadio?.rgCustom?.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == binding?.clRadio?.rb1?.id) {
                binding?.grpDriverSpinner?.visibility = View.VISIBLE
                binding?.etDriverName?.visibility = View.GONE
            } else {
                binding?.grpDriverSpinner?.visibility = View.GONE
                binding?.etDriverName?.visibility = View.VISIBLE
            }
        }
        binding?.clHeader?.ivBack?.setOnClickListener(this)
        binding?.btnAdd?.setOnClickListener(this)
        binding?.btnDiscard?.setOnClickListener(this)
    }

    override fun addObserver() {}
    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.ivBack,
            R.id.btnDiscard -> baseActivity?.onBackPressed()

            R.id.btnAdd -> {}
        }
    }


}