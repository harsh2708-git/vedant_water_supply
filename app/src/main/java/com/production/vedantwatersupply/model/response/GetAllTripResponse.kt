package com.production.vedantwatersupply.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.production.vedantwatersupply.webservice.baseresponse.BaseWebserviceResponse

class GetAllTripResponse: BaseWebserviceResponse() {

    @SerializedName("totalTripsCount")
    @Expose
    var totalTripsCount: Int? = null

    @SerializedName("tripData")
    @Expose
    var tripData: List<TripData>? = null
}