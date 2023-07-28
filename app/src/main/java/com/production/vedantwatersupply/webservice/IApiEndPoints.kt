package com.production.vedantwatersupply.webservice

import com.google.gson.JsonElement
import com.production.vedantwatersupply.utils.ApiConstants.Companion.API_DASHBOARD
import com.production.vedantwatersupply.utils.ApiConstants.Companion.API_FILTERS
import com.production.vedantwatersupply.utils.ApiConstants.Companion.API_LOGIN
import com.production.vedantwatersupply.utils.ApiConstants.Companion.API_MONTH_LIST
import com.production.vedantwatersupply.utils.ApiConstants.Companion.Trip.Companion.API_ADD_UPDATE_TRIP
import com.production.vedantwatersupply.utils.ApiConstants.Companion.Trip.Companion.API_CANCEL_TRIP
import com.production.vedantwatersupply.utils.ApiConstants.Companion.Trip.Companion.API_DELETE_TRIP
import com.production.vedantwatersupply.utils.ApiConstants.Companion.Trip.Companion.API_GET_ALL_TRIP
import com.production.vedantwatersupply.utils.ApiConstants.Companion.Trip.Companion.API_TRIP_DETAIL
import com.production.vedantwatersupply.webservice.baseresponse.WSGenericResponse
import com.production.vedantwatersupply.webservice.baseresponse.WSListResponse
import com.production.vedantwatersupply.utils.filter.FilterItem
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

    @GET(API_MONTH_LIST)
    fun monthList(): Call<WSListResponse<FilterItem>>

    @FormUrlEncoded
    @POST(API_GET_ALL_TRIP)
    fun getAllTrip(@FieldMap hashMap: LinkedHashMap<String, Any>): Call<WSGenericResponse<JsonElement>>

    @FormUrlEncoded
    @POST(API_TRIP_DETAIL)
    fun tripDetailApi(@FieldMap hashMap: LinkedHashMap<String, Any>): Call<WSGenericResponse<JsonElement>>

    @FormUrlEncoded
    @POST(API_DELETE_TRIP)
    fun deleteTripApi(@FieldMap hashMap: LinkedHashMap<String, Any>): Call<WSGenericResponse<JsonElement>>

    @FormUrlEncoded
    @POST(API_CANCEL_TRIP)
    fun cancelTripApi(@FieldMap hashMap: LinkedHashMap<String, Any>): Call<WSGenericResponse<JsonElement>>

    @GET(API_FILTERS)
    fun filterApi(): Call<WSGenericResponse<JsonElement>>

}