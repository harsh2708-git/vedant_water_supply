package com.production.vedantwatersupply.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Driver {

    @SerializedName("_id")
    @Expose
    var id: String? = null

    @SerializedName("driverName")
    @Expose
    var driverName: String? = null

    @SerializedName("is_parmanent")
    @Expose
    var isParmanent: Boolean? = null

}
