package com.production.vedantwatersupply.ui.trips

import android.app.Application
import android.content.Context
import com.production.vedantwatersupply.R
import com.production.vedantwatersupply.core.BaseRepository
import com.production.vedantwatersupply.core.BaseViewModel
import com.production.vedantwatersupply.model.request.AddTripRequest
import com.production.vedantwatersupply.model.request.GetAllTripRequest
import com.production.vedantwatersupply.model.request.TripDetailRequest
import com.production.vedantwatersupply.utils.AppConstants.Trip.Companion.OTHER_DRIVER
import com.production.vedantwatersupply.utils.AppConstants.Trip.Companion.OTHER_TANKER
import com.production.vedantwatersupply.utils.AppConstants.Trip.Companion.OWN_TANKER
import com.production.vedantwatersupply.utils.AppConstants.Trip.Companion.PERMANENT_DRIVER
import com.production.vedantwatersupply.utils.CommonUtils

class TripViewModel(app: Application) : BaseViewModel(app) {

    val tripRepository = TripRepository()
    fun callAddUpdateTripApi(addUpdateTripRequest: AddTripRequest) {
        tripRepository.callAddUpdateTripApi(addUpdateTripRequest)
    }

    fun callMonthFilterApi() {
        tripRepository.callMonthFilterApi()
    }

    fun callGetAllTripListingApi(getAllTripRequest: GetAllTripRequest) {
        tripRepository.callGetAllTripListingApi(getAllTripRequest)
    }

    fun callTripDetailApi(tripDetailRequest: TripDetailRequest) {
        tripRepository.callTripDetailApi(tripDetailRequest)
    }

    fun callTripDeleteApi(tripId: TripDetailRequest) {
        tripRepository.callTripDeleteApi(tripId)
    }

    fun callTripCancelApi(tripId: TripDetailRequest) {
        tripRepository.callTripCancelApi(tripId)
    }

    fun callFilterApi() {
        tripRepository.callFilterApi()
    }
}