package com.production.vedantwatersupply.ui.maintenance

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
import com.production.vedantwatersupply.databinding.FragmentMaintenanceListingBinding
import com.production.vedantwatersupply.databinding.LayoutOptionsBinding
import com.production.vedantwatersupply.listener.MaintenanceFilterClickListener
import com.production.vedantwatersupply.listener.RecyclerViewClickListener
import com.production.vedantwatersupply.ui.dialog.FilterDialogFragment
import com.production.vedantwatersupply.ui.dialog.MaintenanceFilterDialogFragment
import com.production.vedantwatersupply.ui.trips.TripsAdapter
import com.production.vedantwatersupply.utils.CommonUtils
import com.production.vedantwatersupply.utils.filter.FilterListAdapter
import com.production.vedantwatersupply.utils.filter.SpaceItemDecoration
import com.transportermanger.util.filter.FilterItem
import com.transportermanger.util.filter.IFilterItem

class MaintenanceListingFragment : BaseFragment<FragmentMaintenanceListingBinding, MaintenanceViewModel>(), View.OnClickListener, RecyclerViewClickListener {

    private var monthFilterAdapter: FilterListAdapter? = null
    private var monthList = ArrayList<FilterItem>()
    private var monthId = ""

    private var selectedYear = ""
    private var selectedTankerType = ""
    private var selectedTankerNo = ""
    private var selectedPaymentMode = ""

    private var fromDate = ""
    private var toDate = ""
    private var displayFromDate = ""
    private var displayToDate = ""

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
        binding?.ivUp?.setOnClickListener(this)
        binding?.appBar?.addOnOffsetChangedListener { _, verticalOffset ->
            binding?.ivUp?.visibility = if (verticalOffset < 0) View.VISIBLE else View.GONE
        }
    }

    private fun setScreenTitle() {
        binding?.clScreenTitle?.tvTitle?.text = getString(R.string.maintenance)
        binding?.clScreenTitle?.tvDescription?.text = getString(R.string.here_you_can_check_current_month_maintenance_expense_records)
    }

    private fun setSummary() {
        binding?.clSummary?.labelTotal?.text = getString(R.string.total_maintenance)
        binding?.clSummary?.tvCurrentMonth?.text = CommonUtils.currentMonth()
        binding?.clSummary?.tvDate?.text = CommonUtils.currentDate()
    }

    override fun addObserver() {}

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
        val maintenanceAdapter = MaintenanceAdapter(requireContext(), this)
        binding?.rvMaintanance?.adapter = maintenanceAdapter
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ivBack -> baseActivity?.onBackPressed()
            R.id.btnAdd -> navigateFragment(v, R.id.nav_add_maintenance)
            R.id.btnFilter -> openFilterDialog()
            R.id.ivUp -> {
                binding?.appBar?.scrollTo(0, 0)
                binding?.rvMaintanance?.smoothScrollToPosition(0)
                binding?.appBar?.setExpanded(true, true)
            }
        }
    }

    private fun openFilterDialog() {
        val filterDialog = MaintenanceFilterDialogFragment.getInstance(
            selectedYear, selectedTankerType, selectedTankerNo, selectedPaymentMode, fromDate, toDate, displayFromDate, displayToDate, object : MaintenanceFilterClickListener {
                override fun onApply(
                    fromDate: String, displayFromDate: String?,
                    toDate: String, displayToDate: String?, selectedYear: String,
                    selectedTankerType: String, selectedTankerNo: String, selectedPaymentMode: String
                ) {
                    this@MaintenanceListingFragment.fromDate = fromDate
                    this@MaintenanceListingFragment.displayFromDate = displayFromDate.toString()
                    this@MaintenanceListingFragment.toDate = toDate
                    this@MaintenanceListingFragment.displayToDate = displayToDate.toString()
                    this@MaintenanceListingFragment.selectedYear = selectedYear
                    this@MaintenanceListingFragment.selectedTankerType = selectedTankerType
                    this@MaintenanceListingFragment.selectedTankerNo = selectedTankerNo
                    this@MaintenanceListingFragment.selectedPaymentMode = selectedPaymentMode
                }

                override fun onClear() {
                    selectedYear = ""
                    selectedTankerType = ""
                    selectedTankerNo = ""
                    selectedPaymentMode = ""
                    fromDate = ""
                    toDate = ""
                    displayFromDate = ""
                    displayToDate = ""
                }
            }
        )
        filterDialog.show(childFragmentManager, "Trip Filter Dialog")
    }

    override fun onClick(view: View?, position: Int) {
        when (view?.id) {
            R.id.cvMaintenanceMain -> navigateFragment(view, R.id.nav_maintenance_detail)
            R.id.ivMaintenanceOptions -> showOptionMenu(view)
        }
    }

    private fun showOptionMenu(view: View) {
        val inflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val vieww = inflater.inflate(R.layout.layout_options, null)
        val popupWindow = PopupWindow(vieww, requireContext().resources.getDimensionPixelSize(R.dimen._250sdp), LinearLayout.LayoutParams.WRAP_CONTENT, true)
        val binding: LayoutOptionsBinding = LayoutOptionsBinding.bind(vieww)
        popupWindow.showAsDropDown(view)

        binding.llCancel.visibility = View.GONE
        binding.tvEdit.text = getString(R.string.edit_maintenance)
        binding.tvDelete.text = getString(R.string.delete_maintenance)

        binding.llEdit.setOnClickListener {
            popupWindow.dismiss()
        }

        binding.llDelete.setOnClickListener {
            popupWindow.dismiss()
        }
    }

}