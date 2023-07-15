package com.production.vedantwatersupply.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.production.vedantwatersupply.R
import com.production.vedantwatersupply.core.BaseActivity
import com.production.vedantwatersupply.databinding.ActivityLoginBinding
import com.production.vedantwatersupply.model.LoginRequest

class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>(), View.OnClickListener {
    override val layoutId: Int
        get() = R.layout.activity_login

    override fun getViewModel(): Class<LoginViewModel> = LoginViewModel::class.java

    override fun init() {


    }

    override fun initListener() {
        binding?.btnLogin?.setOnClickListener(this)
    }

    override fun addObserver() {

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnLogin -> {
//                callLoginApi()
                navigateToMainActivity(this)
            }
        }
    }

    private fun callLoginApi() {
        val loginRequest = LoginRequest()
        loginRequest.email = binding?.etEmail?.text.toString()
        loginRequest.password = binding?.etPassword?.text.toString()
        if (viewModel?.isAllValid(this,loginRequest) == true){
            showProgress()
        }
    }

}