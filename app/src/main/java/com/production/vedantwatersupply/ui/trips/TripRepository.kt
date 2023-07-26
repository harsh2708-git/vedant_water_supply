package com.production.vedantwatersupply.ui.trips

import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonElement
import com.production.vedantwatersupply.BuildConfig
import com.production.vedantwatersupply.model.request.AddTripRequest
import com.production.vedantwatersupply.model.response.AddTripResponse
import com.production.vedantwatersupply.utils.CommonUtils
import com.production.vedantwatersupply.webservice.ApiClient
import com.production.vedantwatersupply.webservice.baseresponse.Generics
import com.production.vedantwatersupply.webservice.baseresponse.WSGenericResponse
import com.production.vedantwatersupply.webservice.baseresponse.WebServiceSetting
import retrofit2.Call
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

}
