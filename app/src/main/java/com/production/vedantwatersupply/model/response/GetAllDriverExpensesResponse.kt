package com.production.vedantwatersupply.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.production.vedantwatersupply.webservice.baseresponse.BaseWebserviceResponse


class GetAllDriverExpensesResponse: BaseWebserviceResponse() {

    @SerializedName("totalDriverExpenceCount")
    @Expose
    var totalDriverExpenceCount: Int? = null

    @SerializedName("driverExpenceData")
    @Expose
    var driverExpenceData: List<DriverData>? = null

}