package com.production.vedantwatersupply.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Vehicle {

    @SerializedName("_id")
    @Expose
    var id: String? = null

    @SerializedName("truckNumber")
    @Expose
    var truckNumber: String? = null

    @SerializedName("is_owned")
    @Expose
    var isOwned: Boolean? = null

}
