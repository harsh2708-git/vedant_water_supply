package com.production.vedantwatersupply.utils

interface ApiConstants {

    companion object{
        const val BASE_URL = "http://149.100.158.78/"

        const val API_LOGIN = "login"
        const val API_DASHBOARD = "dashboard"

        interface Trip{
            companion object{
                const val API_ADD_UPDATE_TRIP = "trip/addUpdateTrip"
            }
        }
    }
}