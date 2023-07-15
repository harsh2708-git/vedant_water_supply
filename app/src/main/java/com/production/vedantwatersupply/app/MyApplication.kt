package com.production.vedantwatersupply.app

import android.content.Context
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.multidex.MultiDexApplication
import com.production.vedantwatersupply.utils.AppLifecycleObserver

class MyApplication : MultiDexApplication() {


    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)

    }

    override fun onCreate() {
        super.onCreate()
        appLifecycleObserver = AppLifecycleObserver()
        ProcessLifecycleOwner.get().lifecycle.addObserver(appLifecycleObserver!!)
    }

    companion object {
        var appLifecycleObserver: AppLifecycleObserver? = null
    }
}