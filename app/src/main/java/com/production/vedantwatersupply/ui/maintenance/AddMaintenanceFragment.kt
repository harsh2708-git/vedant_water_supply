package com.production.vedantwatersupply.ui.maintenance

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.production.vedantwatersupply.R
import com.production.vedantwatersupply.core.BaseFragment
import com.production.vedantwatersupply.databinding.FragmentAddMaintenanceBinding

class AddMaintenanceFragment : BaseFragment<FragmentAddMaintenanceBinding, MaintenanceViewModel>(), View.OnClickListener {

    override val layoutId: Int
        get() = R.layout.fragment_add_maintenance

    override fun getViewModel(): Class<MaintenanceViewModel> = MaintenanceViewModel::class.java

    override fun init() {
        binding?.clScreenSummary?.tvTitle?.text = getString(R.string.add_maintenance)
        binding?.clScreenSummary?.tvDescription?.text = getString(R.string.here_you_can_add_maintenance_details)
        binding?.clRadio?.rb1?.text = getString(R.string.own_tankers)
        binding?.clRadio?.rb2?.text = getString(R.string.other_tanker)
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
        binding?.clHeader?.ivBack?.setOnClickListener(this)
        binding?.btnAdd?.setOnClickListener(this)
        binding?.btnDiscard?.setOnClickListener(this)
        binding?.ivUp?.setOnClickListener(this)

        binding?.nsView?.viewTreeObserver?.addOnScrollChangedListener {
            val scrollY = binding?.nsView?.scrollY
            if (scrollY != null)
                binding?.ivUp?.visibility = if (scrollY > 0) View.VISIBLE else View.GONE
        }
    }

    override fun addObserver() {

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ivBack,
            R.id.btnDiscard -> baseActivity?.onBackPressed()

            R.id.btnAdd -> {}
            R.id.ivUp -> binding?.nsView?.smoothScrollTo(0, 0)
        }
    }

}