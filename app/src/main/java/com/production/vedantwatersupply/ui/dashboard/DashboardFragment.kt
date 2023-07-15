package com.production.vedantwatersupply.ui.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.production.vedantwatersupply.R
import com.production.vedantwatersupply.core.BaseFragment
import com.production.vedantwatersupply.databinding.FragmentDashboardBinding

class DashboardFragment : BaseFragment<FragmentDashboardBinding,DashboardViewModel>() {

    override val layoutId: Int
        get() = R.layout.fragment_dashboard

    override fun getViewModel(): Class<DashboardViewModel> = DashboardViewModel::class.java

    override fun init() {

    }

    override fun initListener() {

    }

    override fun addObserver() {

    }
}