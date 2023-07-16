package com.production.vedantwatersupply.ui.driver

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.production.vedantwatersupply.R
import com.production.vedantwatersupply.core.BaseFragment
import com.production.vedantwatersupply.databinding.FragmentAddDriverExpenseBinding


class AddDriverExpenseFragment : BaseFragment<FragmentAddDriverExpenseBinding, DriverViewModel>(), View.OnClickListener {


    override val layoutId: Int
        get() = R.layout.fragment_add_driver_expense

    override fun getViewModel(): Class<DriverViewModel> = DriverViewModel::class.java

    override fun init() {
        binding?.clScreenSummary?.tvTitle?.text = getString(R.string.add_driver_expense)
        binding?.clScreenSummary?.tvDescription?.text = getString(R.string.here_you_can_add_driver_expense_details)
        binding?.clDriverTypeRadio?.rb1?.text = getString(R.string.permanent_driver)
        binding?.clDriverTypeRadio?.rb2?.text = getString(R.string.other_driver)
        binding?.clExtraPaymentRadio?.rb1?.text = getString(R.string.yes)
        binding?.clExtraPaymentRadio?.rb2?.text = getString(R.string.no)
    }

    override fun initListener() {
        binding?.clDriverTypeRadio?.rgCustom?.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == binding?.clDriverTypeRadio?.rb1?.id) {
                binding?.grpDriverSpinner?.visibility = View.VISIBLE
                binding?.etDriverName?.visibility = View.GONE
            } else {
                binding?.grpDriverSpinner?.visibility = View.GONE
                binding?.etDriverName?.visibility = View.VISIBLE
            }
        }

        binding?.clExtraPaymentRadio?.rgCustom?.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == binding?.clDriverTypeRadio?.rb1?.id) {
                binding?.etExtraPayment?.visibility = View.VISIBLE
            } else {
                binding?.etExtraPayment?.visibility = View.GONE
            }
        }
        binding?.clHeader?.ivBack?.setOnClickListener(this)
        binding?.btnAdd?.setOnClickListener(this)
        binding?.btnDiscard?.setOnClickListener(this)
    }

    override fun addObserver() {

    }

    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.ivBack,
            R.id.btnDiscard -> baseActivity?.onBackPressed()

            R.id.btnAdd -> {}
        }
    }

}