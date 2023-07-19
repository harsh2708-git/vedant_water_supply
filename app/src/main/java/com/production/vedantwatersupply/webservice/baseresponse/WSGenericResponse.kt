package com.production.vedantwatersupply.webservice.baseresponse

import com.google.gson.annotations.SerializedName

class WSGenericResponse<T> : BaseResponse() {
    @SerializedName("data")
    var data: T? = null
}
