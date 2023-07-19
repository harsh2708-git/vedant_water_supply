package com.production.vedantwatersupply.ui.login

import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonElement
import com.production.vedantwatersupply.BuildConfig
import com.production.vedantwatersupply.model.request.LoginRequest
import com.production.vedantwatersupply.model.response.LoginResponse
import com.production.vedantwatersupply.utils.CommonUtils
import com.production.vedantwatersupply.webservice.ApiClient
import com.production.vedantwatersupply.webservice.baseresponse.Generics
import com.production.vedantwatersupply.webservice.baseresponse.WSGenericResponse
import com.production.vedantwatersupply.webservice.baseresponse.WebServiceSetting
import retrofit2.Call
import retrofit2.Response

class LoginRepository {

    val loginResponseMutableLiveData = MutableLiveData<LoginResponse>()

    fun callLoginApi(loginRequest: LoginRequest){
        ApiClient.iApiEndPoints.loginAPI(CommonUtils.toFieldStringMap(loginRequest))
            .enqueue(object : retrofit2.Callback<WSGenericResponse<JsonElement>> {
                override fun onResponse(call: Call<WSGenericResponse<JsonElement>>, response: Response<WSGenericResponse<JsonElement>>) {
                    if (response.body() != null) {
                        var loginResponse = LoginResponse()
                        if (response.body()?.settings?.success == WebServiceSetting.SUCCESS) {
                            loginResponse = response.body()?.data?.let {
                                Generics.with(it).getAsObject(LoginResponse::class.java)
                            } ?: LoginResponse()
                            loginResponse.webServiceSetting = response.body()?.settings?.message?.let {
                                WebServiceSetting.createMock(WebServiceSetting.SUCCESS, it)
                            }
                        } else
                            loginResponse.webServiceSetting = response.body()?.settings?.message?.let {
                                WebServiceSetting.createMock(WebServiceSetting.FAILURE, it)
                            }

                        loginResponseMutableLiveData.postValue(loginResponse)
                    } else {
                        val loginResponse = LoginResponse()
                        loginResponse.webServiceSetting = WebServiceSetting.createMock(WebServiceSetting.FAILURE, response.message())
                        loginResponseMutableLiveData.postValue(loginResponse)
                    }
                }

                override fun onFailure(call: Call<WSGenericResponse<JsonElement>>, t: Throwable) {
                    val loginResponse = LoginResponse()
                    val message = if (BuildConfig.DEBUG) if (t.message.isNullOrEmpty()) "Internal server error" else t.message else "Internal server error"
                    loginResponse.webServiceSetting = WebServiceSetting.createMock(WebServiceSetting.FAILURE, message.toString())
                    loginResponseMutableLiveData.postValue(loginResponse)
                }
            })
    }

}
