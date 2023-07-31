package com.production.vedantwatersupply.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DriverData {

    @SerializedName("_id")
    @Expose
    var id: String? = null

    @SerializedName("reference")
    @Expose
    var reference: String? = null

    @SerializedName("driver")
    @Expose
    var driver: Driver? = null

    @SerializedName("date")
    @Expose
    var date: String? = null

    @SerializedName("amount")
    @Expose
    var amount: Double? = null

    @SerializedName("extraPaymentAmount")
    @Expose
    var extraPaymentAmount: Int? = null

    @SerializedName("paymentMode")
    @Expose
    var paymentMode: String? = null

    @SerializedName("description")
    @Expose
    var description: String? = null

    @SerializedName("addedBy")
    @Expose
    var addedBy: AddedBy? = null

    @SerializedName("createdAt")
    @Expose
    var createdAt: String? = null

    @SerializedName("updatedAt")
    @Expose
    var updatedAt: String? = null

    @SerializedName("__v")
    @Expose
    var v: Int? = null

    @SerializedName("date_readable")
    @Expose
    var dateReadable: String? = null

}
