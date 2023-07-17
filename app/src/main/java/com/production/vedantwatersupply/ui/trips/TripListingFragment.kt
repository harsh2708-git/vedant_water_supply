package com.production.vedantwatersupply.ui.trips

import android.view.View
import com.production.vedantwatersupply.R
import com.production.vedantwatersupply.core.BaseFragment
import com.production.vedantwatersupply.databinding.FragmentTripListingBinding
import com.production.vedantwatersupply.ui.dialog.FilterDialogFragment
import com.production.vedantwatersupply.utils.filter.SpaceItemDecoration
import com.transportermanger.util.filter.FilterItem
import com.production.vedantwatersupply.utils.filter.FilterListAdapter
import com.transportermanger.util.filter.IFilterItem

class TripListingFragment : BaseFragment<FragmentTripListingBinding, TripViewModel>(), View.OnClickListener {

    private var monthFilterAdapter: FilterListAdapter? = null
    private var monthList = ArrayList<FilterItem>()
    private var monthId = ""

    override val layoutId: Int
        get() = R.layout.fragment_trip_listing

    override fun getViewModel(): Class<TripViewModel> = TripViewModel::class.java
    override fun init() {
        setScreenTitle()
        initFilterView()
        setTripsAdapter()
    }

    private fun setScreenTitle() {
        binding?.clScreenTitle?.tvTitle?.text = getString(R.string.trips)
        binding?.clScreenTitle?.tvDescription?.text = getString(R.string.here_you_can_check_current_month_trips_records)
    }

    override fun initListener() {
        binding?.clHeader?.ivBack?.setOnClickListener(this)
        binding?.btnAdd?.setOnClickListener(this)
        binding?.btnFilter?.setOnClickListener(this)
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

    private fun setTripsAdapter() {
        val tripAdapter = TripsAdapter(requireContext())
        binding?.rvTrips?.adapter = tripAdapter
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ivBack -> baseActivity?.onBackPressed()
            R.id.btnAdd -> navigateFragment(v, R.id.nav_add_trip)
            R.id.btnFilter -> openFilterDialog()
        }
    }

    private fun openFilterDialog() {
        val filterDialog = FilterDialogFragment()
        filterDialog.show(childFragmentManager, "Trip Filter Dialog")
    }
}