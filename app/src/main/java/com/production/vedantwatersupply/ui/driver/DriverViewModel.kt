package com.production.vedantwatersupply.ui.driver

import android.app.Application
import com.production.vedantwatersupply.core.BaseViewModel
import com.production.vedantwatersupply.model.request.AddDriverExpensesRequest
import com.production.vedantwatersupply.model.request.DriverIdRequest
import com.production.vedantwatersupply.model.request.FilterRequest
import com.production.vedantwatersupply.model.request.GetDriverExpensesRequest
import com.production.vedantwatersupply.model.request.MonthFilterRequest
import com.production.vedantwatersupply.model.request.SearchDriverRequest
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

    fun callDriverDetailApi(driverIdRequest: DriverIdRequest) {
        driverRepository.callDriverDetailApi(driverIdRequest)
    }

    fun callGetTankerAndDriverFixed() {
        tripRepository.callGetTankerAndDriverFixed()
    }

    fun callSearchDriverApi(searchDriverRequest: SearchDriverRequest) {
        driverRepository.callSearchDriverApi(searchDriverRequest)
    }

    fun callAddUpdateDriverExpenseApi(addUpdateDriverExpenseRequest: AddDriverExpensesRequest) {
        driverRepository.callAddUpdateDriverExpenseApi(addUpdateDriverExpenseRequest)
    }
    fun callDriverDeleteApi(driverId: DriverIdRequest) {
        driverRepository.callDriverDeleteApi(driverId)
    }

    fun callFilterApi(filterRequest: FilterRequest) {
        tripRepository.callFilterApi(filterRequest)
    }

}