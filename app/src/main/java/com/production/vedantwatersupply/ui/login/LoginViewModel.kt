package com.production.vedantwatersupply.ui.login

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonElement
import com.production.vedantwatersupply.BuildConfig
import com.production.vedantwatersupply.R
import com.production.vedantwatersupply.core.BaseViewModel
import com.production.vedantwatersupply.model.request.LoginRequest
import com.production.vedantwatersupply.model.response.LoginResponse
import com.production.vedantwatersupply.utils.CommonUtils
import com.production.vedantwatersupply.utils.ValidationUtils.isValidEmail
import com.production.vedantwatersupply.webservice.ApiClient
import com.production.vedantwatersupply.webservice.baseresponse.Generics
import com.production.vedantwatersupply.webservice.baseresponse.WSGenericResponse
import com.production.vedantwatersupply.webservice.baseresponse.WebServiceSetting
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class LoginViewModel(app: Application) : BaseViewModel(app) {

    fun isAllValid(context: Context, loginRequest: LoginRequest): Boolean {
        return if (loginRequest.email.isEmpty()) {
            CommonUtils.showToast(context, context.getString(R.string.please_enter_email_address))
            false
        } else if (!(isValidEmail(loginRequest.email))) {
            CommonUtils.showToast(context, context.getString(R.string.please_enter_valid_email_address))
            false
        } else if (loginRequest.password.isEmpty()) {
            CommonUtils.showToast(context, context.getString(R.string.please_enter_password))
            false
        } else {
            true
        }
    }

    val loginRepository = LoginRepository()

    fun callLoginApi(loginRequest: LoginRequest) {
        loginRepository.callLoginApi(loginRequest)
    }

}