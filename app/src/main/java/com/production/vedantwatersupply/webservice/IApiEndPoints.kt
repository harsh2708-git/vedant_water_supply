package com.production.vedantwatersupply.webservice

import com.google.gson.JsonElement
import com.production.vedantwatersupply.utils.ApiConstants.Companion.API_DASHBOARD
import com.production.vedantwatersupply.utils.ApiConstants.Companion.API_FILTERS
import com.production.vedantwatersupply.utils.ApiConstants.Companion.API_GET_TANKER_AND_DRIVER_FIXED
import com.production.vedantwatersupply.utils.ApiConstants.Companion.API_LOGIN
import com.production.vedantwatersupply.utils.ApiConstants.Companion.API_MONTH_LIST
import com.production.vedantwatersupply.utils.ApiConstants.Companion.DriverExpenses.Companion.API_ADD_UPDATE_DRIVER_EXPENSE
import com.production.vedantwatersupply.utils.ApiConstants.Companion.DriverExpenses.Companion.API_DELETE_DRIVER_EXPENSE
import com.production.vedantwatersupply.utils.ApiConstants.Companion.DriverExpenses.Companion.API_DRIVER_EXPENSE_DETAIL
import com.production.vedantwatersupply.utils.ApiConstants.Companion.DriverExpenses.Companion.API_DRIVER_SEARCH
import com.production.vedantwatersupply.utils.ApiConstants.Companion.DriverExpenses.Companion.API_GET_ALL_DRIVER
import com.production.vedantwatersupply.utils.ApiConstants.Companion.Maintenance.Companion.API_ADD_UPDATE_MAINTENANCE
import com.production.vedantwatersupply.utils.ApiConstants.Companion.Maintenance.Companion.API_DELETE_MAINTENANCE
import com.production.vedantwatersupply.utils.ApiConstants.Companion.Maintenance.Companion.API_GET_ALL_MAINTENANCE
import com.production.vedantwatersupply.utils.ApiConstants.Companion.Maintenance.Companion.API_MAINTENANCE_DETAILS
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

    @FormUrlEncoded
    @POST(API_MONTH_LIST)
    fun monthList(@FieldMap hashMap: LinkedHashMap<String, Any>): Call<WSListResponse<FilterItem>>

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

    @FormUrlEncoded
    @POST(API_FILTERS)
    fun filterApi(@FieldMap hashMap: LinkedHashMap<String, Any>): Call<WSGenericResponse<JsonElement>>

    @POST(API_GET_TANKER_AND_DRIVER_FIXED)
    fun getTankerAndDriverFixed(): Call<WSGenericResponse<JsonElement>>

    @FormUrlEncoded
    @POST(API_ADD_UPDATE_MAINTENANCE)
    fun addUpdateMaintenance(@FieldMap hashMap: LinkedHashMap<String, Any>): Call<WSGenericResponse<JsonElement>>

    @FormUrlEncoded
    @POST(API_GET_ALL_MAINTENANCE)
    fun getAllMaintenance(@FieldMap hashMap: LinkedHashMap<String, Any>): Call<WSGenericResponse<JsonElement>>

    @FormUrlEncoded
    @POST(API_MAINTENANCE_DETAILS)
    fun maintenanceDetailApi(@FieldMap hashMap: LinkedHashMap<String, Any>): Call<WSGenericResponse<JsonElement>>

    @FormUrlEncoded
    @POST(API_DELETE_MAINTENANCE)
    fun deleteMaintenanceApi(@FieldMap hashMap: LinkedHashMap<String, Any>): Call<WSGenericResponse<JsonElement>>

    @FormUrlEncoded
    @POST(API_GET_ALL_DRIVER)
    fun getAllDriverExpenses(@FieldMap hashMap: LinkedHashMap<String, Any>): Call<WSGenericResponse<JsonElement>>

    @FormUrlEncoded
    @POST(API_DRIVER_EXPENSE_DETAIL)
    fun driverExpenseDetail(@FieldMap hashMap: LinkedHashMap<String, Any>): Call<WSGenericResponse<JsonElement>>

    @FormUrlEncoded
    @POST(API_DRIVER_SEARCH)
    fun searchDriver(@FieldMap hashMap: LinkedHashMap<String, Any>): Call<WSListResponse<FilterItem>>

    @FormUrlEncoded
    @POST(API_ADD_UPDATE_DRIVER_EXPENSE)
    fun addUpdateDriver(@FieldMap hashMap: LinkedHashMap<String, Any>): Call<WSGenericResponse<JsonElement>>

    @FormUrlEncoded
    @POST(API_DELETE_DRIVER_EXPENSE)
    fun deleteDriverApi(@FieldMap hashMap: LinkedHashMap<String, Any>): Call<WSGenericResponse<JsonElement>>

}