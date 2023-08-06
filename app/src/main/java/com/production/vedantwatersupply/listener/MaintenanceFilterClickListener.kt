package com.production.vedantwatersupply.listener

interface MaintenanceFilterClickListener {

    fun onApply(
        fromDate: String, displayFromDate: String?,
        toDate: String, displayToDate: String?,
        selectedYear: String, selectedTankerType: String,
        selectedTankerNo: String, selectedPaymentMode: String, selectedAddedBy: String, selectedMaintenanceDoneBy: String
    )

    fun onClear()
}