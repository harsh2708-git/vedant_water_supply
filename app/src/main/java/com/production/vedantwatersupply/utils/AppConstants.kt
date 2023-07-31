package com.production.vedantwatersupply.utils

object AppConstants {

    const val PREF_NAME = "Vedant Water Supply"

    interface SharePref {
        companion object {
            const val SP_EMAIL = "email"
            const val SP_PASSWORD = "password"
        }
    }

    interface Bundle {
        companion object {
            const val ARG_TRIP_ID = "tripID"
            const val ARG_IS_FOR_TRIP_UPDATE = "isForTripUpdate"
            const val ARG_MAINTENANCE_ID = "maintenanceId"
            const val ARG_IS_FOR_MAINTENANCE_UPDATE = "isForMaintenanceUpdate"
            const val ARG_DRIVER_ID = "driverId"
            const val ARG_IS_FOR_DRIVER_UPDATE = "isForDriverUpdate"
        }
    }

    interface Trip {
        companion object {
            const val OWN_TANKER = "ownTanker"
            const val OTHER_TANKER = "otherTanker"
            const val PERMANENT_DRIVER = "permanentDriver"
            const val OTHER_DRIVER = "otherDriver"

            const val OEN_TANKER_DISPLAY = "Own Tanker"
            const val OTHER_TANKER_DISPLAY = "Other Tanker"
            const val PERMANENT_DRIVER_DISPLAY = "Permanent Driver"
            const val OTHER_DRIVER_DISPLAY = "Other Driver"

        }
    }

    interface Filter {
        companion object{
            const val TRIP = "trip"
            const val MAINTAINANCE = "maintainance"
            const val DRIVERMAINTAINANCE = "driverMaintainance"
        }
    }

    const val CANCELLED = "cancelled"

}