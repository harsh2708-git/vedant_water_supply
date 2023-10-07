package com.production.vedantwatersupply.core

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.production.vedantwatersupply.databinding.LayoutNoDataFoundBinding

abstract class BaseFragment<VD : ViewDataBinding, VM : BaseViewModel> : Fragment() {

    var mRootView: View? = null
    var isFirstTimeLoad = false
    var binding: VD? = null
    var viewModel: VM? = null
    protected var baseActivity: BaseActivity<*, *>? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        baseActivity = context as BaseActivity<*, *>
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[getViewModel()]
        addObserver()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        isFirstTimeLoad = false
        if (mRootView == null) {
            isFirstTimeLoad = true
            binding = DataBindingUtil.inflate<VD>(inflater, layoutId, container, false)
            init()
            initListener()
            mRootView = binding?.root
        } else {
            container?.removeView(mRootView)
        }

        return mRootView
    }

    @get:LayoutRes
    abstract val layoutId: Int
    abstract fun getViewModel(): Class<VM>
    abstract fun init()
    abstract fun initListener()
    abstract fun addObserver()

    fun navigateFragment(view: View, resId: Int, bundle: Bundle = Bundle()) {
        baseActivity?.navigateFragment(view, resId, bundle)
    }

    open fun showProgress() {
        baseActivity?.showProgress()
    }

    open fun hideProgress() {
        baseActivity?.hideProgress()
    }


//    fun callMonthFilterApi(){
//        viewModel?.callMonthFilterApi()
//    }

}