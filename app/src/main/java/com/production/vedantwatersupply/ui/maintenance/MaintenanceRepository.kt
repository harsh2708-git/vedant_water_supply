package com.production.vedantwatersupply.ui.maintenance

import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonElement
import com.production.vedantwatersupply.BuildConfig
import com.production.vedantwatersupply.model.request.AddMaintenanceRequest
import com.production.vedantwatersupply.model.request.GetAllMaintenanceRequest
import com.production.vedantwatersupply.model.request.MaintenanceIdRequest
import com.production.vedantwatersupply.model.response.AddMaintenanceResponse
import com.production.vedantwatersupply.model.response.CommonEmptyResponse
import com.production.vedantwatersupply.model.response.GetAllMaintenanceResponse
import com.production.vedantwatersupply.model.response.MaintenanceData
import com.production.vedantwatersupply.utils.CommonUtils
import com.production.vedantwatersupply.webservice.ApiClient
import com.production.vedantwatersupply.webservice.baseresponse.Generics
import com.production.vedantwatersupply.webservice.baseresponse.WSGenericResponse
import com.production.vedantwatersupply.webservice.baseresponse.WebServiceSetting
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MaintenanceRepository {

    val addUpdateMaintenanceResponseMutableLiveData = MutableLiveData<AddMaintenanceResponse>()

    fun callAddUpdateMaintenanceApi(addUpdateMaintenanceRequest: AddMaintenanceRequest) {

        ApiClient.iApiEndPoints.addUpdateMaintenance(CommonUtils.toFieldStringMap(addUpdateMaintenanceRequest))
            .enqueue(object : retrofit2.Callback<WSGenericResponse<JsonElement>> {
                override fun onResponse(call: Call<WSGenericResponse<JsonElement>>, response: Response<WSGenericResponse<JsonElement>>) {
                    if (response.body() != null) {
                        var addMaintenanceResponse = AddMaintenanceResponse()
                        if (response.body()?.settings?.success == WebServiceSetting.SUCCESS) {
                            addMaintenanceResponse = response.body()?.data?.let {
                                Generics.with(it).getAsObject(AddMaintenanceResponse::class.java)
                            } ?: AddMaintenanceResponse()
                            addMaintenanceResponse.webServiceSetting = response.body()?.settings?.message?.let {
                                WebServiceSetting.createMock(WebServiceSetting.SUCCESS, it)
                            }
                        } else
                            addMaintenanceResponse.webServiceSetting = response.body()?.settings?.message?.let {
                                WebServiceSetting.createMock(WebServiceSetting.FAILURE, it)
                            }

                        addUpdateMaintenanceResponseMutableLiveData.postValue(addMaintenanceResponse)
                    } else {
                        val addMaintenanceResponse = AddMaintenanceResponse()
                        addMaintenanceResponse.webServiceSetting = WebServiceSetting.createMock(WebServiceSetting.FAILURE, response.message())
                        addUpdateMaintenanceResponseMutableLiveData.postValue(addMaintenanceResponse)
                    }
                }

                override fun onFailure(call: Call<WSGenericResponse<JsonElement>>, t: Throwable) {
                    val addMaintenanceResponse = AddMaintenanceResponse()
                    val message = if (BuildConfig.DEBUG) if (t.message.isNullOrEmpty()) "Internal server error" else t.message else "Internal server error"
                    addMaintenanceResponse.webServiceSetting = WebServiceSetting.createMock(WebServiceSetting.FAILURE, message.toString())
                    addUpdateMaintenanceResponseMutableLiveData.postValue(addMaintenanceResponse)
                }
            })

    }

    val getAllMaintenanceResponseMutableLiveData = MutableLiveData<GetAllMaintenanceResponse>()

    fun callGetAllMaintenanceApi(request: GetAllMaintenanceRequest) {
        ApiClient.iApiEndPoints.getAllMaintenance(CommonUtils.toFieldStringMap(request))
            .enqueue(object : retrofit2.Callback<WSGenericResponse<JsonElement>> {
                override fun onResponse(call: Call<WSGenericResponse<JsonElement>>, response: Response<WSGenericResponse<JsonElement>>) {
                    if (response.body() != null) {
                        var getAllMaintenanceResponse = GetAllMaintenanceResponse()
                        if (response.body()?.settings?.success == WebServiceSetting.SUCCESS) {
                            getAllMaintenanceResponse = response.body()?.data?.let {
                                Generics.with(it).getAsObject(GetAllMaintenanceResponse::class.java)
                            } ?: GetAllMaintenanceResponse()
                            getAllMaintenanceResponse.webServiceSetting = response.body()?.settings?.message?.let {
                                WebServiceSetting.createMock(WebServiceSetting.SUCCESS, it)
                            }
                        } else
                            getAllMaintenanceResponse.webServiceSetting = response.body()?.settings?.message?.let {
                                WebServiceSetting.createMock(WebServiceSetting.FAILURE, it)
                            }

                        getAllMaintenanceResponseMutableLiveData.postValue(getAllMaintenanceResponse)
                    } else {
                        val getAllMaintenanceResponse = GetAllMaintenanceResponse()
                        getAllMaintenanceResponse.webServiceSetting = WebServiceSetting.createMock(WebServiceSetting.FAILURE, response.message())
                        getAllMaintenanceResponseMutableLiveData.postValue(getAllMaintenanceResponse)
                    }
                }

                override fun onFailure(call: Call<WSGenericResponse<JsonElement>>, t: Throwable) {
                    val getAllMaintenanceResponse = GetAllMaintenanceResponse()
                    val message = if (BuildConfig.DEBUG) if (t.message.isNullOrEmpty()) "Internal server error" else t.message else "Internal server error"
                    getAllMaintenanceResponse.webServiceSetting = WebServiceSetting.createMock(WebServiceSetting.FAILURE, message.toString())
                    getAllMaintenanceResponseMutableLiveData.postValue(getAllMaintenanceResponse)
                }
            })
    }

    val maintenanceDetailResponseMutableLiveData = MutableLiveData<MaintenanceData>()

    fun callMaintenanceDetailApi(maintenanceDetailRequest: MaintenanceIdRequest) {
        ApiClient.iApiEndPoints.maintenanceDetailApi(CommonUtils.toFieldStringMap(maintenanceDetailRequest))
            .enqueue(object : Callback<WSGenericResponse<JsonElement>> {
                override fun onResponse(call: Call<WSGenericResponse<JsonElement>>, response: Response<WSGenericResponse<JsonElement>>) {
                    if (response.body() != null) {
                        var maintenanceData = MaintenanceData()
                        if (response.body()?.settings?.success == WebServiceSetting.SUCCESS) {
                            maintenanceData = response.body()?.data?.let {
                                Generics.with(it).getAsObject(MaintenanceData::class.java)
                            } ?: MaintenanceData()
                            maintenanceData.webServiceSetting = response.body()?.settings?.message?.let {
                                WebServiceSetting.createMock(WebServiceSetting.SUCCESS, it)
                            }
                        } else
                            maintenanceData.webServiceSetting = response.body()?.settings?.message?.let {
                                WebServiceSetting.createMock(WebServiceSetting.FAILURE, it)
                            }

                        maintenanceDetailResponseMutableLiveData.postValue(maintenanceData)
                    } else {
                        val maintenanceData = MaintenanceData()
                        maintenanceData.webServiceSetting = WebServiceSetting.createMock(WebServiceSetting.FAILURE, response.message())
                        maintenanceDetailResponseMutableLiveData.postValue(maintenanceData)
                    }
                }

                override fun onFailure(call: Call<WSGenericResponse<JsonElement>>, t: Throwable) {
                    val maintenanceData = MaintenanceData()
                    val message = if (BuildConfig.DEBUG) if (t.message.isNullOrEmpty()) "Internal server error" else t.message else "Internal server error"
                    maintenanceData.webServiceSetting = WebServiceSetting.createMock(WebServiceSetting.FAILURE, message.toString())
                    maintenanceDetailResponseMutableLiveData.postValue(maintenanceData)
                }
            })
    }

    val maintenanceDeleteResponseMutableLiveData = MutableLiveData<CommonEmptyResponse>()

    fun callMaintenanceDeleteApi(request: MaintenanceIdRequest) {
        ApiClient.iApiEndPoints.deleteMaintenanceApi(CommonUtils.toFieldStringMap(request))
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

                        maintenanceDeleteResponseMutableLiveData.postValue(commonEmptyResponse)
                    } else {
                        val commonEmptyResponse = CommonEmptyResponse()
                        commonEmptyResponse.webServiceSetting = WebServiceSetting.createMock(WebServiceSetting.FAILURE, response.message())
                        maintenanceDeleteResponseMutableLiveData.postValue(commonEmptyResponse)
                    }
                }

                override fun onFailure(call: Call<WSGenericResponse<JsonElement>>, t: Throwable) {
                    val commonEmptyResponse = CommonEmptyResponse()
                    val message = if (BuildConfig.DEBUG) if (t.message.isNullOrEmpty()) "Internal server error" else t.message else "Internal server error"
                    commonEmptyResponse.webServiceSetting = WebServiceSetting.createMock(WebServiceSetting.FAILURE, message.toString())
                    maintenanceDeleteResponseMutableLiveData.postValue(commonEmptyResponse)
                }
            })
    }
}