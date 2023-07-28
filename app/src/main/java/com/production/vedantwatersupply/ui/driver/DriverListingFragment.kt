package com.production.vedantwatersupply.ui.driver

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import com.production.vedantwatersupply.R
import com.production.vedantwatersupply.core.BaseFragment
import com.production.vedantwatersupply.databinding.FragmentDriverListingBinding
import com.production.vedantwatersupply.databinding.LayoutOptionsBinding
import com.production.vedantwatersupply.listener.DriverFilterClickListener
import com.production.vedantwatersupply.listener.RecyclerViewClickListener
import com.production.vedantwatersupply.ui.dialog.DriverFilterDialogFragment
import com.production.vedantwatersupply.utils.CommonUtils
import com.production.vedantwatersupply.utils.filter.FilterListAdapter
import com.production.vedantwatersupply.utils.filter.SpaceItemDecoration
import com.production.vedantwatersupply.utils.filter.FilterItem
import com.transportermanger.util.filter.IFilterItem

class DriverListingFragment : BaseFragment<FragmentDriverListingBinding, DriverViewModel>(), View.OnClickListener, RecyclerViewClickListener {

    private var monthFilterAdapter: FilterListAdapter? = null
    private var monthList = ArrayList<FilterItem>()
    private var monthId = ""

    private var yearId = ""
    private var driverTypeId = ""
    private var driverId = ""
    private var paymentModeId = ""

    private var selectedYear = ""
    private var selectedDriverType = ""
    private var selectedDriver = ""
    private var selectedPaymentMode = ""

    private var fromDate = ""
    private var toDate = ""
    private var displayFromDate = ""
    private var displayToDate = ""

    override val layoutId: Int
        get() = R.layout.fragment_driver_listing

    override fun getViewModel(): Class<DriverViewModel> = DriverViewModel::class.java

    override fun init() {
        setScreenTitle()
        setSummary()
        initFilterView()
        setDriverAdapter()
    }

    override fun initListener() {
        binding?.clHeader?.ivBack?.setOnClickListener(this)
        binding?.btnAdd?.setOnClickListener(this)
        binding?.btnFilter?.setOnClickListener(this)
        binding?.ivUp?.setOnClickListener(this)
        binding?.appBar?.addOnOffsetChangedListener { _, verticalOffset ->
            binding?.ivUp?.visibility = if (verticalOffset < 0) View.VISIBLE else  View.GONE
        }
    }

    override fun addObserver() {}

    private fun setScreenTitle() {
        binding?.clScreenTitle?.tvTitle?.text = getString(R.string.drivers)
        binding?.clScreenTitle?.tvDescription?.text = getString(R.string.here_you_can_check_current_month_drivers_expense_records)
    }

    private fun setSummary() {
        binding?.clSummary?.labelTotal?.text = getString(R.string.total_drivers_expense)
        binding?.clSummary?.tvCurrentMonth?.text = CommonUtils.currentMonth()
        binding?.clSummary?.tvDate?.text = CommonUtils.currentDate()
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

    private fun setDriverAdapter() {
        val driverAdapter = DriverAdapter(requireContext(), this)
        binding?.rvDrivers?.adapter = driverAdapter
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ivBack -> baseActivity?.onBackPressed()
            R.id.btnAdd -> navigateFragment(v, R.id.nav_add_driver)
            R.id.btnFilter -> openFilterDialog()
            R.id.ivUp -> {
                binding?.appBar?.scrollTo(0, 0)
                binding?.rvDrivers?.smoothScrollToPosition(0)
                binding?.appBar?.setExpanded(true, true)
            }
        }
    }

    private fun openFilterDialog() {
        val filterDialog = DriverFilterDialogFragment.getInstance(
            selectedYear,selectedDriverType,selectedDriver,selectedPaymentMode,fromDate,toDate,displayFromDate,displayToDate,object : DriverFilterClickListener{
                override fun onApply(fromDate: String, displayFromDate: String?, toDate: String, displayToDate: String?, selectedYear: String, selectedDriverType: String, selectedDriver: String, selectedPaymentMode: String) {
                    this@DriverListingFragment.fromDate = fromDate
                    this@DriverListingFragment.displayFromDate = displayFromDate.toString()
                    this@DriverListingFragment.toDate = toDate
                    this@DriverListingFragment.displayToDate = displayToDate.toString()
                    this@DriverListingFragment.selectedYear = selectedYear
                    this@DriverListingFragment.selectedDriver = selectedDriver
                    this@DriverListingFragment.selectedDriverType = selectedDriverType
                    this@DriverListingFragment.selectedPaymentMode = selectedPaymentMode
                }

                override fun onClear() {
                  yearId = ""
                  driverTypeId = ""
                  driverId = ""
                  paymentModeId = ""

                  selectedYear = ""
                  selectedDriverType = ""
                  selectedDriver = ""
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
            R.id.cvDriverMain -> navigateFragment(view, R.id.nav_driver_detail)
            R.id.ivDriverOptions -> showOptionMenu(view)
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