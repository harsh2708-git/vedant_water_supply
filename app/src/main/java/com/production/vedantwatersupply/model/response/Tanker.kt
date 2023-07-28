package com.production.vedantwatersupply.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Tanker {

    @SerializedName("_id")
    @Expose
    var id: String? = null

    @SerializedName("tankerNumber")
    @Expose
    var tankerNumber: String? = null

    @SerializedName("is_owned")
    @Expose
    var isOwned: Boolean? = null

}
