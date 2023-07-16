package com.production.vedantwatersupply.ui.maintenance

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.production.vedantwatersupply.R
import com.production.vedantwatersupply.core.BaseFragment
import com.production.vedantwatersupply.databinding.FragmentMaintenanceListingBinding
import com.production.vedantwatersupply.ui.trips.TripsAdapter
import com.production.vedantwatersupply.utils.filter.FilterListAdapter
import com.production.vedantwatersupply.utils.filter.SpaceItemDecoration
import com.transportermanger.util.filter.FilterItem
import com.transportermanger.util.filter.IFilterItem

class MaintenanceListingFragment : BaseFragment<FragmentMaintenanceListingBinding, MaintenanceViewModel>(), View.OnClickListener {

    private var monthFilterAdapter: FilterListAdapter? = null
    private var monthList = ArrayList<FilterItem>()
    private var monthId = ""

    override val layoutId: Int
        get() = R.layout.fragment_maintenance_listing

    override fun getViewModel(): Class<MaintenanceViewModel> = MaintenanceViewModel::class.java

    override fun init() {
        setScreenTitle()
        setSummary()
        initFilterView()
        setMaintenanceAdpter()
    }

    override fun initListener() {
        binding?.clHeader?.ivBack?.setOnClickListener(this)
        binding?.btnAdd?.setOnClickListener(this)
        binding?.btnFilter?.setOnClickListener(this)
    }

    private fun setScreenTitle() {
        binding?.clScreenTitle?.tvTitle?.text = getString(R.string.maintenance)
        binding?.clScreenTitle?.tvDescription?.text = getString(R.string.here_you_can_check_current_month_maintenance_expense_records)
    }

    private fun setSummary() {
        binding?.clSummary?.labelTotal?.text = getString(R.string.total_maintenance)
    }

    override fun addObserver() {

    }

    private fun initFilterView() {
        if (monthList.isEmpty()) {
            monthList.add(0, FilterItem("all", "All", true))
            monthList.add(FilterItem("july", "July"))
            monthList.add(FilterItem("june", "June"))
            monthList.add(FilterItem("may", "May"))
            monthList.add(FilterItem("april", "April"))
            monthList.add(FilterItem("march", "March"))
            monthList.add(FilterItem("february", "February"))
            monthList.add(FilterItem("january", "January"))
        }

        monthFilterAdapter = FilterListAdapter(monthList, object : IFilterItem {
            override fun onFilterItemSelected(view: View?, pos: Int) {
                monthId = monthList[pos].dbValue
//                resetAdapter()

            }
        })
        val resources = resources
        if (binding?.rvMonthFilter?.itemDecorationCount!! > 0) {
            binding?.rvMonthFilter?.removeItemDecorationAt(0)
        }
        binding?.rvMonthFilter?.addItemDecoration(SpaceItemDecoration(resources.getDimensionPixelSize(R.dimen._30sdp), resources.getDimensionPixelSize(R.dimen._10sdp), 0))
        binding?.rvMonthFilter?.adapter = monthFilterAdapter
    }

    private fun setMaintenanceAdpter() {
        val maintenanceAdapter = MaintenanceAdapter(requireContext())
        binding?.rvMaintanance?.adapter = maintenanceAdapter
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ivBack -> baseActivity?.onBackPressed()
            R.id.btnAdd -> navigateFragment(v, R.id.nav_add_maintenance)
            R.id.btnFilter -> {}
        }
    }
}