package com.production.vedantwatersupply.utils

import android.content.Context
import com.google.gson.Gson
import com.production.vedantwatersupply.model.response.LoginResponse
import com.production.vedantwatersupply.utils.AppConstants.PREF_NAME

/**
 * The type User utils.
 */
object UserUtils {
    private const val PREF_USER_DATA = "pref_user_data"
    private const val PREF_IS_USER_LOGIN = "pref_is_user_login"

    /**
     * To get user data if user logged-in
     *
     * @param context the context
     * @return UserData user data
     */
    fun getUserData(context: Context?): LoginResponse? {
        var userDataResponse: LoginResponse? = null
        try {
            val json = context?.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)?.getString(PREF_USER_DATA, "")
            userDataResponse = Gson().fromJson(json, LoginResponse::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return userDataResponse
    }

    /**
     * To set user data after getting from login
     *
     * @param context  the context
     * @param userDataResponse the user data
     */
    fun setUserData(context: Context?, userDataResponse: LoginResponse?) {
        try {
            val json = Gson().toJson(userDataResponse)
            context?.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)?.edit()?.putString(UserUtils.PREF_USER_DATA, json)?.apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Is user logged in boolean.
     *
     * @param context the context
     * @return the boolean
     */
    fun isUserLoggedIn(context: Context?): Boolean {
        return context?.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)?.getBoolean(PREF_IS_USER_LOGIN, false) == true
    }

    /**
     * Sets is user login.
     *
     * @param context the context
     */
    fun setIsUserLogin(context: Context?) {
        context?.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)?.edit()?.putBoolean(PREF_IS_USER_LOGIN, true)?.apply()
    }

    /**
     * Reset user data.
     *
     * @param context the context
     */
    fun resetUserData(context: Context?) {
        context?.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)?.edit()?.putBoolean(PREF_IS_USER_LOGIN, false)?.apply()
        context?.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)?.edit()?.putString(PREF_USER_DATA, "")?.apply()
    }

}