package com.production.vedantwatersupply.core

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider

abstract class BaseActivity<VD : ViewDataBinding, VM : BaseViewModel> : AppCompatActivity() {

    var binding: VD? = null
    var viewModel: VM? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<VD>(this,layoutId)
        setViewModel()
        init()
        initListener()
        addObserver()
    }

    @get:LayoutRes
    abstract val layoutId: Int
    private fun setViewModel(){
        viewModel = ViewModelProvider(this)[getViewModel()]
    }

    abstract fun getViewModel(): Class<VM>
    abstract fun init()
    abstract fun initListener()
    abstract fun addObserver()

}