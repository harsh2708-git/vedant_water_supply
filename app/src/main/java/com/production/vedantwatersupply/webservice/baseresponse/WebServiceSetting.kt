package com.production.vedantwatersupply.webservice.baseresponse

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class WebServiceSetting {
    @SerializedName("success")
    @Expose
    var success: String? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("currentPage")
    @Expose
    var currentPage: Int = 0

    @SerializedName("nextPage")
    @Expose
    var nextPage: Int = 0

    companion object {
        val SUCCESS = "1"
        val FAILURE = "0"
        val NO_INTERNET = "-1"

        fun createMock(success: String, message: String,currentPage:Int,nextPage : Int): WebServiceSetting {
            val webServiceSetting = WebServiceSetting()
            webServiceSetting.success = success
            webServiceSetting.message = message
            webServiceSetting.currentPage = currentPage
            webServiceSetting.nextPage = nextPage
            return webServiceSetting
        }
        fun createMock(success: String, message: String,): WebServiceSetting {
            val webServiceSetting = WebServiceSetting()
            webServiceSetting.success = success
            webServiceSetting.message = message
            return webServiceSetting
        }
    }
}