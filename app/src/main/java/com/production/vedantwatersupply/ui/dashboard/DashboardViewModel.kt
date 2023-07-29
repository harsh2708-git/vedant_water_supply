package com.production.vedantwatersupply.ui.dashboard

import android.app.Application
import com.production.vedantwatersupply.core.BaseViewModel
import com.production.vedantwatersupply.model.request.TripDetailRequest
import com.production.vedantwatersupply.ui.trips.TripRepository

class DashboardViewModel(app: Application) : BaseViewModel(app) {

    val dashboardRepository = DashboardRepository()
    val tripRepository = TripRepository()

    fun callDashboardApi() {
        dashboardRepository.callDashboardApi()
    }

    fun callTripDeleteApi(tripId: TripDetailRequest) {
        tripRepository.callTripDeleteApi(tripId)
    }

    fun callTripCancelApi(tripId: TripDetailRequest) {
        tripRepository.callTripCancelApi(tripId)
    }
}