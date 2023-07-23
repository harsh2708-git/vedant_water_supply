package com.production.vedantwatersupply.ui.driver

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import com.production.vedantwatersupply.R
import com.production.vedantwatersupply.core.BaseFragment
import com.production.vedantwatersupply.databinding.FragmentDriverDetailBinding
import com.production.vedantwatersupply.databinding.LayoutOptionsBinding

class DriverDetailFragment : BaseFragment<FragmentDriverDetailBinding,DriverViewModel>(), View.OnClickListener {

    override val layoutId: Int
        get() = R.layout.fragment_driver_detail

    override fun getViewModel(): Class<DriverViewModel> = DriverViewModel::class.java

    override fun init() {
        binding?.clScreenTitle?.tvTitle?.text = getString(R.string.driver_expense_detail)
        binding?.clScreenTitle?.tvDescription?.text = getString(R.string.here_you_can_check_driver_expense_detail)
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

    override fun addObserver() {

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
            popupWindow.dismiss()
        }

        binding.llDelete.setOnClickListener {
            popupWindow.dismiss()
        }

    }

}