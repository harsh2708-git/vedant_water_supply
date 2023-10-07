package com.production.vedantwatersupply.core

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleObserver

open class BaseViewModel(app: Application) : AndroidViewModel(app), LifecycleObserver {

//    val baseRepository = BaseRepository()
//    fun callMonthFilterApi() {
//        baseRepository.callMonthFilterApi()
//    }
}