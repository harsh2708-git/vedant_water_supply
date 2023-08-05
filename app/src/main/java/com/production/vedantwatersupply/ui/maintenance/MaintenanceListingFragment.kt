package com.production.vedantwatersupply.ui.maintenance

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
import com.production.vedantwatersupply.databinding.FragmentMaintenanceListingBinding
import com.production.vedantwatersupply.databinding.LayoutOptionsBinding
import com.production.vedantwatersupply.listener.MaintenanceFilterClickListener
import com.production.vedantwatersupply.listener.RecyclerViewClickListener
import com.production.vedantwatersupply.model.request.FilterRequest
import com.production.vedantwatersupply.model.request.GetAllMaintenanceRequest
import com.production.vedantwatersupply.model.request.MaintenanceIdRequest
import com.production.vedantwatersupply.model.request.MonthFilterRequest
import com.production.vedantwatersupply.model.response.FilterResponse
import com.production.vedantwatersupply.model.response.GetAllMaintenanceResponse
import com.production.vedantwatersupply.model.response.MaintenanceData
import com.production.vedantwatersupply.ui.dialog.MaintenanceFilterDialogFragment
import com.production.vedantwatersupply.utils.AppConstants
import com.production.vedantwatersupply.utils.AppConstants.Bundle.Companion.ARG_IS_FOR_MAINTENANCE_UPDATE
import com.production.vedantwatersupply.utils.AppConstants.Bundle.Companion.ARG_MAINTENANCE_ID
import com.production.vedantwatersupply.utils.CommonUtils
import com.production.vedantwatersupply.utils.filter.FilterListAdapter
import com.production.vedantwatersupply.utils.filter.SpaceItemDecoration
import com.production.vedantwatersupply.utils.filter.FilterItem
import com.production.vedantwatersupply.utils.formatPriceWithoutDecimal
import com.production.vedantwatersupply.webservice.baseresponse.WebServiceSetting
import com.production.vedantwatersupply.utils.filter.IFilterItem
import com.production.vedantwatersupply.utils.formatPriceWithDecimal

class MaintenanceListingFragment : BaseFragment<FragmentMaintenanceListingBinding, MaintenanceViewModel>(), View.OnClickListener, RecyclerViewClickListener {

    private var monthFilterAdapter: FilterListAdapter? = null
    private var monthList = ArrayList<FilterItem>()
    private var monthId = ""

    private var selectedYear = ""
    private var selectedTankerType = ""
    private var selectedTankerNo = ""
    private var selectedPaymentMode = ""
    private var selectedAddedBy = ""

    private var fromDate = ""
    private var toDate = ""
    private var displayFromDate = ""
    private var displayToDate = ""

    private var isNextPage = false
    private var isLoading = false
    private var nextPage = "0"
    private var pageIndex = 1
    private var visibleThreshold = 2

    private var maintenanceAdapter: MaintenanceAdapter? = null
    private var maintenanceList = ArrayList<MaintenanceData>()

    private var filterResponse = FilterResponse()
    private var yearList = ArrayList<FilterItem>()
    private var tankerList = ArrayList<FilterItem>()
    private var addedByList = ArrayList<FilterItem>()

    override val layoutId: Int
        get() = R.layout.fragment_maintenance_listing

    override fun getViewModel(): Class<MaintenanceViewModel> = MaintenanceViewModel::class.java

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callMonthFilterApi(CommonUtils.getCurrentYear().toString())
    }

    override fun init() {
        setScreenTitle()
        setSummary()

        setMaintenanceAdapter()
    }

    override fun initListener() {
        binding?.clHeader?.ivBack?.setOnClickListener(this)
        binding?.btnAdd?.setOnClickListener(this)
        binding?.btnFilter?.setOnClickListener(this)
        binding?.ivUp?.setOnClickListener(this)
        binding?.appBar?.addOnOffsetChangedListener { _, verticalOffset ->
            binding?.ivUp?.visibility = if (verticalOffset < 0) View.VISIBLE else View.GONE
        }

        /*
        * Load more
        */
        binding?.rvMaintanance?.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val scrollOffset = if (dy < 0) -1 * dy else dy
                if (binding?.rvMaintanance?.layoutManager != null) {
                    val lastVisibleItem = (binding?.rvMaintanance?.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    val totalItemCount: Int = binding?.rvMaintanance?.layoutManager?.itemCount!!
                    if (scrollOffset > 0 && isNextPage && !isLoading && totalItemCount <= lastVisibleItem + visibleThreshold) {
                        isLoading = true
                        pageIndex++
                        binding!!.llLoading.visibility = View.VISIBLE
                        callGetAllMaintenanceApi()
                    }
                }
            }
        })

        binding?.swipeLayout?.setOnRefreshListener {
            pageIndex = 1
            callGetAllMaintenanceApi()
        }

    }

    private fun setScreenTitle() {
        binding?.clScreenTitle?.tvTitle?.text = getString(R.string.maintenance)
        binding?.clScreenTitle?.tvDescription?.text = getString(R.string.here_you_can_check_current_month_maintenance_expense_records)
    }

    private fun setSummary() {
        binding?.clSummary?.labelTotal?.text = getString(R.string.total_maintenance)
//        binding?.clSummary?.tvCurrentMonth?.text = CommonUtils.currentMonth()
//        binding?.clSummary?.tvDate?.text = CommonUtils.currentDate()
    }

    private fun callMonthFilterApi(selectedYear: String) {
//        showProgress()
        val monthFilterRequest = MonthFilterRequest()
        monthFilterRequest.year = selectedYear
        viewModel?.callMonthFilterApi(monthFilterRequest)
    }

    private fun callGetAllMaintenanceApi() {
        showProgress()
        val getAllMaintenanceRequest = GetAllMaintenanceRequest()
        getAllMaintenanceRequest.page = pageIndex.toString()
        getAllMaintenanceRequest.limit = "10"
        getAllMaintenanceRequest.fromDate = fromDate
        getAllMaintenanceRequest.toDate = toDate
        getAllMaintenanceRequest.year = selectedYear
        getAllMaintenanceRequest.month = monthId
        getAllMaintenanceRequest.tankerType = selectedTankerType
        getAllMaintenanceRequest.tankerId = selectedTankerNo
        getAllMaintenanceRequest.paymentmode = selectedPaymentMode
        getAllMaintenanceRequest.addedBy = selectedAddedBy
        Log.d("Maintenance Listing Params", "callGetAllMaintenanceApi: " + Gson().toJson(getAllMaintenanceRequest))
        viewModel?.callGetAllMaintenanceApi(getAllMaintenanceRequest)
    }

    private fun callMaintenanceDeleteApi(id: String?) {
        baseActivity?.showProgress()
        val tripId = MaintenanceIdRequest()
        tripId.maintainanceId = id.toString()
        viewModel?.callMaintenanceDeleteApi(tripId)
    }

    private fun callFilterApi() {
        baseActivity?.showProgress()
        val filterRequest = FilterRequest()
        filterRequest.filterFor = AppConstants.Filter.MAINTAINANCE
        viewModel?.callFilterApi(filterRequest)
    }

    override fun addObserver() {
        viewModel?.tripRepository?.monthFilterLiveData?.observe(this) {
            when (it.settings?.success) {
                WebServiceSetting.SUCCESS -> {
                    monthList.clear()
                    monthList.add(0, FilterItem("", getString(R.string.all), true))
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
//            hideProgress()
        }

        viewModel?.maintenanceRepository?.getAllMaintenanceResponseMutableLiveData?.observe(this) {
            when (it.webServiceSetting?.success) {
                WebServiceSetting.SUCCESS -> {
                    isLoading = false
                    isNextPage = it?.webServiceSetting?.nextPage != 0
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

        viewModel?.maintenanceRepository?.maintenanceDeleteResponseMutableLiveData?.observe(this) {
            when (it.webServiceSetting?.success) {
                WebServiceSetting.SUCCESS -> {
                    CommonUtils.showToast(requireContext(), it.webServiceSetting?.message)
                    callGetAllMaintenanceApi()
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

                    tankerList.clear()
                    tankerList.add(0, FilterItem("", getString(R.string.please_select_tanker_no)))
                    filterResponse.vehicle?.let { tanker -> tankerList.addAll(tanker) }

                    addedByList.clear()
                    addedByList.add(0, FilterItem("", getString(R.string.please_select_added_by)))
                    filterResponse.addedBy?.let { addedBy -> addedByList.addAll(addedBy) }

                    openFilterDialog()
                }

                WebServiceSetting.FAILURE -> {
                    CommonUtils.showToast(requireContext(), it.webServiceSetting?.message)
                }

                WebServiceSetting.NO_INTERNET -> {
                    CommonUtils.showToast(requireContext(), getString(R.string.no_internet_title))
                }
            }
        }
    }

    private fun updateUI(it: GetAllMaintenanceResponse?) {
        binding?.clMain?.visibility = View.VISIBLE
        binding?.clSummary?.tvTotal?.text = it?.totalMaintainanceCount?.toString()?.formatPriceWithoutDecimal()
        binding?.clSummary?.labelAmount?.text = getString(R.string.total_maintenance_amount)
        binding?.clSummary?.tvAmount?.text = it?.totalMaintainanceAmount.toString().formatPriceWithoutDecimal()
       /* maintenanceList.clear()
        it?.maintainanceData?.let { it1 -> maintenanceList.addAll(it1) }

        if (maintenanceList.isEmpty()) {
            hideData()
        } else {
            showData()
            setMaintenanceAdapter()
        }*/

        if (it?.maintainanceData?.isEmpty() == true) {
            hideData()
        } else {
            showData()
            binding?.rvMaintanance?.visibility = View.VISIBLE
            if (pageIndex ==1){
                it?.maintainanceData?.let { it1 -> maintenanceAdapter?.addRecords(it1) }
            }else {
                it?.maintainanceData?.let { it1 -> maintenanceAdapter?.updateRecords(it1) }
            }
        }
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

    private fun setMaintenanceAdapter() {
        maintenanceAdapter = MaintenanceAdapter(requireContext(), ArrayList(), this)
        binding?.rvMaintanance?.adapter = maintenanceAdapter
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ivBack -> baseActivity?.onBackPressed()
            R.id.btnAdd -> navigateFragment(v, R.id.nav_add_maintenance)
            R.id.btnFilter -> {
                if (yearList.isEmpty() || tankerList.isEmpty() || addedByList.isEmpty()) callFilterApi()
                else openFilterDialog()
            }

            R.id.ivUp -> {
                binding?.appBar?.scrollTo(0, 0)
                binding?.rvMaintanance?.smoothScrollToPosition(0)
                binding?.appBar?.setExpanded(true, true)
            }
        }
    }

    private fun openFilterDialog() {
        val filterDialog = MaintenanceFilterDialogFragment.getInstance(
            selectedYear, selectedTankerType, selectedTankerNo,
            selectedPaymentMode, fromDate, toDate, displayFromDate, displayToDate,selectedAddedBy,
            yearList,tankerList,addedByList,
            object : MaintenanceFilterClickListener {
                override fun onApply(
                    fromDate: String, displayFromDate: String?,
                    toDate: String, displayToDate: String?, selectedYear: String,
                    selectedTankerType: String, selectedTankerNo: String, selectedPaymentMode: String, selectedAddedBy: String
                ) {
                    this@MaintenanceListingFragment.fromDate = fromDate
                    this@MaintenanceListingFragment.displayFromDate = displayFromDate.toString()
                    this@MaintenanceListingFragment.toDate = toDate
                    this@MaintenanceListingFragment.displayToDate = displayToDate.toString()
                    this@MaintenanceListingFragment.selectedYear = selectedYear
                    this@MaintenanceListingFragment.selectedTankerType = selectedTankerType
                    this@MaintenanceListingFragment.selectedTankerNo = selectedTankerNo
                    this@MaintenanceListingFragment.selectedPaymentMode = selectedPaymentMode
                    this@MaintenanceListingFragment.selectedAddedBy = selectedAddedBy

                    if (fromDate.isNotEmpty() && toDate.isNotEmpty()) {
                        binding?.rvMonthFilter?.visibility = View.GONE
                        monthId = ""
                    }

                    if (selectedYear.isNotEmpty()) {
                        callMonthFilterApi(selectedYear)
                    }else {
                        resetAdapter()
                    }
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
                    selectedAddedBy = ""

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
        val response = maintenanceAdapter?.getItemAt(position)
        when (view?.id) {
            R.id.cvMaintenanceMain -> {
                val bundle = Bundle()
                bundle.putString(ARG_MAINTENANCE_ID, response?.id)
                navigateFragment(view, R.id.nav_maintenance_detail, bundle)
            }

            R.id.ivMaintenanceOptions -> showOptionMenu(view, response)
        }
    }

    private fun showOptionMenu(view: View, response: MaintenanceData?) {
        val inflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val vieww = inflater.inflate(R.layout.layout_options, null)
        val popupWindow = PopupWindow(vieww, requireContext().resources.getDimensionPixelSize(R.dimen._250sdp), LinearLayout.LayoutParams.WRAP_CONTENT, true)
        val binding: LayoutOptionsBinding = LayoutOptionsBinding.bind(vieww)
        popupWindow.showAsDropDown(view)

        binding.llCancel.visibility = View.GONE
        binding.tvEdit.text = getString(R.string.edit_maintenance)
        binding.tvDelete.text = getString(R.string.delete_maintenance)

        binding.llEdit.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(ARG_MAINTENANCE_ID, response?.id)
            bundle.putBoolean(ARG_IS_FOR_MAINTENANCE_UPDATE, true)
            navigateFragment(view, R.id.nav_add_maintenance, bundle)
            popupWindow.dismiss()
        }

        binding.llDelete.setOnClickListener {
            callMaintenanceDeleteApi(response?.id)
            popupWindow.dismiss()
        }
    }

    private fun resetAdapter() {
        binding?.swipeLayout?.isRefreshing = false
        pageIndex = 1
        callGetAllMaintenanceApi()
//        setMaintenanceAdapter()
    }

    private fun showData() {
        binding?.rvMaintanance?.visibility = View.VISIBLE
        binding?.maintenanceNoData?.tvNoData?.visibility = View.GONE
    }

    private fun hideData() {
        binding?.rvMaintanance?.visibility = View.GONE
        binding?.maintenanceNoData?.tvNoData?.visibility = View.VISIBLE
        binding?.maintenanceNoData?.tvNoData?.text = getString(R.string.no_maintenance_data_found)
    }

    override fun onResume() {
        super.onResume()
        yearList = ArrayList()
        tankerList = ArrayList()
        addedByList = ArrayList()
    }
}