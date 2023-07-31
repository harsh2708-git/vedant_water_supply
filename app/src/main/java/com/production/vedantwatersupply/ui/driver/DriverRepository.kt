package com.production.vedantwatersupply.ui.driver

import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonElement
import com.production.vedantwatersupply.BuildConfig
import com.production.vedantwatersupply.model.request.GetDriverExpensesRequest
import com.production.vedantwatersupply.model.response.GetAllDriverExpensesResponse
import com.production.vedantwatersupply.model.response.GetAllMaintenanceResponse
import com.production.vedantwatersupply.utils.CommonUtils
import com.production.vedantwatersupply.webservice.ApiClient
import com.production.vedantwatersupply.webservice.baseresponse.Generics
import com.production.vedantwatersupply.webservice.baseresponse.WSGenericResponse
import com.production.vedantwatersupply.webservice.baseresponse.WebServiceSetting
import retrofit2.Call
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
                                WebServiceSetting.createMock(WebServiceSetting.SUCCESS, it)
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
}