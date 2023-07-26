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
            const val OWN_TANKER = "Own Tanker"
            const val OTHER_TANKER = "Other Tanker"
            const val PERMANENT_DRIVER = "Parmanent Driver"
            const val OTHER_DRIVER = "Other Driver"
        }
    }

}