package com.production.vedantwatersupply.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TripData {
    @SerializedName("_id")
    @Expose
    var id: String? = null

    @SerializedName("reference")
    @Expose
    var reference: String? = null

    @SerializedName("vehicle")
    @Expose
    var vehicle: List<Vehicle>? = null

    @SerializedName("driver")
    @Expose
    var driver: List<Driver>? = null

    @SerializedName("addedBy")
    @Expose
    var addedBy: List<AddedBy>? = null

    @SerializedName("tripDate")
    @Expose
    var tripDate: String? = null

    @SerializedName("fromDestination")
    @Expose
    var fromDestination: String? = null

    @SerializedName("toDestination")
    @Expose
    var toDestination: String? = null

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

    @SerializedName("amount")
    @Expose
    var amount: Int? = null

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
}
