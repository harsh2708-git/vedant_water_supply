package com.production.vedantwatersupply.webservice

import com.google.gson.JsonElement
import com.production.vedantwatersupply.model.response.LoginResponse
import com.production.vedantwatersupply.utils.ApiConstants.Companion.API_DASHBOARD
import com.production.vedantwatersupply.utils.ApiConstants.Companion.API_LOGIN
import com.production.vedantwatersupply.utils.ApiConstants.Companion.Trip.Companion.API_ADD_UPDATE_TRIP
import com.production.vedantwatersupply.webservice.baseresponse.WSGenericResponse
import retrofit2.Call
import retrofit2.http.*

interface IApiEndPoints {

    @FormUrlEncoded
    @POST(API_LOGIN)
    fun loginAPI(@FieldMap hashMap: LinkedHashMap<String, Any>): Call<WSGenericResponse<JsonElement>>

    @GET(API_DASHBOARD)
    fun dashboardApi(): Call<WSGenericResponse<JsonElement>>

    @FormUrlEncoded
    @POST(API_ADD_UPDATE_TRIP)
    fun addUpdateTrip(@FieldMap hashMap: LinkedHashMap<String, Any>): Call<WSGenericResponse<JsonElement>>

}