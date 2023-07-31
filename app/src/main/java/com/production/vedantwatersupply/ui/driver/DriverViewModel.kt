package com.production.vedantwatersupply.ui.driver

import android.app.Application
import com.production.vedantwatersupply.core.BaseViewModel
import com.production.vedantwatersupply.model.request.GetDriverExpensesRequest
import com.production.vedantwatersupply.model.request.MonthFilterRequest
import com.production.vedantwatersupply.ui.trips.TripRepository

class DriverViewModel(app: Application): BaseViewModel(app) {

    val driverRepository = DriverRepository()
    val tripRepository = TripRepository()
    fun callMonthFilterApi(monthFilterRequest: MonthFilterRequest) {
        tripRepository.callMonthFilterApi(monthFilterRequest)
    }

    fun callGetAllDriverExpences(driverExpensesRequest: GetDriverExpensesRequest) {
        driverRepository.callGetAllDriverExpences(driverExpensesRequest)
    }
}