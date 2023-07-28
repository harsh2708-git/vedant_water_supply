package com.production.vedantwatersupply.ui.trips

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.production.vedantwatersupply.R
import com.production.vedantwatersupply.core.BaseFragment
import com.production.vedantwatersupply.databinding.FragmentTripListingBinding
import com.production.vedantwatersupply.databinding.LayoutOptionsBinding
import com.production.vedantwatersupply.listener.RecyclerViewClickListener
import com.production.vedantwatersupply.listener.TripFilterClickListener
import com.production.vedantwatersupply.model.request.GetAllTripRequest
import com.production.vedantwatersupply.model.request.TripDetailRequest
import com.production.vedantwatersupply.model.response.FilterResponse
import com.production.vedantwatersupply.model.response.GetAllTripResponse
import com.production.vedantwatersupply.model.response.TripData
import com.production.vedantwatersupply.ui.dialog.FilterDialogFragment
import com.production.vedantwatersupply.utils.AppConstants.Bundle.Companion.ARG_TRIP_ID
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
    private var selectedWaterType = ""
    private var selectedAddedBy = ""
    private var selectedStatus = ""
    private var selectedFuelFilledBy = ""

    private var isNextPage = false
    private var isLoading = false
    private var pageIndex = 1
    private var visibleThreshold = 2

    private var tripAdapter: TripsAdapter? = null
    private var filterResponse = FilterResponse()

    override val layoutId: Int
        get() = R.layout.fragment_trip_listing

    override fun getViewModel(): Class<TripViewModel> = TripViewModel::class.java
    override fun init() {
        setScreenTitle()

        callMonthFilterApi()
        //setTripsAdapter()

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
    }

    private fun callMonthFilterApi() {
        viewModel?.callMonthFilterApi()
    }

    private fun callGetAllTripApi() {
        val getAllTripRequest = GetAllTripRequest()
        getAllTripRequest.page = pageIndex.toString()
        getAllTripRequest.limit = "10"
        getAllTripRequest.fromDate = ""
        getAllTripRequest.toDate = ""
        getAllTripRequest.year = ""
        getAllTripRequest.tankerType = ""
        getAllTripRequest.tankerId = ""
        getAllTripRequest.driverType = ""
        getAllTripRequest.driverId = ""
        getAllTripRequest.paymentmode = ""
        getAllTripRequest.fillingSite = ""
        getAllTripRequest.fuelFilledBy = ""
        getAllTripRequest.waterType = ""
        getAllTripRequest.addedBy = ""
        getAllTripRequest.status = ""
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
        viewModel?.callFilterApi()
    }

    override fun addObserver() {
        viewModel?.tripRepository?.monthFilterLiveData?.observe(this) {
            when (it.settings?.success) {
                WebServiceSetting.SUCCESS -> {
                    monthList.clear()
                    monthList.add(0, FilterItem("", getString(R.string.all), true))
                    it?.data?.let { it1 -> monthList.addAll(it1) }
                    initFilterView()
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
//                    isNextPage = it.webServiceSetting.nextPage.equals("1")
//                    isLoading = false
//                    binding?.llLoading?.visibility = View.GONE
                    if (it != null) {
                        showData(binding?.rvTrips, binding?.tripNoData?.tvNoData)
                        updateUI(it)
                    } else {
                        hideData(binding?.rvTrips, binding?.tripNoData?.tvNoData)
                    }
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
//                    callGetAllTripApi()
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
//                    callGetAllTripApi()
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
            when (it?.webServiceSetting?.success) {
                WebServiceSetting.SUCCESS -> {
                    filterResponse = it
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

    }

    private fun initFilterView() {
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
        tripAdapter = TripsAdapter(requireContext(), ArrayList(), this)
        binding?.rvTrips?.adapter = tripAdapter
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ivBack -> baseActivity?.onBackPressed()
            R.id.btnAdd -> navigateFragment(v, R.id.nav_add_trip)
            R.id.btnFilter -> openFilterDialog()
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
            selectedDriverType, selectedDriver, selectedFillingSite, selectedWaterType,
            selectedAddedBy, selectedStatus, selectedFuelFilledBy,
            fromDate, toDate, displayFromDate.toString(), displayToDate.toString(),
            object : TripFilterClickListener {
                override fun onApply(
                    fromDate: String, displayFromDate: String?, toDate: String, displayToDate: String?,
                    selectedYear: String, selectedTankerType: String, selectedTankerNo: String,
                    selectedPaymentMode: String, selectedFuelFilledBy: String, selectedDriverType: String,
                    selectedDriver: String, selectedWaterType: String, selectedAddedBy: String,
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
                    this@TripListingFragment.selectedWaterType = selectedWaterType
                    this@TripListingFragment.selectedAddedBy = selectedAddedBy
                    this@TripListingFragment.selectedStatus = selectedStatus
                    this@TripListingFragment.selectedFillingSite = selectedFillingSite

                    resetAdapter()
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
                    selectedWaterType = ""
                    selectedAddedBy = ""
                    selectedStatus = ""
                    selectedFuelFilledBy = ""

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
                bundle.putString(ARG_TRIP_ID, "64c27d61b6d1c28ac6248a26")
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
        setTripsAdapter()
    }

}