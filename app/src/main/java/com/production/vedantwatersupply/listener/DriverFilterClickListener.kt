package com.production.vedantwatersupply.listener

interface DriverFilterClickListener {

    fun onApply(
        fromDate: String, displayFromDate: String?,
        toDate: String, displayToDate: String?,
        selectedYear: String, selectedDriverType: String,
        selectedDriver: String, selectedPaymentMode: String, selectedAddedBy: String
    )

    fun onClear()
}