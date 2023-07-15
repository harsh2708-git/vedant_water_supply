package com.production.vedantwatersupply.ui

import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.production.vedantwatersupply.R
import com.production.vedantwatersupply.core.BaseActivity
import com.production.vedantwatersupply.core.BaseViewModel
import com.production.vedantwatersupply.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding, BaseViewModel>() {

    private var navController: NavController? = null

    override val layoutId: Int
        get() = R.layout.activity_main

    override fun getViewModel(): Class<BaseViewModel> = BaseViewModel::class.java

    override fun init() {
        navController = getNavController()
    }

    fun getNavController(): NavController {
        val navHost = supportFragmentManager.findFragmentById(R.id.mainFragment) as NavHostFragment
        return navHost.navController
    }

    override fun initListener() {}

    override fun addObserver() {}
}