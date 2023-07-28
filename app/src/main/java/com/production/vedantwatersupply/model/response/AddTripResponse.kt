package com.production.vedantwatersupply.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.production.vedantwatersupply.webservice.baseresponse.BaseWebserviceResponse


class AddTripResponse: BaseWebserviceResponse() {
    @SerializedName("reference")
    @Expose
    var reference: String? = null

    @SerializedName("tanker")
    @Expose
    var tanker: String? = null

    @SerializedName("driver")
    @Expose
    var driver: String? = null

    @SerializedName("addedBy")
    @Expose
    var addedBy: String? = null

    @SerializedName("tripDate")
    @Expose
    var tripDate: String? = null

    @SerializedName("fillingSite")
    @Expose
    var fillingSite: String? = null

    @SerializedName("destinationSite")
    @Expose
    var destinationSite: String? = null

    @SerializedName("fuelFilledBy")
    @Expose
    var fuelFilledBy: String? = null

    @SerializedName("customerName")
    @Expose
    var customerName: String? = null

    @SerializedName("customerMobile")
    @Expose
    var customerMobile: String? = null

    @SerializedName("waterType")
    @Expose
    var waterType: String? = null

    @SerializedName("fuelAmount")
    @Expose
    var fuelAmount: Float? = null

    @SerializedName("paymentMode")
    @Expose
    var paymentMode: String? = null

    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("description")
    @Expose
    var description: String? = null

    @SerializedName("createdAt")
    @Expose
    var createdAt: String? = null

    @SerializedName("updatedAt")
    @Expose
    var updatedAt: String? = null

    @SerializedName("__v")
    @Expose
    var v: Int? = null

    @SerializedName("tripDate_readable")
    @Expose
    var tripDateReadable: String? = null

    @SerializedName("id")
    @Expose
    var id: String? = null
}