package com.production.vedantwatersupply.utils.filter

import com.google.gson.annotations.SerializedName

class FilterItem {
    @SerializedName("value")
    var displayValue: String = ""

    @SerializedName("id")
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