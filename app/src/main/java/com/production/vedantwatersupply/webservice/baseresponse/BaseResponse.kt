package com.production.vedantwatersupply.webservice.baseresponse

import com.google.gson.annotations.SerializedName

open class BaseResponse {
    @SerializedName("settings")
    var settings: Settings? = null
}
