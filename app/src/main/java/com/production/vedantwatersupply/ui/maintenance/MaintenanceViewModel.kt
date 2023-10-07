package com.production.vedantwatersupply.ui.maintenance

import android.app.Application
import com.production.vedantwatersupply.core.BaseViewModel
import com.production.vedantwatersupply.model.request.AddMaintenanceRequest
import com.production.vedantwatersupply.model.request.FilterRequest
import com.production.vedantwatersupply.model.request.GetAllMaintenanceRequest
import com.production.vedantwatersupply.model.request.MaintenanceIdRequest
import com.production.vedantwatersupply.model.request.MonthFilterRequest
import com.production.vedantwatersupply.ui.trips.TripRepository

class MaintenanceViewModel(app: Application) : BaseViewModel(app) {

    var tripRepository = TripRepository()
    var maintenanceRepository = MaintenanceRepository()
    fun callGetTankerAndDriverFixed() {
        tripRepository.callGetTankerAndDriverFixed()
    }

    fun callAddUpdateMaintenanceApi(addUpdateMaintenanceRequest: AddMaintenanceRequest) {
        maintenanceRepository.callAddUpdateMaintenanceApi(addUpdateMaintenanceRequest)
    }

    fun callMonthFilterApi(monthFilterRequest: MonthFilterRequest) {
        tripRepository.callMonthFilterApi(monthFilterRequest)
    }

    fun callGetAllMaintenanceApi(allMaintenanceRequest: GetAllMaintenanceRequest) {
        maintenanceRepository.callGetAllMaintenanceApi(allMaintenanceRequest)
    }

    fun callMaintenanceDetailApi(maintenanceDetailRequest: MaintenanceIdRequest) {
        maintenanceRepository.callMaintenanceDetailApi(maintenanceDetailRequest)
    }

    fun callMaintenanceDeleteApi(request: MaintenanceIdRequest) {
        maintenanceRepository.callMaintenanceDeleteApi(request)
    }

    fun callFilterApi(filterRequest: FilterRequest) {
        tripRepository.callFilterApi(filterRequest)
    }
}