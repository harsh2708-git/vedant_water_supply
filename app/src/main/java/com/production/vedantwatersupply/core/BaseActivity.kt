package com.production.vedantwatersupply.core

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDialog
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.production.vedantwatersupply.ui.MainActivity
import com.production.vedantwatersupply.R

abstract class BaseActivity<VD : ViewDataBinding, VM : BaseViewModel> : AppCompatActivity() {

    var binding: VD? = null
    var viewModel: VM? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<VD>(this, layoutId)
        setViewModel()
        init()
        initListener()
        addObserver()
    }

    @get:LayoutRes
    abstract val layoutId: Int
    private fun setViewModel() {
        viewModel = ViewModelProvider(this)[getViewModel()]
    }

    abstract fun getViewModel(): Class<VM>
    abstract fun init()
    abstract fun initListener()
    abstract fun addObserver()

    fun navigateActivity(intent: Intent?) {
        startActivity(intent)
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.animation_enter, R.anim.animation_exit)
    }

    private var progressDialog: AppCompatDialog? = null
    open fun showProgress() {
        hideProgress()
        progressDialog = AppCompatDialog(this)
        progressDialog?.setContentView(R.layout.progress_view)
        if (progressDialog?.window != null) {
            progressDialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        progressDialog?.setCancelable(false)
        if (!(isFinishing || isDestroyed)) progressDialog?.show()
    }

    open fun hideProgress() {
        if (progressDialog != null && progressDialog?.isShowing!! && !(isFinishing || isDestroyed)) {
            progressDialog?.dismiss()
        }
    }

    fun navigateToMainActivity(activity: Activity) {
        navigateActivity(Intent(activity, MainActivity::class.java))
        finish()
    }

    fun navigateFragment(view: View, resId: Int, bundle: Bundle = Bundle()) {
        view.findNavController().navigate(resId, bundle)
    }
}