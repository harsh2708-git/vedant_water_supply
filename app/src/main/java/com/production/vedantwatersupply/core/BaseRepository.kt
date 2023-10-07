package com.production.vedantwatersupply.core

import androidx.lifecycle.MutableLiveData
import com.production.vedantwatersupply.BuildConfig
import com.production.vedantwatersupply.webservice.ApiClient
import com.production.vedantwatersupply.webservice.baseresponse.ObserverModel
import com.production.vedantwatersupply.webservice.baseresponse.WSListResponse
import com.production.vedantwatersupply.utils.filter.FilterItem
import com.production.vedantwatersupply.webservice.baseresponse.Settings
import com.production.vedantwatersupply.webservice.baseresponse.WebServiceSetting
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BaseRepository {

//    val addTripMutableLiveData = MutableLiveData<ObserverModel<ArrayList<FilterItem>>>()
//
//    fun callMonthFilterApi() {
//        ApiClient.iApiEndPoints.monthList()
//            .enqueue(object : Callback<WSListResponse<FilterItem>> {
//                override fun onResponse(call: Call<WSListResponse<FilterItem>>, response: Response<WSListResponse<FilterItem>>) {
//                    if (response.body() != null) {
//                        if (response.body()!!.settings!!.success == WebServiceSetting.SUCCESS)
//                            notifyMonthFilterObserver(response.body()!!.settings, response.body()!!.data)
//                        else
//                            notifyMonthFilterObserver(Settings(WebServiceSetting.FAILURE, response.body()!!.settings!!.message))
//                    } else {
//                        notifyMonthFilterObserver(Settings(WebServiceSetting.FAILURE, response.message()))
//                    }
//                }
//
//                override fun onFailure(call: Call<WSListResponse<FilterItem>>, t: Throwable) {
//                    val message = if (BuildConfig.DEBUG) if (t.message.isNullOrEmpty()) "Internal server error" else t.message else "Internal server error"
//                    notifyMonthFilterObserver(Settings(WebServiceSetting.FAILURE, message!!))
//                }
//            })
//    }
//
//    fun notifyMonthFilterObserver(settings: Settings? = Settings(), data: ArrayList<FilterItem>? = null) {
//        val observerModel = ObserverModel<ArrayList<FilterItem>>()
//        observerModel.settings = settings
//        observerModel.data = data
//        addTripMutableLiveData.value = observerModel
//    }
}