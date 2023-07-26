package com.production.vedantwatersupply.ui.trips

import android.app.Application
import android.content.Context
import com.production.vedantwatersupply.R
import com.production.vedantwatersupply.core.BaseViewModel
import com.production.vedantwatersupply.model.request.AddTripRequest
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
}