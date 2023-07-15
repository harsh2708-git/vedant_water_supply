package com.production.vedantwatersupply.utils

import android.annotation.SuppressLint
import android.os.Build
import android.util.Patterns

object ValidationUtils {

    /**
     * Is valid email boolean.
     *
     * @param emailString the email string
     * @return the boolean
     */
    @SuppressLint("ObsoleteSdkInt")
    fun isValidEmail(emailString: String): Boolean {
        return if (Build.VERSION.SDK_INT < 8) {
            (emailString.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+".toRegex()) && emailString.isNotEmpty())
        } else {
            val inputStr: CharSequence = emailString
            val pattern = Patterns.EMAIL_ADDRESS
            val matcher = pattern.matcher(inputStr)
            matcher.matches()
        }
    }

}