package com.production.vedantwatersupply.ui.driver

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.production.vedantwatersupply.R
import com.production.vedantwatersupply.core.BaseFragment
import com.production.vedantwatersupply.databinding.FragmentDriverListingBinding
import com.production.vedantwatersupply.databinding.LayoutOptionsBinding
import com.production.vedantwatersupply.listener.DriverFilterClickListener
import com.production.vedantwatersupply.listener.RecyclerViewClickListener
import com.production.vedantwatersupply.model.request.GetDriverExpensesRequest
import com.production.vedantwatersupply.model.request.MonthFilterRequest
import com.production.vedantwatersupply.model.response.DriverData
import com.production.vedantwatersupply.model.response.GetAllDriverExpensesResponse
import com.production.vedantwatersupply.ui.dialog.DriverFilterDialogFragment
import com.production.vedantwatersupply.utils.CommonUtils
import com.production.vedantwatersupply.utils.filter.FilterListAdapter
import com.production.vedantwatersupply.utils.filter.SpaceItemDecoration
import com.production.vedantwatersupply.utils.filter.FilterItem
import com.production.vedantwatersupply.utils.formatPriceWithoutDecimal
import com.production.vedantwatersupply.webservice.baseresponse.WebServiceSetting
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

    private var isNextPage = false
    private var isLoading = false
    private var pageIndex = 1
    private var visibleThreshold = 2

    private var driverAdapter: DriverAdapter? = null
    private var driverList = ArrayList<DriverData>()
    override val layoutId: Int
        get() = R.layout.fragment_driver_listing

    override fun getViewModel(): Class<DriverViewModel> = DriverViewModel::class.java

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callMonthFilterApi(CommonUtils.getCurrentYear().toString())
    }

    override fun init() {
        setScreenTitle()
        setSummary()
//        initFilterView()
//        setDriverAdapter()
    }

    override fun initListener() {
        binding?.clHeader?.ivBack?.setOnClickListener(this)
        binding?.btnAdd?.setOnClickListener(this)
        binding?.btnFilter?.setOnClickListener(this)
        binding?.ivUp?.setOnClickListener(this)
        binding?.appBar?.addOnOffsetChangedListener { _, verticalOffset ->
            binding?.ivUp?.visibility = if (verticalOffset < 0) View.VISIBLE else View.GONE
        }

        binding?.rvDrivers?.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val scrollOffset = if (dy < 0) -1 * dy else dy
                if (binding?.rvDrivers?.layoutManager != null) {
                    val lastVisibleItem = (binding?.rvDrivers?.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    val totalItemCount: Int = binding?.rvDrivers?.layoutManager?.itemCount!!
                    if (scrollOffset > 0 && isNextPage && !isLoading && totalItemCount <= lastVisibleItem + visibleThreshold) {
                        isLoading = true
                        pageIndex++
                        binding!!.llLoading.visibility = View.VISIBLE

                    }
                }
            }
        })

        binding?.swipeLayout?.setOnRefreshListener {
            pageIndex = 1
            callGetAllDriverExpences()
        }

    }

    private fun callMonthFilterApi(selectedYear: String) {
        showProgress()
        val monthFilterRequest = MonthFilterRequest()
        monthFilterRequest.year = selectedYear
        viewModel?.callMonthFilterApi(monthFilterRequest)
    }

    private fun callGetAllDriverExpences(){
        showProgress()
        val getDriverExpensesRequest = GetDriverExpensesRequest()
        getDriverExpensesRequest.page = pageIndex.toString()
        getDriverExpensesRequest.limit = "10"
        getDriverExpensesRequest.fromDate = fromDate
        getDriverExpensesRequest.toDate = toDate
        getDriverExpensesRequest.year = selectedYear
        getDriverExpensesRequest.month = monthId
        getDriverExpensesRequest.driverType = selectedDriverType
        getDriverExpensesRequest.driverId = selectedDriver
        getDriverExpensesRequest.paymentmode = selectedPaymentMode
        getDriverExpensesRequest.addedBy = ""
        Log.d("Driver Listing Params", "callGetAllDriverExpences: " + Gson().toJson(getDriverExpensesRequest))
        viewModel?.callGetAllDriverExpences(getDriverExpensesRequest)
    }

    override fun addObserver() {
        viewModel?.tripRepository?.monthFilterLiveData?.observe(this) {
            when (it.settings?.success) {
                WebServiceSetting.SUCCESS -> {
                    monthList.clear()
                    monthList.add(0, FilterItem("", getString(R.string.all), true))
                    it?.data?.let { it1 -> monthList.addAll(it1) }
                    initFilterView()

                    if (selectedYear.isEmpty()) {
                        monthId = monthFilterAdapter?.getSelectedItem()?.dbValue.toString()
                        resetAdapter()
                    }
                }

                WebServiceSetting.FAILURE -> {
                    CommonUtils.showToast(requireContext(), it.settings?.message)
                }

                WebServiceSetting.NO_INTERNET -> {
                    CommonUtils.showToast(requireContext(), getString(R.string.no_internet_title))
                }
            }
            hideProgress()
        }

        viewModel?.driverRepository?.getAllDriverExpensesResponseMutableLiveData?.observe(this){
            when (it.webServiceSetting?.success) {
                WebServiceSetting.SUCCESS -> {
                    isNextPage = it.webServiceSetting?.currentPage.equals("1")
                    isLoading = false
                    binding?.llLoading?.visibility = View.GONE
                    binding?.swipeLayout?.isRefreshing = false
                    updateUI(it)
                }

                WebServiceSetting.FAILURE -> {
                    CommonUtils.showToast(requireContext(), it.webServiceSetting?.message)
                }

                WebServiceSetting.NO_INTERNET -> {
                    CommonUtils.showToast(requireContext(), getString(R.string.no_internet_title))
                }
            }
            hideProgress()
        }
    }

    private fun updateUI(it: GetAllDriverExpensesResponse?) {
        binding?.clSummary?.tvTotal?.text = it?.totalDriverExpenceCount?.toString()?.formatPriceWithoutDecimal()
        driverList.clear()
        it?.driverExpenceData?.let { it1 -> driverList.addAll(it1) }

        if (driverList.isEmpty()) {
            hideData()
        } else {
            showData()
            setDriverAdapter()
        }
    }

    private fun setScreenTitle() {
        binding?.clScreenTitle?.tvTitle?.text = getString(R.string.drivers)
        binding?.clScreenTitle?.tvDescription?.text = getString(R.string.here_you_can_check_current_month_drivers_expense_records)
    }

    private fun setSummary() {
        binding?.clSummary?.labelTotal?.text = getString(R.string.total_drivers_expense)
        binding?.clSummary?.tvCurrentMonth?.text = CommonUtils.currentMonth()
        binding?.clSummary?.tvDate?.text = CommonUtils.currentDate()
    }

    private fun resetAdapter() {
        binding?.swipeLayout?.isRefreshing = false
        pageIndex = 1
        callGetAllDriverExpences()
        setDriverAdapter()
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
        driverAdapter = DriverAdapter(requireContext(),driverList, this)
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
            selectedYear, selectedDriverType, selectedDriver, selectedPaymentMode, fromDate, toDate, displayFromDate, displayToDate, object : DriverFilterClickListener {
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

    private fun showData() {
        binding?.rvDrivers?.visibility = View.VISIBLE
        binding?.driverNoData?.tvNoData?.visibility = View.GONE
    }

    private fun hideData() {
        binding?.rvDrivers?.visibility = View.GONE
        binding?.driverNoData?.tvNoData?.visibility = View.VISIBLE
        binding?.driverNoData?.tvNoData?.text = getString(R.string.no_maintenance_data_found)
    }

}