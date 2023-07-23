package com.production.vedantwatersupply.listener

interface TripFilterClickListener {

    fun onApply(
        fromDate: String, displayFromDate: String?,
        toDate: String, displayToDate: String?,
        selectedYear: String, selectedTankerType: String,
        selectedTankerNo: String, selectedPaymentMode: String,
        selectedFuelFilledBy: String, selectedDriverType: String,
        selectedDriver: String, selectedWaterType: String,
        selectedAddedBy: String, selectedStatus: String, selectedFillingSite: String
    )

    fun onClear()
}