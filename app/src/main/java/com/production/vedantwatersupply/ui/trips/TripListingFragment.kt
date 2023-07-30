package com.production.vedantwatersupply.ui.trips

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
import com.production.vedantwatersupply.databinding.FragmentTripListingBinding
import com.production.vedantwatersupply.databinding.LayoutOptionsBinding
import com.production.vedantwatersupply.listener.RecyclerViewClickListener
import com.production.vedantwatersupply.listener.TripFilterClickListener
import com.production.vedantwatersupply.model.request.FilterRequest
import com.production.vedantwatersupply.model.request.GetAllTripRequest
import com.production.vedantwatersupply.model.request.MonthFilterRequest
import com.production.vedantwatersupply.model.request.TripDetailRequest
import com.production.vedantwatersupply.model.response.FilterResponse
import com.production.vedantwatersupply.model.response.GetAllTripResponse
import com.production.vedantwatersupply.model.response.TripData
import com.production.vedantwatersupply.ui.dialog.FilterDialogFragment
import com.production.vedantwatersupply.utils.AppConstants.Bundle.Companion.ARG_IS_FOR_TRIP_UPDATE
import com.production.vedantwatersupply.utils.AppConstants.Bundle.Companion.ARG_TRIP_ID
import com.production.vedantwatersupply.utils.AppConstants.Filter.Companion.TRIP
import com.production.vedantwatersupply.utils.CommonUtils
import com.production.vedantwatersupply.utils.filter.SpaceItemDecoration
import com.production.vedantwatersupply.utils.filter.FilterItem
import com.production.vedantwatersupply.utils.filter.FilterListAdapter
import com.production.vedantwatersupply.webservice.baseresponse.WebServiceSetting
import com.transportermanger.util.filter.IFilterItem

class TripListingFragment : BaseFragment<FragmentTripListingBinding, TripViewModel>(), View.OnClickListener, RecyclerViewClickListener {

    private var monthFilterAdapter: FilterListAdapter? = null
    private var monthList = ArrayList<FilterItem>()
    private var monthId = ""

    private var fromDate = ""
    private var toDate = ""
    private var displayFromDate = ""
    private var displayToDate = ""

    private var selectedYear = ""
    private var selectedTankerType = ""
    private var selectedTankerNo = ""
    private var selectedPaymentMode = ""
    private var selectedDriverType = ""
    private var selectedDriver = ""
    private var selectedFillingSite = ""

    //    private var selectedWaterType = ""
    private var selectedAddedBy = ""
    private var selectedStatus = ""
    private var selectedFuelFilledBy = ""

    private var isNextPage = false
    private var isLoading = false
    private var pageIndex = 1
    private var visibleThreshold = 2

    private var tripAdapter: TripsAdapter? = null
    private var filterResponse = FilterResponse()

    private var yearList = ArrayList<FilterItem>()
    private var tankerList = ArrayList<FilterItem>()
    private var driverList = ArrayList<FilterItem>()

    //    private var waterTypeList = ArrayList<FilterItem>()
    private var addedByList = ArrayList<FilterItem>()

    private var tripList = ArrayList<TripData>()

    override val layoutId: Int
        get() = R.layout.fragment_trip_listing

    override fun getViewModel(): Class<TripViewModel> = TripViewModel::class.java

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callMonthFilterApi(CommonUtils.getCurrentYear().toString())
    }

    override fun init() {
        setScreenTitle()
        binding?.clSummary?.tvCurrentMonth?.text = CommonUtils.currentMonth()
        binding?.clSummary?.tvDate?.text = CommonUtils.currentDate()
    }

    private fun setScreenTitle() {
        binding?.clScreenTitle?.tvTitle?.text = getString(R.string.trips)
        binding?.clScreenTitle?.tvDescription?.text = getString(R.string.here_you_can_check_current_month_trips_records)
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
        binding?.rvTrips?.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val scrollOffset = if (dy < 0) -1 * dy else dy
                if (binding?.rvTrips?.layoutManager != null) {
                    val lastVisibleItem = (binding?.rvTrips?.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    val totalItemCount: Int = binding?.rvTrips?.layoutManager?.itemCount!!
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
            callGetAllTripApi()
        }
    }

    private fun callMonthFilterApi(selectedYear: String) {
        showProgress()
        val monthFilterRequest = MonthFilterRequest()
        monthFilterRequest.year = selectedYear
        viewModel?.callMonthFilterApi(monthFilterRequest)
    }

    private fun callGetAllTripApi() {
        showProgress()
        val getAllTripRequest = GetAllTripRequest()
        getAllTripRequest.page = pageIndex.toString()
        getAllTripRequest.limit = "10"
        getAllTripRequest.month = monthId
        getAllTripRequest.fromDate = fromDate
        getAllTripRequest.toDate = toDate
        getAllTripRequest.year = selectedYear
        getAllTripRequest.tankerType = selectedTankerType
        getAllTripRequest.tankerId = selectedTankerNo
        getAllTripRequest.driverType = selectedDriverType
        getAllTripRequest.driverId = selectedDriver
        getAllTripRequest.paymentmode = selectedPaymentMode
        getAllTripRequest.fillingSite = selectedFillingSite
        getAllTripRequest.fuelFilledBy = selectedFuelFilledBy
//        getAllTripRequest.waterType = selectedWaterType
        getAllTripRequest.addedBy = selectedAddedBy
        getAllTripRequest.status = selectedStatus
        Log.d("Trip Listing Params", "callGetAllTripApi: " + Gson().toJson(getAllTripRequest))
        viewModel?.callGetAllTripListingApi(getAllTripRequest)
    }

    private fun callTripDeleteApi(id: String?) {
        baseActivity?.showProgress()
        val tripId = TripDetailRequest()
        tripId.id = id.toString()
        viewModel?.callTripDeleteApi(tripId)
    }

    private fun callTripCancelApi(id: String?) {
        baseActivity?.showProgress()
        val tripId = TripDetailRequest()
        tripId.id = id.toString()
        viewModel?.callTripCancelApi(tripId)
    }

    private fun callFilterApi() {
        baseActivity?.showProgress()
        val filterRequest = FilterRequest()
        filterRequest.filterFor = TRIP
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

        viewModel?.tripRepository?.getAllTripResponseMutableLiveData?.observe(this) {
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

        viewModel?.tripRepository?.tripDeleteResponseMutableLiveData?.observe(this) {
            when (it.webServiceSetting?.success) {
                WebServiceSetting.SUCCESS -> {
                    CommonUtils.showToast(requireContext(), it.webServiceSetting?.message)
                    callGetAllTripApi()
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

        viewModel?.tripRepository?.tripCancelResponseMutableLiveData?.observe(this) {
            when (it.webServiceSetting?.success) {
                WebServiceSetting.SUCCESS -> {
                    CommonUtils.showToast(requireContext(), it.webServiceSetting?.message)
                    callGetAllTripApi()
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

                    driverList.clear()
                    driverList.add(0, FilterItem("", getString(R.string.please_select_driver)))
                    filterResponse.driver?.let { driver -> driverList.addAll(driver) }

//                    waterTypeList.clear()
//                    waterTypeList.add(0, FilterItem("", getString(R.string.please_select_water_type)))
//                    filterResponse.waterType?.let { waterType -> waterTypeList.addAll(waterType) }

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

    private fun updateUI(it: GetAllTripResponse?) {
        binding?.clSummary?.tvTotal?.text = it?.totalTripsCount.toString()

        tripList.clear()
        it?.tripData?.let { it1 -> tripList.addAll(it1) }

        if (tripList.isEmpty()) {
            hideData()
        } else {
            showData()
            setTripsAdapter()
        }

    }

    private fun initFilterView() {
        monthFilterAdapter = FilterListAdapter(monthList, object : IFilterItem {
            override fun onFilterItemSelected(view: View?, pos: Int) {
                monthId = monthList[pos].dbValue
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

    private fun setTripsAdapter() {
        tripAdapter = TripsAdapter(requireContext(), tripList, this)
        binding?.rvTrips?.adapter = tripAdapter
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ivBack -> baseActivity?.onBackPressed()
            R.id.btnAdd -> navigateFragment(v, R.id.nav_add_trip)
            R.id.btnFilter -> {
                if (tankerList.isEmpty() || driverList.isEmpty() /*|| waterTypeList.isEmpty()*/ || addedByList.isEmpty()) callFilterApi()
                else openFilterDialog()
//                openFilterDialog()
            }

            R.id.ivUp -> {
                binding?.appBar?.scrollTo(0, 0)
                binding?.rvTrips?.smoothScrollToPosition(0)
                binding?.appBar?.setExpanded(true, true)
            }
        }
    }

    private fun openFilterDialog() {
        val filterDialog = FilterDialogFragment.getInstance(
            selectedYear, selectedTankerType, selectedTankerNo, selectedPaymentMode,
            selectedDriverType, selectedDriver, selectedFillingSite, /*selectedWaterType,*/
            selectedAddedBy, selectedStatus, selectedFuelFilledBy,
            fromDate, toDate, displayFromDate, displayToDate, tankerList, driverList, /*waterTypeList,*/ addedByList, yearList,
            object : TripFilterClickListener {
                override fun onApply(
                    fromDate: String, displayFromDate: String?, toDate: String, displayToDate: String?,
                    selectedYear: String, selectedTankerType: String, selectedTankerNo: String,
                    selectedPaymentMode: String, selectedFuelFilledBy: String, selectedDriverType: String,
                    selectedDriver: String,/* selectedWaterType: String,*/ selectedAddedBy: String,
                    selectedStatus: String, selectedFillingSite: String
                ) {
                    this@TripListingFragment.fromDate = fromDate
                    this@TripListingFragment.displayFromDate = displayFromDate.toString()
                    this@TripListingFragment.toDate = toDate
                    this@TripListingFragment.displayToDate = displayToDate.toString()
                    this@TripListingFragment.selectedYear = selectedYear
                    this@TripListingFragment.selectedTankerType = selectedTankerType
                    this@TripListingFragment.selectedTankerNo = selectedTankerNo
                    this@TripListingFragment.selectedPaymentMode = selectedPaymentMode
                    this@TripListingFragment.selectedFuelFilledBy = selectedFuelFilledBy
                    this@TripListingFragment.selectedDriverType = selectedDriverType
                    this@TripListingFragment.selectedDriver = selectedDriver
//                    this@TripListingFragment.selectedWaterType = selectedWaterType
                    this@TripListingFragment.selectedAddedBy = selectedAddedBy
                    this@TripListingFragment.selectedStatus = selectedStatus
                    this@TripListingFragment.selectedFillingSite = selectedFillingSite

                    if (fromDate.isNotEmpty() && toDate.isNotEmpty()) {
                        binding?.rvMonthFilter?.visibility = View.GONE
                        monthId = ""
                    }

                    if (selectedYear.isNotEmpty()) {
                        callMonthFilterApi(selectedYear)
                        monthFilterAdapter?.setSelected(0)
                    } else {
                        resetAdapter()
                    }
                }

                override fun onClear() {
                    fromDate = ""
                    toDate = ""
                    displayFromDate = ""
                    displayToDate = ""

                    selectedYear = ""
                    selectedTankerType = ""
                    selectedTankerNo = ""
                    selectedPaymentMode = ""
                    selectedDriverType = ""
                    selectedDriver = ""
                    selectedFillingSite = ""
//                    selectedWaterType = ""
                    selectedAddedBy = ""
                    selectedStatus = ""
                    selectedFuelFilledBy = ""

                    binding?.rvMonthFilter?.visibility = View.VISIBLE

                    callMonthFilterApi(CommonUtils.getCurrentYear().toString())
                    monthId = ""
                    monthFilterAdapter?.setSelected(0)

                    resetAdapter()
                }
            }
        )
        filterDialog.show(childFragmentManager, "Trip Filter Dialog")
    }

    override fun onClick(view: View?, position: Int) {
        val response = tripAdapter?.getItemAt(position)
        when (view?.id) {
            R.id.cvTripMain -> {
                val bundle = Bundle()
                bundle.putString(ARG_TRIP_ID, response?.id)
                navigateFragment(view, R.id.nav_trip_detail, bundle)
            }

            R.id.ivTripOptions -> {
                showOptionMenu(view, response)
            }
        }
    }

    private fun showOptionMenu(view: View, response: TripData?) {
        val inflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val vieww = inflater.inflate(R.layout.layout_options, null)
        val popupWindow = PopupWindow(vieww, requireContext().resources.getDimensionPixelSize(R.dimen._250sdp), LinearLayout.LayoutParams.WRAP_CONTENT, true)
        val binding: LayoutOptionsBinding = LayoutOptionsBinding.bind(vieww)
        popupWindow.showAsDropDown(view)

        binding.llCancel.visibility = View.VISIBLE
        binding.tvEdit.text = getString(R.string.edit_trip)
        binding.tvDelete.text = getString(R.string.delete_trip)

        binding.llEdit.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(ARG_TRIP_ID, response?.id)
            bundle.putBoolean(ARG_IS_FOR_TRIP_UPDATE, true)
            navigateFragment(view, R.id.nav_add_trip, bundle)
            popupWindow.dismiss()
        }

        binding.llDelete.setOnClickListener {
            callTripDeleteApi(response?.id)
            popupWindow.dismiss()
        }

        binding.llCancel.setOnClickListener {
            callTripCancelApi(response?.id)
            popupWindow.dismiss()
        }
    }

    private fun resetAdapter() {
        binding?.swipeLayout?.isRefreshing = false
        pageIndex = 1
        callGetAllTripApi()
        setTripsAdapter()
    }

    private fun showData() {
        binding?.rvTrips?.visibility = View.VISIBLE
        binding?.tripNoData?.tvNoData?.visibility = View.GONE
    }

    private fun hideData() {
        binding?.rvTrips?.visibility = View.GONE
        binding?.tripNoData?.tvNoData?.visibility = View.VISIBLE
        binding?.tripNoData?.tvNoData?.text = getString(R.string.no_trips_data_found)
    }

}