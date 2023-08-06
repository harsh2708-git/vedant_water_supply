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
import com.production.vedantwatersupply.model.request.DriverIdRequest
import com.production.vedantwatersupply.model.request.FilterRequest
import com.production.vedantwatersupply.model.request.GetDriverExpensesRequest
import com.production.vedantwatersupply.model.request.MonthFilterRequest
import com.production.vedantwatersupply.model.response.DriverData
import com.production.vedantwatersupply.model.response.FilterResponse
import com.production.vedantwatersupply.model.response.GetAllDriverExpensesResponse
import com.production.vedantwatersupply.ui.dialog.DriverFilterDialogFragment
import com.production.vedantwatersupply.utils.AppConstants
import com.production.vedantwatersupply.utils.CommonUtils
import com.production.vedantwatersupply.utils.filter.FilterListAdapter
import com.production.vedantwatersupply.utils.filter.SpaceItemDecoration
import com.production.vedantwatersupply.utils.filter.FilterItem
import com.production.vedantwatersupply.utils.formatPriceWithoutDecimal
import com.production.vedantwatersupply.webservice.baseresponse.WebServiceSetting
import com.production.vedantwatersupply.utils.filter.IFilterItem

class DriverListingFragment : BaseFragment<FragmentDriverListingBinding, DriverViewModel>(), View.OnClickListener, RecyclerViewClickListener {

    private var monthFilterAdapter: FilterListAdapter? = null
    private var monthList = ArrayList<FilterItem>()
    private var monthId = ""

    private var selectedYear = ""
    private var selectedDriverType = ""
    private var selectedDriver = ""
    private var selectedPaymentMode = ""
    private var selectedAddedBy = ""

    private var fromDate = ""
    private var toDate = ""
    private var displayFromDate = ""
    private var displayToDate = ""

    private var isNextPage = false
    private var isLoading = false
    private var nextPage = "0"
    private var currentPage = 1
    private var visibleThreshold = 2

    private var driverAdapter: DriverAdapter? = null

    private var filterResponse = FilterResponse()
    private var yearList = ArrayList<FilterItem>()
    private var driveFilterList = ArrayList<FilterItem>()
    private var addedByList = ArrayList<FilterItem>()

    private var selectedDriverPaymentDoneBy = ""

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
        setDriverAdapter()
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
                        currentPage++
                        binding!!.llLoading.visibility = View.VISIBLE
                        callGetAllDriverExpences()
                    }
                }
            }
        })

        binding?.swipeLayout?.setOnRefreshListener {
            currentPage = 1
            callGetAllDriverExpences()
        }

    }

    private fun callMonthFilterApi(selectedYear: String) {
//        showProgress()
        val monthFilterRequest = MonthFilterRequest()
        monthFilterRequest.year = selectedYear
        viewModel?.callMonthFilterApi(monthFilterRequest)
    }

    private fun callGetAllDriverExpences() {
        showProgress()
        val getDriverExpensesRequest = GetDriverExpensesRequest()
        getDriverExpensesRequest.page = currentPage.toString()
        getDriverExpensesRequest.limit = "10"
        getDriverExpensesRequest.fromDate = fromDate
        getDriverExpensesRequest.toDate = toDate
        getDriverExpensesRequest.year = selectedYear
        getDriverExpensesRequest.month = monthId
        getDriverExpensesRequest.driverType = selectedDriverType
        getDriverExpensesRequest.driverId = selectedDriver
        getDriverExpensesRequest.paymentmode = selectedPaymentMode
        getDriverExpensesRequest.addedBy = selectedAddedBy
        Log.d("Driver Listing Params", "callGetAllDriverExpences: " + Gson().toJson(getDriverExpensesRequest))
        viewModel?.callGetAllDriverExpences(getDriverExpensesRequest)
    }

    override fun addObserver() {
        viewModel?.tripRepository?.monthFilterLiveData?.observe(this) {
            when (it.settings?.success) {
                WebServiceSetting.SUCCESS -> {
                    monthList.clear()
                    monthList.add(0, FilterItem("", getString(R.string.all), monthId == ""))
                    it?.data?.let { it1 -> monthList.addAll(it1) }

                    initFilterView()

                    if (selectedYear.isNotEmpty()) {
//                        monthId = monthFilterAdapter?.getSelectedItem()?.dbValue.toString()
                        if (monthId.isNotEmpty()) {
                            val find = monthList.find { it.dbValue == monthId }
                            val pos = monthList.indexOf(find)
                            monthFilterAdapter?.setSelected(pos)
                        }
                        resetAdapter()
                    } else {
                        monthId = ""
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

        }

        viewModel?.driverRepository?.getAllDriverExpensesResponseMutableLiveData?.observe(this) {
            when (it.webServiceSetting?.success) {
                WebServiceSetting.SUCCESS -> {
                    isLoading = false
                    isNextPage = it?.webServiceSetting?.nextPage != 0
                    binding?.llLoading?.visibility = View.GONE
                    binding?.swipeLayout?.isRefreshing = false
                    updateUI(it)
                    hideProgress()
                }

                WebServiceSetting.FAILURE -> {
                    hideProgress()
                    CommonUtils.showToast(requireContext(), it.webServiceSetting?.message)
                }

                WebServiceSetting.NO_INTERNET -> {
                    CommonUtils.showToast(requireContext(), getString(R.string.no_internet_title))
                }
            }
        }

        viewModel?.driverRepository?.deleteDriverExpenseResponseMutableLiveData?.observe(this) {
            when (it.webServiceSetting?.success) {
                WebServiceSetting.SUCCESS -> {
                    CommonUtils.showToast(requireContext(), it.webServiceSetting?.message)
                    callGetAllDriverExpences()
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

        viewModel?.tripRepository?.filterResponseMutableLiveData?.observe(this) {
            baseActivity?.hideProgress()
            when (it?.webServiceSetting?.success) {
                WebServiceSetting.SUCCESS -> {
                    filterResponse = it

                    yearList.clear()
                    yearList.add(0, FilterItem("", getString(R.string.please_select_year)))
                    filterResponse.years?.let { tanker -> yearList.addAll(tanker) }

                    driveFilterList.clear()
                    driveFilterList.add(0, FilterItem("", getString(R.string.please_select_driver)))
                    filterResponse.driver?.let { tanker -> driveFilterList.addAll(tanker) }

                    addedByList.clear()
                    addedByList.add(0, FilterItem("", getString(R.string.please_select_added_by)))
                    filterResponse.addedBy?.let { addedBy -> addedByList.addAll(addedBy) }

                    openFilterDialog()
                }

                WebServiceSetting.FAILURE -> {
                    hideProgress()
                    CommonUtils.showToast(requireContext(), it.webServiceSetting?.message)
                }

                WebServiceSetting.NO_INTERNET -> {
                    CommonUtils.showToast(requireContext(), getString(R.string.no_internet_title))
                }
            }
        }

    }

    private fun updateUI(it: GetAllDriverExpensesResponse?) {
        binding?.clMain?.visibility = View.VISIBLE
        binding?.clSummary?.tvTotal?.text = it?.totalDriverExpenceCount?.toString()?.formatPriceWithoutDecimal()
        binding?.clSummary?.labelAmount?.text = getString(R.string.total_driver_payment)
        binding?.clSummary?.tvAmount?.text = it?.totalDriverExpenceAmount.toString().formatPriceWithoutDecimal()
        binding?.clSummary?.tvExtraPayment?.text = it?.totalDriverExpenceExtraPaymentAmount.toString().formatPriceWithoutDecimal()
        if (it?.driverExpenceData?.isEmpty() == true) {
            hideData()
        } else {
            showData()
            binding?.rvDrivers?.visibility = View.VISIBLE
            if (currentPage == 1) {
                it?.driverExpenceData?.let { it1 -> driverAdapter?.addRecords(it1) }
            } else {
                it?.driverExpenceData?.let { it1 -> driverAdapter?.updateRecords(it1) }
            }
        }
    }

    private fun setScreenTitle() {
        binding?.clScreenTitle?.tvTitle?.text = getString(R.string.drivers)
        binding?.clScreenTitle?.tvDescription?.text = getString(R.string.here_you_can_check_current_month_drivers_expense_records)
    }

    private fun setSummary() {
        binding?.clSummary?.labelTotal?.text = getString(R.string.total_drivers_expense)
//        binding?.clSummary?.tvCurrentMonth?.text = CommonUtils.currentMonth()
//        binding?.clSummary?.tvDate?.text = CommonUtils.currentDate()
    }

    private fun resetAdapter() {
        binding?.swipeLayout?.isRefreshing = false
        binding?.rvDrivers?.visibility = View.VISIBLE
        currentPage = 1
        nextPage = "0"
        showProgress()
        callGetAllDriverExpences()
    }

    private fun initFilterView() {
        monthFilterAdapter = FilterListAdapter(monthList, object : IFilterItem {
            override fun onFilterItemSelected(view: View?, pos: Int) {
                monthId = monthList[pos].dbValue

                if (monthId.isNotEmpty()) {
                    val find = monthList.find { it.dbValue == monthId }
                    val position = monthList.indexOf(find)
                    monthFilterAdapter?.setSelected(position)
                }else if (monthId.isEmpty()){
                    val find = monthList.find { it.dbValue == monthId }
                    val position = monthList.indexOf(find)
                    monthFilterAdapter?.setSelected(position)
                }
//                monthFilterAdapter?.setUnSelected()

                resetAdapter()

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
        driverAdapter = DriverAdapter(requireContext(), arrayListOf(), this)
        binding?.rvDrivers?.adapter = driverAdapter
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ivBack -> baseActivity?.onBackPressed()
            R.id.btnAdd -> navigateFragment(v, R.id.nav_add_driver)
            R.id.btnFilter -> {
                if (yearList.isEmpty() || driveFilterList.isEmpty() || addedByList.isEmpty()) callFilterApi()
                else openFilterDialog()
            }

            R.id.ivUp -> {
                binding?.appBar?.scrollTo(0, 0)
                binding?.rvDrivers?.smoothScrollToPosition(0)
                binding?.appBar?.setExpanded(true, true)
            }
        }
    }

    private fun openFilterDialog() {
        val filterDialog = DriverFilterDialogFragment.getInstance(
            selectedYear, selectedDriverType, selectedDriver, selectedPaymentMode, selectedAddedBy,
            fromDate, toDate, displayFromDate, displayToDate,selectedDriverPaymentDoneBy,
            yearList, driveFilterList, addedByList, object : DriverFilterClickListener {
                override fun onApply(fromDate: String, displayFromDate: String?, toDate: String, displayToDate: String?, selectedYear: String, selectedDriverType: String, selectedDriver: String, selectedPaymentMode: String, selectedAddedBy: String, selectedDriverPaymentDoneBy: String) {
                    this@DriverListingFragment.fromDate = fromDate
                    this@DriverListingFragment.displayFromDate = displayFromDate.toString()
                    this@DriverListingFragment.toDate = toDate
                    this@DriverListingFragment.displayToDate = displayToDate.toString()
                    this@DriverListingFragment.selectedYear = selectedYear
                    this@DriverListingFragment.selectedDriver = selectedDriver
                    this@DriverListingFragment.selectedDriverType = selectedDriverType
                    this@DriverListingFragment.selectedPaymentMode = selectedPaymentMode
                    this@DriverListingFragment.selectedAddedBy = selectedAddedBy
                    this@DriverListingFragment.selectedDriverPaymentDoneBy = selectedDriverPaymentDoneBy

                    if (fromDate.isNotEmpty() && toDate.isNotEmpty()) {
                        binding?.rvMonthFilter?.visibility = View.GONE
                        monthId = ""
                    }

                    if (selectedYear.isNotEmpty()) {
                        callMonthFilterApi(selectedYear)
//                        monthFilterAdapter?.setUnSelected()
                    } else {
                        resetAdapter()
                    }
                }

                override fun onClear() {
                    selectedYear = ""
                    selectedDriverType = ""
                    selectedDriver = ""
                    selectedPaymentMode = ""
                    selectedAddedBy = ""

                    fromDate = ""
                    toDate = ""
                    displayFromDate = ""
                    displayToDate = ""

                    binding?.rvMonthFilter?.visibility = View.VISIBLE

                    callMonthFilterApi(CommonUtils.getCurrentYear().toString())
                    monthId = ""
                    monthFilterAdapter?.setSelected(0)
                }
            }
        )
        filterDialog.show(childFragmentManager, "Trip Filter Dialog")
    }

    override fun onClick(view: View?, position: Int) {
        val response = driverAdapter?.getItemAt(position)
        when (view?.id) {

            R.id.cvDriverMain -> {
                val bundle = Bundle()
                bundle.putString(AppConstants.Bundle.ARG_DRIVER_ID, response?.id)
                navigateFragment(view, R.id.nav_driver_detail, bundle)
            }

            R.id.ivDriverOptions -> showOptionMenu(view, response)
        }
    }

    private fun showOptionMenu(view: View, response: DriverData?) {
        val inflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val vieww = inflater.inflate(R.layout.layout_options, null)
        val popupWindow = PopupWindow(vieww, requireContext().resources.getDimensionPixelSize(R.dimen._250sdp), LinearLayout.LayoutParams.WRAP_CONTENT, true)
        val binding: LayoutOptionsBinding = LayoutOptionsBinding.bind(vieww)
        popupWindow.showAsDropDown(view)

        binding.llCancel.visibility = View.GONE
        binding.tvEdit.text = getString(R.string.edit_driver_expense)
        binding.tvDelete.text = getString(R.string.delete_driver_expense)

        binding.llEdit.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(AppConstants.Bundle.ARG_DRIVER_ID, response?.id)
            bundle.putBoolean(AppConstants.Bundle.ARG_IS_FOR_DRIVER_UPDATE, true)
            navigateFragment(view, R.id.nav_add_driver, bundle)
            popupWindow.dismiss()
        }

        binding.llDelete.setOnClickListener {
            callDriverDeleteApi(response?.id)
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
        binding?.driverNoData?.tvNoData?.text = getString(R.string.no_driver_data_found)
    }

    private fun callDriverDeleteApi(id: String?) {
        baseActivity?.showProgress()
        val driverId = DriverIdRequest()
        driverId.driverExpenceId = id.toString()
        viewModel?.callDriverDeleteApi(driverId)
    }

    private fun callFilterApi() {
        baseActivity?.showProgress()
        val filterRequest = FilterRequest()
        filterRequest.filterFor = AppConstants.Filter.DRIVERMAINTAINANCE
        viewModel?.callFilterApi(filterRequest)
    }

    override fun onResume() {
        super.onResume()
        yearList = ArrayList()
        driveFilterList = ArrayList()
        addedByList = ArrayList()
    }

}