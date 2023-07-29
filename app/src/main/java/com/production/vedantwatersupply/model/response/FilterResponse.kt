package com.production.vedantwatersupply.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.production.vedantwatersupply.utils.filter.FilterItem
import com.production.vedantwatersupply.webservice.baseresponse.BaseWebserviceResponse

class FilterResponse: BaseWebserviceResponse() {


    @SerializedName("years")
    @Expose
    var years: List<FilterItem>? = null

    @SerializedName("tanker", alternate = ["tankers"])
    @Expose
    var vehicle: List<FilterItem>? = null

    @SerializedName("driver", alternate = ["drivers"])
    @Expose
    var driver: List<FilterItem>? = null

    @SerializedName("waterType")
    @Expose
    var waterType: List<FilterItem>? = null

    @SerializedName("addedBy")
    @Expose
    var addedBy: List<FilterItem>? = null

}
