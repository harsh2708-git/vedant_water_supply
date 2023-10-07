package com.production.vedantwatersupply.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.production.vedantwatersupply.webservice.baseresponse.BaseWebserviceResponse


class GetAllMaintenanceResponse : BaseWebserviceResponse() {

    @SerializedName("totalMaintainanceCount")
    @Expose
    var totalMaintainanceCount: Int? = null

    @SerializedName("totalMaintainanceAmount")
    @Expose
    var totalMaintainanceAmount: Int? = null

    @SerializedName("maintainanceData")
    @Expose
    var maintainanceData: List<MaintenanceData>? = null

}