package com.production.vedantwatersupply.ui.dashboard

import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonElement
import com.production.vedantwatersupply.BuildConfig
import com.production.vedantwatersupply.model.response.DashboardResponse
import com.production.vedantwatersupply.webservice.ApiClient
import com.production.vedantwatersupply.webservice.baseresponse.Generics
import com.production.vedantwatersupply.webservice.baseresponse.WSGenericResponse
import com.production.vedantwatersupply.webservice.baseresponse.WebServiceSetting
import retrofit2.Call
import retrofit2.Response

class DashboardRepository {

    val dashboardResponseLiveData = MutableLiveData<DashboardResponse>()

    fun callDashboardApi() {
        ApiClient.iApiEndPoints.dashboardApi()
            .enqueue(object : retrofit2.Callback<WSGenericResponse<JsonElement>> {
                override fun onResponse(call: Call<WSGenericResponse<JsonElement>>, response: Response<WSGenericResponse<JsonElement>>) {
                    if (response.body() != null) {
                        var dashboardResponse = DashboardResponse()
                        if (response.body()?.settings?.success == WebServiceSetting.SUCCESS) {
                            dashboardResponse = response.body()?.data?.let {
                                Generics.with(it).getAsObject(DashboardResponse::class.java)
                            } ?: DashboardResponse()
                            dashboardResponse.webServiceSetting = response.body()?.settings?.message?.let {
                                WebServiceSetting.createMock(WebServiceSetting.SUCCESS, it)
                            }
                        } else
                            dashboardResponse.webServiceSetting = response.body()?.settings?.message?.let {
                                WebServiceSetting.createMock(WebServiceSetting.FAILURE, it)
                            }

                        dashboardResponseLiveData.postValue(dashboardResponse)
                    } else {
                        val dashboardResponse = DashboardResponse()
                        dashboardResponse.webServiceSetting = WebServiceSetting.createMock(WebServiceSetting.FAILURE, response.message())
                        dashboardResponseLiveData.postValue(dashboardResponse)
                    }
                }

                override fun onFailure(call: Call<WSGenericResponse<JsonElement>>, t: Throwable) {
                    val dashboardResponse = DashboardResponse()
                    val message = if (BuildConfig.DEBUG) if (t.message.isNullOrEmpty()) "Internal server error" else t.message else "Internal server error"
                    dashboardResponse.webServiceSetting = WebServiceSetting.createMock(WebServiceSetting.FAILURE, message.toString())
                    dashboardResponseLiveData.postValue(dashboardResponse)
                }
            })
    }
}