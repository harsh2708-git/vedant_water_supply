package com.production.vedantwatersupply.webservice.baseresponse

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Ignore
import com.google.gson.annotations.SerializedName


open class BaseWebserviceResponse() : Parcelable {
    @Ignore
    @SerializedName("settings")
    var webServiceSetting: WebServiceSetting? = null

    constructor(parcel: Parcel) : this() {

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BaseWebserviceResponse> {
        override fun createFromParcel(parcel: Parcel): BaseWebserviceResponse {
            return BaseWebserviceResponse(parcel)
        }

        override fun newArray(size: Int): Array<BaseWebserviceResponse?> {
            return arrayOfNulls(size)
        }
    }


}