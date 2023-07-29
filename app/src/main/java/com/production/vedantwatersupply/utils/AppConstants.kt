package com.production.vedantwatersupply.utils

object AppConstants {

    const val PREF_NAME = "Vedant Water Supply"

    interface SharePref {
        companion object {
            const val SP_EMAIL = "email"
            const val SP_PASSWORD = "password"
        }
    }

    interface Trip {
        companion object {
            const val OWN_TANKER = "ownTanker"
            const val OTHER_TANKER = "otherTanker"
            const val PERMANENT_DRIVER = "ownDriver"
            const val OTHER_DRIVER = "otherDriver"
        }
    }

    interface Bundle{
        companion object{
            const val ARG_TRIP_ID = "tripID"
            const val ARG_IS_FOR_TRIP_UPDATE = "isForTripUpdate"
        }
    }

}