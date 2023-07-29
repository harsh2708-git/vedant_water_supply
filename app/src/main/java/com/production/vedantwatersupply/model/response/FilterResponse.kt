package com.production.vedantwatersupply.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.production.vedantwatersupply.utils.filter.FilterItem
import com.production.vedantwatersupply.webservice.baseresponse.BaseWebserviceResponse

class FilterResponse: BaseWebserviceResponse() {

    @SerializedName("vehicle")
    @Expose
    var vehicle: List<Tanker>? = null

    @SerializedName("driver")
    @Expose
    var driver: List<Driver>? = null

    @SerializedName("waterType")
    @Expose
    var waterType: List<FilterItem>? = null

    @SerializedName("addedBy")
    @Expose
    var addedBy: List<FilterItem>? = null

}
