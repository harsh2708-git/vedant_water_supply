package com.production.vedantwatersupply.ui.trips

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
import com.production.vedantwatersupply.databinding.FragmentTripDetailBinding
import com.production.vedantwatersupply.databinding.LayoutOptionsBinding


class TripDetailFragment : BaseFragment<FragmentTripDetailBinding, TripViewModel>(), View.OnClickListener {

    override val layoutId: Int
        get() = R.layout.fragment_trip_detail

    override fun getViewModel(): Class<TripViewModel> = TripViewModel::class.java

    override fun init() {

        binding?.clScreenTitle?.tvTitle?.text = getString(R.string.trip_detail)
        binding?.clScreenTitle?.tvDescription?.text = getString(R.string.here_you_can_check_trip_detail)
    }

    override fun initListener() {
        binding?.clHeader?.ivBack?.setOnClickListener(this)
        binding?.ivOptions?.setOnClickListener(this)
        binding?.ivUp?.setOnClickListener(this)
    }

    override fun addObserver() {

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ivBack -> baseActivity?.onBackPressed()
            R.id.ivOptions -> showOptionMenu(v)
            R.id.ivUp -> {}
        }
    }

    private fun showOptionMenu(view: View) {
        val inflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val vieww = inflater.inflate(R.layout.layout_options, null)
        val popupWindow = PopupWindow(vieww, requireContext().resources.getDimensionPixelSize(R.dimen._250sdp), LinearLayout.LayoutParams.WRAP_CONTENT, true)
        val binding: LayoutOptionsBinding = LayoutOptionsBinding.bind(vieww)
        popupWindow.showAsDropDown(view)

        binding.llCancel.visibility = View.VISIBLE
        binding.tvEdit.text = getString(R.string.edit_trip)
        binding.tvDelete.text = getString(R.string.delete_trip)

        binding.llEdit.setOnClickListener {
            popupWindow.dismiss()
        }

        binding.llDelete.setOnClickListener {
            popupWindow.dismiss()
        }

        binding.llCancel.setOnClickListener {
            popupWindow.dismiss()
        }
    }

}