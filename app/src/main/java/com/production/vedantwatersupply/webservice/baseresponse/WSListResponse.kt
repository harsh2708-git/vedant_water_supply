package com.production.vedantwatersupply.webservice.baseresponse


import com.google.gson.annotations.SerializedName
import java.util.*

class WSListResponse<T> : BaseResponse() {
    @SerializedName("data")
    var data: ArrayList<T>? = null
}
