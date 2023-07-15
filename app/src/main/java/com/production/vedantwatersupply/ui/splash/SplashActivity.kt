package com.production.vedantwatersupply.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import com.production.vedantwatersupply.R
import com.production.vedantwatersupply.core.BaseActivity
import com.production.vedantwatersupply.core.BaseViewModel
import com.production.vedantwatersupply.databinding.ActivitySplashBinding
import com.production.vedantwatersupply.ui.login.LoginActivity
import java.util.*

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity<ActivitySplashBinding, BaseViewModel>() {

    override val layoutId: Int
        get() = R.layout.activity_splash

    override fun getViewModel(): Class<BaseViewModel> = BaseViewModel::class.java

    override fun init() {

        val year = Calendar.getInstance().get(Calendar.YEAR)
        binding?.tvCopyRight?.text = getString(R.string.copyright_2023_vedant_water_supply_nall_rights_reserved, year.toString())

        binding?.clRoot?.postDelayed({
            navigateToNextScreen()
        }, 2000)
    }

    private fun navigateToNextScreen() {
        navigateActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    override fun initListener() {}

    override fun addObserver() {}
}