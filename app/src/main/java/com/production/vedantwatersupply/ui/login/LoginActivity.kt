package com.production.vedantwatersupply.ui.login

import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.production.vedantwatersupply.R
import com.production.vedantwatersupply.core.BaseActivity
import com.production.vedantwatersupply.databinding.ActivityLoginBinding
import com.production.vedantwatersupply.model.request.LoginRequest
import com.production.vedantwatersupply.utils.AppConstants.SharePref.Companion.SP_EMAIL
import com.production.vedantwatersupply.utils.AppConstants.SharePref.Companion.SP_PASSWORD
import com.production.vedantwatersupply.utils.CommonUtils
import com.production.vedantwatersupply.utils.SharedPreferenceUtil
import com.production.vedantwatersupply.utils.UserUtils
import com.production.vedantwatersupply.webservice.baseresponse.WebServiceSetting

class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>(), View.OnClickListener {
    override val layoutId: Int
        get() = R.layout.activity_login

    override fun getViewModel(): Class<LoginViewModel> = LoginViewModel::class.java

    override fun init() {

        val email = SharedPreferenceUtil.getPreference(this, SP_EMAIL, "")
        val password = SharedPreferenceUtil.getPreference(this, SP_PASSWORD, "")

        if (email.isNotEmpty() && password.isNotEmpty()) {
            binding?.etEmail?.setText(email)
            binding?.etPassword?.setText(password)
        }
    }

    override fun initListener() {
        binding?.btnLogin?.setOnClickListener(this)
        binding?.etPassword?.setOnEditorActionListener { _: TextView?, actionId: Int, _: KeyEvent? ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                callLoginApi()
            }
            false
        }
    }

    override fun addObserver() {
        viewModel?.loginRepository?.loginResponseMutableLiveData?.observe(this) {
            hideProgress()
            when (it.webServiceSetting?.success) {
                WebServiceSetting.SUCCESS -> {
                    UserUtils.setUserData(this, it)
                    UserUtils.setIsUserLogin(this)

                    if (binding?.chkRememberMe?.isChecked == true) {
                        SharedPreferenceUtil.setPreference(this, SP_EMAIL, binding?.etEmail?.text?.toString())
                        SharedPreferenceUtil.setPreference(this, SP_PASSWORD, binding?.etPassword?.text?.toString())
                    } else {
                        SharedPreferenceUtil.setPreference(this, SP_EMAIL, "")
                        SharedPreferenceUtil.setPreference(this, SP_PASSWORD, "")
                    }
                    CommonUtils.showToast(this, it?.webServiceSetting?.message)

                    navigateToMainActivity(this)
                }

                WebServiceSetting.FAILURE -> {
                    CommonUtils.showToast(this, "Failed")
                }

                WebServiceSetting.NO_INTERNET -> {
                    CommonUtils.showToast(this, getString(R.string.no_internet_title))
                }
            }

        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnLogin -> /*callLoginApi()*/ navigateToMainActivity(this)
        }
    }

    private fun callLoginApi() {
        val loginRequest = LoginRequest()
        loginRequest.email = binding?.etEmail?.text.toString()
        loginRequest.password = binding?.etPassword?.text.toString()
        if (viewModel?.isAllValid(this, loginRequest) == true) {
            showProgress()
            viewModel?.callLoginApi(loginRequest)
        }
    }

}