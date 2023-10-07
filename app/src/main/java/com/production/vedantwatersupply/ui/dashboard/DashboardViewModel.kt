package com.production.vedantwatersupply.ui.dashboard

import android.app.Application
import com.production.vedantwatersupply.core.BaseViewModel
import com.production.vedantwatersupply.model.request.DriverIdRequest
import com.production.vedantwatersupply.model.request.IdRequest
import com.production.vedantwatersupply.model.request.MaintenanceIdRequest
import com.production.vedantwatersupply.ui.driver.DriverRepository
import com.production.vedantwatersupply.ui.maintenance.MaintenanceRepository
import com.production.vedantwatersupply.ui.trips.TripRepository

class DashboardViewModel(app: Application) : BaseViewModel(app) {

    val dashboardRepository = DashboardRepository()
    val tripRepository = TripRepository()
    val maintenanceRepository = MaintenanceRepository()
    val driverRepository = DriverRepository()
    fun callDashboardApi() {
        dashboardRepository.callDashboardApi()
    }

    fun callTripDeleteApi(tripId: IdRequest) {
        tripRepository.callTripDeleteApi(tripId)
    }

    fun callTripCancelApi(tripId: IdRequest) {
        tripRepository.callTripCancelApi(tripId)
    }

    fun callMaintenanceDeleteApi(request: MaintenanceIdRequest) {
        maintenanceRepository.callMaintenanceDeleteApi(request)
    }

    fun callDriverDeleteApi(driverId: DriverIdRequest) {
        driverRepository.callDriverDeleteApi(driverId)
    }
}