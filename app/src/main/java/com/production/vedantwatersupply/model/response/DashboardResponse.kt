package com.production.vedantwatersupply.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.production.vedantwatersupply.webservice.baseresponse.BaseWebserviceResponse

class DashboardResponse: BaseWebserviceResponse() {

    @SerializedName("totalMaintainanceCount")
    @Expose
    var totalMaintainanceCount: Int? = null

    @SerializedName("totalMaintainanceAmount")
    @Expose
    var totalMaintainanceAmount: Int? = null

    @SerializedName("totalTripsCount")
    @Expose
    var totalTripsCount: Int? = null

    @SerializedName("totalTripsAmount")
    @Expose
    var totalTripsAmount: Int? = null

    @SerializedName("maintainanceData")
    @Expose
    var maintainanceData: List<MaintenanceData>? = null

    @SerializedName("driverExpenceData")
    @Expose
    var driverExpenceData: List<DriverData>? = null

    @SerializedName("tripData")
    @Expose
    var tripData: List<TripData>? = null

}