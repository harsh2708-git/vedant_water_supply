package com.production.vedantwatersupply.ui.driver

import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonElement
import com.production.vedantwatersupply.BuildConfig
import com.production.vedantwatersupply.model.request.AddDriverExpensesRequest
import com.production.vedantwatersupply.model.request.DriverIdRequest
import com.production.vedantwatersupply.model.request.GetDriverExpensesRequest
import com.production.vedantwatersupply.model.request.SearchDriverRequest
import com.production.vedantwatersupply.model.response.AddMaintenanceResponse
import com.production.vedantwatersupply.model.response.AddUpdateDriverExpenseResponse
import com.production.vedantwatersupply.model.response.CommonEmptyResponse
import com.production.vedantwatersupply.model.response.DriverData
import com.production.vedantwatersupply.model.response.GetAllDriverExpensesResponse
import com.production.vedantwatersupply.model.response.GetAllMaintenanceResponse
import com.production.vedantwatersupply.model.response.MaintenanceData
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

class DriverRepository {


    var getAllDriverExpensesResponseMutableLiveData = MutableLiveData<GetAllDriverExpensesResponse>()

    fun callGetAllDriverExpences(request: GetDriverExpensesRequest) {
        ApiClient.iApiEndPoints.getAllDriverExpenses(CommonUtils.toFieldStringMap(request))
            .enqueue(object : retrofit2.Callback<WSGenericResponse<JsonElement>> {
                override fun onResponse(call: Call<WSGenericResponse<JsonElement>>, response: Response<WSGenericResponse<JsonElement>>) {
                    if (response.body() != null) {
                        var getAllDriverExpensesResponse = GetAllDriverExpensesResponse()
                        if (response.body()?.settings?.success == WebServiceSetting.SUCCESS) {
                            getAllDriverExpensesResponse = response.body()?.data?.let {
                                Generics.with(it).getAsObject(GetAllDriverExpensesResponse::class.java)
                            } ?: GetAllDriverExpensesResponse()
                            getAllDriverExpensesResponse.webServiceSetting = response.body()?.settings?.message?.let {
                                WebServiceSetting.createMock(WebServiceSetting.SUCCESS, it,response.body()?.settings?.currentPage?:0,response.body()?.settings?.nextPage?:0)
                            }
                        } else
                            getAllDriverExpensesResponse.webServiceSetting = response.body()?.settings?.message?.let {
                                WebServiceSetting.createMock(WebServiceSetting.FAILURE, it)
                            }

                        getAllDriverExpensesResponseMutableLiveData.postValue(getAllDriverExpensesResponse)
                    } else {
                        val getAllDriverExpensesResponse = GetAllDriverExpensesResponse()
                        getAllDriverExpensesResponse.webServiceSetting = WebServiceSetting.createMock(WebServiceSetting.FAILURE, response.message())
                        getAllDriverExpensesResponseMutableLiveData.postValue(getAllDriverExpensesResponse)
                    }
                }

                override fun onFailure(call: Call<WSGenericResponse<JsonElement>>, t: Throwable) {
                    val getAllDriverExpensesResponse = GetAllDriverExpensesResponse()
                    val message = if (BuildConfig.DEBUG) if (t.message.isNullOrEmpty()) "Internal server error" else t.message else "Internal server error"
                    getAllDriverExpensesResponse.webServiceSetting = WebServiceSetting.createMock(WebServiceSetting.FAILURE, message.toString())
                    getAllDriverExpensesResponseMutableLiveData.postValue(getAllDriverExpensesResponse)
                }
            })
    }

    var driverExpensesDetailResponseMutableLiveData = MutableLiveData<DriverData>()
    fun callDriverDetailApi(driverIdRequest: DriverIdRequest) {
        ApiClient.iApiEndPoints.driverExpenseDetail(CommonUtils.toFieldStringMap(driverIdRequest))
            .enqueue(object : Callback<WSGenericResponse<JsonElement>> {
                override fun onResponse(call: Call<WSGenericResponse<JsonElement>>, response: Response<WSGenericResponse<JsonElement>>) {
                    if (response.body() != null) {
                        var driverData = DriverData()
                        if (response.body()?.settings?.success == WebServiceSetting.SUCCESS) {
                            driverData = response.body()?.data?.let {
                                Generics.with(it).getAsObject(DriverData::class.java)
                            } ?: DriverData()
                            driverData.webServiceSetting = response.body()?.settings?.message?.let {
                                WebServiceSetting.createMock(WebServiceSetting.SUCCESS, it)
                            }
                        } else
                            driverData.webServiceSetting = response.body()?.settings?.message?.let {
                                WebServiceSetting.createMock(WebServiceSetting.FAILURE, it)
                            }

                        driverExpensesDetailResponseMutableLiveData.postValue(driverData)
                    } else {
                        val driverData = DriverData()
                        driverData.webServiceSetting = WebServiceSetting.createMock(WebServiceSetting.FAILURE, response.message())
                        driverExpensesDetailResponseMutableLiveData.postValue(driverData)
                    }
                }

                override fun onFailure(call: Call<WSGenericResponse<JsonElement>>, t: Throwable) {
                    val driverData = DriverData()
                    val message = if (BuildConfig.DEBUG) if (t.message.isNullOrEmpty()) "Internal server error" else t.message else "Internal server error"
                    driverData.webServiceSetting = WebServiceSetting.createMock(WebServiceSetting.FAILURE, message.toString())
                    driverExpensesDetailResponseMutableLiveData.postValue(driverData)
                }
            })
    }

    var searchDriverResponseMutableLiveData = MutableLiveData<ObserverModel<ArrayList<FilterItem>>>()
    fun callSearchDriverApi(searchDriverRequest: SearchDriverRequest) {
        ApiClient.iApiEndPoints.searchDriver(CommonUtils.toFieldStringMap(searchDriverRequest))
            .enqueue(object : Callback<WSListResponse<FilterItem>> {
                override fun onResponse(call: Call<WSListResponse<FilterItem>>, response: Response<WSListResponse<FilterItem>>) {
                    if (response.body() != null) {
                        if (response.body()!!.settings!!.success == WebServiceSetting.SUCCESS)
                            notifyDriverSearchObserver(response.body()!!.settings, response.body()!!.data)
                        else
                            notifyDriverSearchObserver(Settings(WebServiceSetting.FAILURE, response.body()!!.settings!!.message))
                    } else {
                        notifyDriverSearchObserver(Settings(WebServiceSetting.FAILURE, response.message()))
                    }
                }

                override fun onFailure(call: Call<WSListResponse<FilterItem>>, t: Throwable) {
                    val message = if (BuildConfig.DEBUG) if (t.message.isNullOrEmpty()) "Internal server error" else t.message else "Internal server error"
                    notifyDriverSearchObserver(Settings(WebServiceSetting.FAILURE, message!!))
                }
            })
    }
    fun notifyDriverSearchObserver(settings: Settings? = Settings(), data: ArrayList<FilterItem>? = null) {
        val observerModel = ObserverModel<ArrayList<FilterItem>>()
        observerModel.settings = settings
        observerModel.data = data
        searchDriverResponseMutableLiveData.value = observerModel
    }

    var addUpdateDriverExpenseResponseMutableLiveData = MutableLiveData<AddUpdateDriverExpenseResponse>()
    fun callAddUpdateDriverExpenseApi(addUpdateDriverExpenseRequest: AddDriverExpensesRequest) {
        ApiClient.iApiEndPoints.addUpdateDriver(CommonUtils.toFieldStringMap(addUpdateDriverExpenseRequest))
            .enqueue(object : retrofit2.Callback<WSGenericResponse<JsonElement>> {
                override fun onResponse(call: Call<WSGenericResponse<JsonElement>>, response: Response<WSGenericResponse<JsonElement>>) {
                    if (response.body() != null) {
                        var addUpdateDriverExpenseResponse = AddUpdateDriverExpenseResponse()
                        if (response.body()?.settings?.success == WebServiceSetting.SUCCESS) {
                            addUpdateDriverExpenseResponse = response.body()?.data?.let {
                                Generics.with(it).getAsObject(AddUpdateDriverExpenseResponse::class.java)
                            } ?: AddUpdateDriverExpenseResponse()
                            addUpdateDriverExpenseResponse.webServiceSetting = response.body()?.settings?.message?.let {
                                WebServiceSetting.createMock(WebServiceSetting.SUCCESS, it)
                            }
                        } else
                            addUpdateDriverExpenseResponse.webServiceSetting = response.body()?.settings?.message?.let {
                                WebServiceSetting.createMock(WebServiceSetting.FAILURE, it)
                            }

                        addUpdateDriverExpenseResponseMutableLiveData.postValue(addUpdateDriverExpenseResponse)
                    } else {
                        val addUpdateDriverExpenseResponse = AddUpdateDriverExpenseResponse()
                        addUpdateDriverExpenseResponse.webServiceSetting = WebServiceSetting.createMock(WebServiceSetting.FAILURE, response.message())
                        addUpdateDriverExpenseResponseMutableLiveData.postValue(addUpdateDriverExpenseResponse)
                    }
                }

                override fun onFailure(call: Call<WSGenericResponse<JsonElement>>, t: Throwable) {
                    val addUpdateDriverExpenseResponse = AddUpdateDriverExpenseResponse()
                    val message = if (BuildConfig.DEBUG) if (t.message.isNullOrEmpty()) "Internal server error" else t.message else "Internal server error"
                    addUpdateDriverExpenseResponse.webServiceSetting = WebServiceSetting.createMock(WebServiceSetting.FAILURE, message.toString())
                    addUpdateDriverExpenseResponseMutableLiveData.postValue(addUpdateDriverExpenseResponse)
                }
            })
    }

    var deleteDriverExpenseResponseMutableLiveData = MutableLiveData<CommonEmptyResponse>()
    fun callDriverDeleteApi(driverId: DriverIdRequest) {
        ApiClient.iApiEndPoints.deleteDriverApi(CommonUtils.toFieldStringMap(driverId))
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

                        deleteDriverExpenseResponseMutableLiveData.postValue(commonEmptyResponse)
                    } else {
                        val commonEmptyResponse = CommonEmptyResponse()
                        commonEmptyResponse.webServiceSetting = WebServiceSetting.createMock(WebServiceSetting.FAILURE, response.message())
                        deleteDriverExpenseResponseMutableLiveData.postValue(commonEmptyResponse)
                    }
                }

                override fun onFailure(call: Call<WSGenericResponse<JsonElement>>, t: Throwable) {
                    val commonEmptyResponse = CommonEmptyResponse()
                    val message = if (BuildConfig.DEBUG) if (t.message.isNullOrEmpty()) "Internal server error" else t.message else "Internal server error"
                    commonEmptyResponse.webServiceSetting = WebServiceSetting.createMock(WebServiceSetting.FAILURE, message.toString())
                    deleteDriverExpenseResponseMutableLiveData.postValue(commonEmptyResponse)
                }
            })
    }
}