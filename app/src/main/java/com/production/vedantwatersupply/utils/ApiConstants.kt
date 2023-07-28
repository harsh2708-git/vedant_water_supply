package com.production.vedantwatersupply.utils

interface ApiConstants {

    companion object{
        const val BASE_URL = "http://149.100.158.78/"

        const val API_LOGIN = "login"
        const val API_DASHBOARD = "dashboard"
        const val API_MONTH_LIST = "dashboard/monthList"
        const val API_FILTERS = "filters"

        interface Trip{
            companion object{
                const val API_ADD_UPDATE_TRIP = "trip/addUpdateTrip"
                const val API_GET_ALL_TRIP = "trip/getAllTrip"
                const val API_TRIP_DETAIL = "trip/tripDetail"
                const val API_DELETE_TRIP = "trip/deleteTrip"
                const val API_CANCEL_TRIP = "trip/cancelTrip"
            }
        }
    }
}