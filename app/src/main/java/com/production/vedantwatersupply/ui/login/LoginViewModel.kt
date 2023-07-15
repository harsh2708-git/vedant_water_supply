package com.production.vedantwatersupply.ui.login

import android.app.Application
import android.content.Context
import com.production.vedantwatersupply.R
import com.production.vedantwatersupply.core.BaseViewModel
import com.production.vedantwatersupply.model.LoginRequest
import com.production.vedantwatersupply.utils.CommonUtils
import com.production.vedantwatersupply.utils.ValidationUtils
import com.production.vedantwatersupply.utils.ValidationUtils.isValidEmail

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

}