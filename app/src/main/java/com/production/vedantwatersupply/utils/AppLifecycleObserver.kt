package com.production.vedantwatersupply.utils

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class AppLifecycleObserver : LifecycleObserver {
    private var listener: LifeCycleCallBack? = null
    fun setListener(listener: LifeCycleCallBack?) {
        this.listener = listener
    }

    /**
     * Shows foreground [android.widget.Toast] after attempting to cancel the background one.
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onEnterForeground() {
        if (listener != null) {
            listener!!.onEnterForeground()
        }
    }

    /**
     * Shows background [android.widget.Toast] after attempting to cancel the foreground one.
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onEnterBackground() {
        if (listener != null) {
            listener!!.onEnterBackground()
        }
    }

    interface LifeCycleCallBack {
        fun onEnterForeground()
        fun onEnterBackground()
    }
}