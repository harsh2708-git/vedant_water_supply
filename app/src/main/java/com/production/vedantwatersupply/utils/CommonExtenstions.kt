package com.production.vedantwatersupply.utils

import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale

fun String.formatPriceWithoutDecimal(): String {
    return if (this.isNotEmpty()) {
        var formattedString: String = this
        if (this.contains(",")) {
            formattedString = formattedString.replace(",".toRegex(), "")
        }
        val doubleVal: Double = java.lang.Double.valueOf(formattedString)
        val formatter = NumberFormat.getInstance(Locale.US) as DecimalFormat
        formatter.applyPattern("#,###,###,###,###")
        formattedString = formatter.format(doubleVal)
        val separated = formattedString.split("\\.".toRegex()).toTypedArray()
        if (separated.size > 1 && separated[1].length == 1) {
            formattedString += "0"
        }

        /*if (!formattedString.contains(".")) {
                 formattedString += ".00";
             } else if (formattedString.endsWith(".")) {
                 formattedString += "00";
             }*/formattedString
    } else {
        "0"
    }
}

fun String.formatPriceWithDecimal(): String {
    return if (this.isNotEmpty()) {
        var formattedString: String = this
        if (this.contains(",")) {
            formattedString = formattedString.replace(",".toRegex(), "")
        }
        val doubleVal: Double = java.lang.Double.valueOf(formattedString)
        val formatter = NumberFormat.getInstance(Locale.US) as DecimalFormat
        formatter.applyPattern("#,###,###,###,###.##")
        formattedString = formatter.format(doubleVal)
        val separated = formattedString.split("\\.".toRegex()).toTypedArray()
        if (separated.size > 1 && separated[1].length == 1) {
            formattedString += "0"
        }

        if (!formattedString.contains(".")) {
            formattedString += ".00"
        } else if (formattedString.endsWith(".")) {
            formattedString += "00"
        }
        return formattedString
    } else {
        "0"
    }
}