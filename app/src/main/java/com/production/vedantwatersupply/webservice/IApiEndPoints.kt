package com.production.vedantwatersupply.webservice

import com.google.gson.JsonElement
import com.production.vedantwatersupply.model.response.LoginResponse
import com.production.vedantwatersupply.utils.ApiConstants.Companion.API_LOGIN
import com.production.vedantwatersupply.webservice.baseresponse.WSGenericResponse
import retrofit2.Call
import retrofit2.http.*

interface IApiEndPoints {

    @FormUrlEncoded
    @POST(API_LOGIN)
    fun loginAPI(@FieldMap hashMap: LinkedHashMap<String, Any>): Call<WSGenericResponse<JsonElement>>

}