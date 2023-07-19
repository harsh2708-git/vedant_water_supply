package com.production.vedantwatersupply.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.production.vedantwatersupply.webservice.baseresponse.BaseWebserviceResponse

class LoginResponse : BaseWebserviceResponse() {
    @SerializedName("token")
    @Expose
    val token: String? = null

    @SerializedName("firstName")
    @Expose
    val firstName: String? = null

    @SerializedName("lastName")
    @Expose
    val lastName: String? = null

    @SerializedName("email")
    @Expose
    val email: String? = null
}