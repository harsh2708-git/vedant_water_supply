package com.production.vedantwatersupply.webservice.baseresponse

import com.google.gson.annotations.SerializedName


class WebServiceSetting {
    @SerializedName("success")
    var success: String? = null

    @SerializedName("message")
    var message: String? = null

    @SerializedName("currentPage")
    var currentPage: String? = null

    companion object {
        val SUCCESS = "1"
        val FAILURE = "0"
        val NO_INTERNET = "-1"
        fun createMock(success: String, message: String): WebServiceSetting {
            val webServiceSetting = WebServiceSetting()
            webServiceSetting.success = success
            webServiceSetting.message = message
            return webServiceSetting
        }
    }
}