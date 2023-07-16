package com.transportermanger.util.filter

import com.google.gson.annotations.SerializedName

class FilterItem {
    @SerializedName("fur_display_status", alternate = ["dr_display_status", "display_status", "lat_display_status", "CustomerCompanyName", "val"])
    var displayValue: String = ""

    @SerializedName("fur_status", alternate = ["dr_status", "status", "lat_status", "customerId", "id"])
    var dbValue: String = ""
    var isSelected: Boolean = false

    constructor()

    constructor(dbValue: String, displayValue: String) {
        this.dbValue = dbValue
        this.displayValue = displayValue
    }

    constructor(dbValue: String, displayValue: String, isSelected: Boolean) {
        this.dbValue = dbValue
        this.displayValue = displayValue
        this.isSelected = isSelected
    }


    override fun toString(): String {
        return displayValue
    }


}