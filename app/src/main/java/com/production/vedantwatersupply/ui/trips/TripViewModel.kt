package com.production.vedantwatersupply.ui.trips

import android.app.Application
import com.production.vedantwatersupply.core.BaseViewModel
import com.production.vedantwatersupply.model.request.AddTripRequest
import com.production.vedantwatersupply.model.request.FilterRequest
import com.production.vedantwatersupply.model.request.GetAllTripRequest
import com.production.vedantwatersupply.model.request.MonthFilterRequest
import com.production.vedantwatersupply.model.request.IdRequest

class TripViewModel(app: Application) : BaseViewModel(app) {

    val tripRepository = TripRepository()
    fun callAddUpdateTripApi(addUpdateTripRequest: AddTripRequest) {
        tripRepository.callAddUpdateTripApi(addUpdateTripRequest)
    }

    fun callMonthFilterApi(monthFilterRequest: MonthFilterRequest) {
        tripRepository.callMonthFilterApi(monthFilterRequest)
    }

    fun callGetAllTripListingApi(getAllTripRequest: GetAllTripRequest) {
        tripRepository.callGetAllTripListingApi(getAllTripRequest)
    }

    fun callTripDetailApi(tripDetailRequest: IdRequest) {
        tripRepository.callTripDetailApi(tripDetailRequest)
    }

    fun callTripDeleteApi(tripId: IdRequest) {
        tripRepository.callTripDeleteApi(tripId)
    }

    fun callTripCancelApi(tripId: IdRequest) {
        tripRepository.callTripCancelApi(tripId)
    }

    fun callFilterApi(filterRequest: FilterRequest) {
        tripRepository.callFilterApi(filterRequest)
    }

    fun callGetTankerAndDriverFixed() {
        tripRepository.callGetTankerAndDriverFixed()
    }
}