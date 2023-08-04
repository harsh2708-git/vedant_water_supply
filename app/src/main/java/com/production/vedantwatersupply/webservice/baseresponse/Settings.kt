package com.production.vedantwatersupply.webservice.baseresponse

import com.google.gson.annotations.SerializedName

class Settings {
    @SerializedName("success")
    var success: String? = "0"      // 0 -> Failure, 1->Success, 101-> Network error(showing network exception/no network)

    @SerializedName("message")
    var message = ""

    @SerializedName("currentPage")
    var currPage = 0

//    @SerializedName("prev_page")
//    var prevPage = "0"

//    @SerializedName("next_page")
//    var nextPage = "0"


    val isSuccess: Boolean
        get() = success != null && success!!.equals("1", ignoreCase = true)

    val isNetworkError: Boolean
        get() = success != null && success!!.equals(NETWORK_ERROR, ignoreCase = true)

    /*constructor(success: String, message: String, currPage: String, prevPage: String, nextPage: String) {
        this.success = success
        this.message = message

        this.currPage = currPage
        this.prevPage = prevPage
        this.nextPage = nextPage
    }*/

    constructor() {

    }

    constructor(success: String, message: String) {
        this.success = success
        this.message = message
    }

    companion object {

        val AUTHENTICATION_ERROR = "402"
        val NETWORK_ERROR = "1001"
    }
}

