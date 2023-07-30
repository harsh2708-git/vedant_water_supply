package com.production.vedantwatersupply.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.production.vedantwatersupply.webservice.baseresponse.BaseWebserviceResponse

class MaintenanceData : BaseWebserviceResponse() {

    @SerializedName("_id")
    @Expose
    var id: String? = null

    @SerializedName("reference")
    @Expose
    var reference: String? = null

    @SerializedName("tanker")
    @Expose
    var tanker: Tanker? = null

    @SerializedName("description")
    @Expose
    var description: String? = null

    @SerializedName("amount")
    @Expose
    var amount: Int? = null

    @SerializedName("paymentMode")
    @Expose
    var paymentMode: String? = null

    @SerializedName("date")
    @Expose
    var date: String? = null

    @SerializedName("addedBy")
    @Expose
    var addedBy: AddedBy? = null

    @SerializedName("date_readable")
    @Expose
    var dateReadable: String? = null

}
