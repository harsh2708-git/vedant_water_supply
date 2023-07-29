package com.production.vedantwatersupply.ui.trips

import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonElement
import com.production.vedantwatersupply.BuildConfig
import com.production.vedantwatersupply.model.request.AddTripRequest
import com.production.vedantwatersupply.model.request.FilterRequest
import com.production.vedantwatersupply.model.request.GetAllTripRequest
import com.production.vedantwatersupply.model.request.TripDetailRequest
import com.production.vedantwatersupply.model.response.AddTripResponse
import com.production.vedantwatersupply.model.response.CommonEmptyResponse
import com.production.vedantwatersupply.model.response.FilterResponse
import com.production.vedantwatersupply.model.response.GetAllTripResponse
import com.production.vedantwatersupply.model.response.TripData
import com.production.vedantwatersupply.utils.CommonUtils
import com.production.vedantwatersupply.utils.filter.FilterItem
import com.production.vedantwatersupply.webservice.ApiClient
import com.production.vedantwatersupply.webservice.baseresponse.Generics
import com.production.vedantwatersupply.webservice.baseresponse.ObserverModel
import com.production.vedantwatersupply.webservice.baseresponse.Settings
import com.production.vedantwatersupply.webservice.baseresponse.WSGenericResponse
import com.production.vedantwatersupply.webservice.baseresponse.WSListResponse
import com.production.vedantwatersupply.webservice.baseresponse.WebServiceSetting
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TripRepository {

    val addTripMutableLiveData = MutableLiveData<AddTripResponse>()
    fun callAddUpdateTripApi(addUpdateTripRequest: AddTripRequest) {
        ApiClient.iApiEndPoints.addUpdateTrip(CommonUtils.toFieldStringMap(addUpdateTripRequest))
            .enqueue(object : retrofit2.Callback<WSGenericResponse<JsonElement>> {
                override fun onResponse(call: Call<WSGenericResponse<JsonElement>>, response: Response<WSGenericResponse<JsonElement>>) {
                    if (response.body() != null) {
                        var addTripResponse = AddTripResponse()
                        if (response.body()?.settings?.success == WebServiceSetting.SUCCESS) {
                            addTripResponse = response.body()?.data?.let {
                                Generics.with(it).getAsObject(AddTripResponse::class.java)
                            } ?: AddTripResponse()
                            addTripResponse.webServiceSetting = response.body()?.settings?.message?.let {
                                WebServiceSetting.createMock(WebServiceSetting.SUCCESS, it)
                            }
                        } else
                            addTripResponse.webServiceSetting = response.body()?.settings?.message?.let {
                                WebServiceSetting.createMock(WebServiceSetting.FAILURE, it)
                            }

                        addTripMutableLiveData.postValue(addTripResponse)
                    } else {
                        val addTripResponse = AddTripResponse()
                        addTripResponse.webServiceSetting = WebServiceSetting.createMock(WebServiceSetting.FAILURE, response.message())
                        addTripMutableLiveData.postValue(addTripResponse)
                    }
                }

                override fun onFailure(call: Call<WSGenericResponse<JsonElement>>, t: Throwable) {
                    val addTripResponse = AddTripResponse()
                    val message = if (BuildConfig.DEBUG) if (t.message.isNullOrEmpty()) "Internal server error" else t.message else "Internal server error"
                    addTripResponse.webServiceSetting = WebServiceSetting.createMock(WebServiceSetting.FAILURE, message.toString())
                    addTripMutableLiveData.postValue(addTripResponse)
                }
            })
    }

    val monthFilterLiveData = MutableLiveData<ObserverModel<ArrayList<FilterItem>>>()
    fun callMonthFilterApi() {
        ApiClient.iApiEndPoints.monthList()
            .enqueue(object : Callback<WSListResponse<FilterItem>> {
                override fun onResponse(call: Call<WSListResponse<FilterItem>>, response: Response<WSListResponse<FilterItem>>) {
                    if (response.body() != null) {
                        if (response.body()!!.settings!!.success == WebServiceSetting.SUCCESS)
                            notifyMonthFilterObserver(response.body()!!.settings, response.body()!!.data)
                        else
                            notifyMonthFilterObserver(Settings(WebServiceSetting.FAILURE, response.body()!!.settings!!.message))
                    } else {
                        notifyMonthFilterObserver(Settings(WebServiceSetting.FAILURE, response.message()))
                    }
                }

                override fun onFailure(call: Call<WSListResponse<FilterItem>>, t: Throwable) {
                    val message = if (BuildConfig.DEBUG) if (t.message.isNullOrEmpty()) "Internal server error" else t.message else "Internal server error"
                    notifyMonthFilterObserver(Settings(WebServiceSetting.FAILURE, message!!))
                }
            })
    }

    fun notifyMonthFilterObserver(settings: Settings? = Settings(), data: ArrayList<FilterItem>? = null) {
        val observerModel = ObserverModel<ArrayList<FilterItem>>()
        observerModel.settings = settings
        observerModel.data = data
        monthFilterLiveData.value = observerModel
    }

    val getAllTripResponseMutableLiveData = MutableLiveData<GetAllTripResponse>()
    fun callGetAllTripListingApi(request: GetAllTripRequest) {
        ApiClient.iApiEndPoints.getAllTrip(CommonUtils.toFieldStringMap(request))
            .enqueue(object : retrofit2.Callback<WSGenericResponse<JsonElement>> {
                override fun onResponse(call: Call<WSGenericResponse<JsonElement>>, response: Response<WSGenericResponse<JsonElement>>) {
                    if (response.body() != null) {
                        var getAllTripResponse = GetAllTripResponse()
                        if (response.body()?.settings?.success == WebServiceSetting.SUCCESS) {
                            getAllTripResponse = response.body()?.data?.let {
                                Generics.with(it).getAsObject(GetAllTripResponse::class.java)
                            } ?: GetAllTripResponse()
                            getAllTripResponse.webServiceSetting = response.body()?.settings?.message?.let {
                                WebServiceSetting.createMock(WebServiceSetting.SUCCESS, it)
                            }
                        } else
                            getAllTripResponse.webServiceSetting = response.body()?.settings?.message?.let {
                                WebServiceSetting.createMock(WebServiceSetting.FAILURE, it)
                            }

                        getAllTripResponseMutableLiveData.postValue(getAllTripResponse)
                    } else {
                        val getAllTripResponse = GetAllTripResponse()
                        getAllTripResponse.webServiceSetting = WebServiceSetting.createMock(WebServiceSetting.FAILURE, response.message())
                        getAllTripResponseMutableLiveData.postValue(getAllTripResponse)
                    }
                }

                override fun onFailure(call: Call<WSGenericResponse<JsonElement>>, t: Throwable) {
                    val getAllTripResponse = GetAllTripResponse()
                    val message = if (BuildConfig.DEBUG) if (t.message.isNullOrEmpty()) "Internal server error" else t.message else "Internal server error"
                    getAllTripResponse.webServiceSetting = WebServiceSetting.createMock(WebServiceSetting.FAILURE, message.toString())
                    getAllTripResponseMutableLiveData.postValue(getAllTripResponse)
                }
            })
    }

    val tripDetailResponseMutableLiveData = MutableLiveData<TripData>()
    fun callTripDetailApi(tripDetailRequest: TripDetailRequest) {
        ApiClient.iApiEndPoints.tripDetailApi(CommonUtils.toFieldStringMap(tripDetailRequest))
            .enqueue(object : Callback<WSGenericResponse<JsonElement>> {
                override fun onResponse(call: Call<WSGenericResponse<JsonElement>>, response: Response<WSGenericResponse<JsonElement>>) {
                    if (response.body() != null) {
                        var tripDateResponse = TripData()
                        if (response.body()?.settings?.success == WebServiceSetting.SUCCESS) {
                            tripDateResponse = response.body()?.data?.let {
                                Generics.with(it).getAsObject(TripData::class.java)
                            } ?: TripData()
                            tripDateResponse.webServiceSetting = response.body()?.settings?.message?.let {
                                WebServiceSetting.createMock(WebServiceSetting.SUCCESS, it)
                            }
                        } else
                            tripDateResponse.webServiceSetting = response.body()?.settings?.message?.let {
                                WebServiceSetting.createMock(WebServiceSetting.FAILURE, it)
                            }

                        tripDetailResponseMutableLiveData.postValue(tripDateResponse)
                    } else {
                        val tripDateResponse = TripData()
                        tripDateResponse.webServiceSetting = WebServiceSetting.createMock(WebServiceSetting.FAILURE, response.message())
                        tripDetailResponseMutableLiveData.postValue(tripDateResponse)
                    }
                }

                override fun onFailure(call: Call<WSGenericResponse<JsonElement>>, t: Throwable) {
                    val tripDateResponse = TripData()
                    val message = if (BuildConfig.DEBUG) if (t.message.isNullOrEmpty()) "Internal server error" else t.message else "Internal server error"
                    tripDateResponse.webServiceSetting = WebServiceSetting.createMock(WebServiceSetting.FAILURE, message.toString())
                    tripDetailResponseMutableLiveData.postValue(tripDateResponse)
                }
            })
    }

    val tripDeleteResponseMutableLiveData = MutableLiveData<CommonEmptyResponse>()
    fun callTripDeleteApi(tripId: TripDetailRequest) {
        ApiClient.iApiEndPoints.deleteTripApi(CommonUtils.toFieldStringMap(tripId))
            .enqueue(object : Callback<WSGenericResponse<JsonElement>> {
                override fun onResponse(call: Call<WSGenericResponse<JsonElement>>, response: Response<WSGenericResponse<JsonElement>>) {
                    if (response.body() != null) {
                        var commonEmptyResponse = CommonEmptyResponse()
                        if (response.body()?.settings?.success == WebServiceSetting.SUCCESS) {
                            commonEmptyResponse = response.body()?.data?.let {
                                Generics.with(it).getAsObject(CommonEmptyResponse::class.java)
                            } ?: CommonEmptyResponse()
                            commonEmptyResponse.webServiceSetting = response.body()?.settings?.message?.let {
                                WebServiceSetting.createMock(WebServiceSetting.SUCCESS, it)
                            }
                        } else
                            commonEmptyResponse.webServiceSetting = response.body()?.settings?.message?.let {
                                WebServiceSetting.createMock(WebServiceSetting.FAILURE, it)
                            }

                        tripDeleteResponseMutableLiveData.postValue(commonEmptyResponse)
                    } else {
                        val commonEmptyResponse = CommonEmptyResponse()
                        commonEmptyResponse.webServiceSetting = WebServiceSetting.createMock(WebServiceSetting.FAILURE, response.message())
                        tripDeleteResponseMutableLiveData.postValue(commonEmptyResponse)
                    }
                }

                override fun onFailure(call: Call<WSGenericResponse<JsonElement>>, t: Throwable) {
                    val commonEmptyResponse = CommonEmptyResponse()
                    val message = if (BuildConfig.DEBUG) if (t.message.isNullOrEmpty()) "Internal server error" else t.message else "Internal server error"
                    commonEmptyResponse.webServiceSetting = WebServiceSetting.createMock(WebServiceSetting.FAILURE, message.toString())
                    tripDeleteResponseMutableLiveData.postValue(commonEmptyResponse)
                }
            })
    }

    val tripCancelResponseMutableLiveData = MutableLiveData<CommonEmptyResponse>()
    fun callTripCancelApi(tripId: TripDetailRequest) {
        ApiClient.iApiEndPoints.cancelTripApi(CommonUtils.toFieldStringMap(tripId))
            .enqueue(object : Callback<WSGenericResponse<JsonElement>> {
                override fun onResponse(call: Call<WSGenericResponse<JsonElement>>, response: Response<WSGenericResponse<JsonElement>>) {
                    if (response.body() != null) {
                        var commonEmptyResponse = CommonEmptyResponse()
                        if (response.body()?.settings?.success == WebServiceSetting.SUCCESS) {
                            commonEmptyResponse = response.body()?.data?.let {
                                Generics.with(it).getAsObject(CommonEmptyResponse::class.java)
                            } ?: CommonEmptyResponse()
                            commonEmptyResponse.webServiceSetting = response.body()?.settings?.message?.let {
                                WebServiceSetting.createMock(WebServiceSetting.SUCCESS, it)
                            }
                        } else
                            commonEmptyResponse.webServiceSetting = response.body()?.settings?.message?.let {
                                WebServiceSetting.createMock(WebServiceSetting.FAILURE, it)
                            }

                        tripCancelResponseMutableLiveData.postValue(commonEmptyResponse)
                    } else {
                        val commonEmptyResponse = CommonEmptyResponse()
                        commonEmptyResponse.webServiceSetting = WebServiceSetting.createMock(WebServiceSetting.FAILURE, response.message())
                        tripCancelResponseMutableLiveData.postValue(commonEmptyResponse)
                    }
                }

                override fun onFailure(call: Call<WSGenericResponse<JsonElement>>, t: Throwable) {
                    val commonEmptyResponse = CommonEmptyResponse()
                    val message = if (BuildConfig.DEBUG) if (t.message.isNullOrEmpty()) "Internal server error" else t.message else "Internal server error"
                    commonEmptyResponse.webServiceSetting = WebServiceSetting.createMock(WebServiceSetting.FAILURE, message.toString())
                    tripCancelResponseMutableLiveData.postValue(commonEmptyResponse)
                }
            })
    }

    val filterResponseMutableLiveData = MutableLiveData<FilterResponse>()
    fun callFilterApi(filterRequest: FilterRequest) {
        ApiClient.iApiEndPoints.filterApi(CommonUtils.toFieldStringMap(filterRequest))
            .enqueue(object : Callback<WSGenericResponse<JsonElement>> {
                override fun onResponse(call: Call<WSGenericResponse<JsonElement>>, response: Response<WSGenericResponse<JsonElement>>) {
                    if (response.body() != null) {
                        var filterResponse = FilterResponse()
                        if (response.body()?.settings?.success == WebServiceSetting.SUCCESS) {
                            filterResponse = response.body()?.data?.let {
                                Generics.with(it).getAsObject(FilterResponse::class.java)
                            } ?: FilterResponse()
                            filterResponse.webServiceSetting = response.body()?.settings?.message?.let {
                                WebServiceSetting.createMock(WebServiceSetting.SUCCESS, it)
                            }
                        } else
                            filterResponse.webServiceSetting = response.body()?.settings?.message?.let {
                                WebServiceSetting.createMock(WebServiceSetting.FAILURE, it)
                            }

                        filterResponseMutableLiveData.postValue(filterResponse)
                    } else {
                        val filterResponse = FilterResponse()
                        filterResponse.webServiceSetting = WebServiceSetting.createMock(WebServiceSetting.FAILURE, response.message())
                        filterResponseMutableLiveData.postValue(filterResponse)
                    }
                }

                override fun onFailure(call: Call<WSGenericResponse<JsonElement>>, t: Throwable) {
                    val filterResponse = FilterResponse()
                    val message = if (BuildConfig.DEBUG) if (t.message.isNullOrEmpty()) "Internal server error" else t.message else "Internal server error"
                    filterResponse.webServiceSetting = WebServiceSetting.createMock(WebServiceSetting.FAILURE, message.toString())
                    filterResponseMutableLiveData.postValue(filterResponse)
                }
            })
    }

    val getTankerAndDriverFixedResponseMutableLiveData = MutableLiveData<FilterResponse>()
    fun callGetTankerAndDriverFixed() {
        ApiClient.iApiEndPoints.getTankerAndDriverFixed()
            .enqueue(object : Callback<WSGenericResponse<JsonElement>> {
                override fun onResponse(call: Call<WSGenericResponse<JsonElement>>, response: Response<WSGenericResponse<JsonElement>>) {
                    if (response.body() != null) {
                        var tankerAndDriverFixed = FilterResponse()
                        if (response.body()?.settings?.success == WebServiceSetting.SUCCESS) {
                            tankerAndDriverFixed = response.body()?.data?.let {
                                Generics.with(it).getAsObject(FilterResponse::class.java)
                            } ?: FilterResponse()
                            tankerAndDriverFixed.webServiceSetting = response.body()?.settings?.message?.let {
                                WebServiceSetting.createMock(WebServiceSetting.SUCCESS, it)
                            }
                        } else
                            tankerAndDriverFixed.webServiceSetting = response.body()?.settings?.message?.let {
                                WebServiceSetting.createMock(WebServiceSetting.FAILURE, it)
                            }

                        getTankerAndDriverFixedResponseMutableLiveData.postValue(tankerAndDriverFixed)
                    } else {
                        val tankerAndDriverFixed = FilterResponse()
                        tankerAndDriverFixed.webServiceSetting = WebServiceSetting.createMock(WebServiceSetting.FAILURE, response.message())
                        getTankerAndDriverFixedResponseMutableLiveData.postValue(tankerAndDriverFixed)
                    }
                }

                override fun onFailure(call: Call<WSGenericResponse<JsonElement>>, t: Throwable) {
                    val tankerAndDriverFixed = FilterResponse()
                    val message = if (BuildConfig.DEBUG) if (t.message.isNullOrEmpty()) "Internal server error" else t.message else "Internal server error"
                    tankerAndDriverFixed.webServiceSetting = WebServiceSetting.createMock(WebServiceSetting.FAILURE, message.toString())
                    getTankerAndDriverFixedResponseMutableLiveData.postValue(tankerAndDriverFixed)
                }
            })
    }

}
