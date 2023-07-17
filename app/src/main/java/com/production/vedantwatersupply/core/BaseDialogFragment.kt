package com.production.vedantwatersupply.core

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.production.vedantwatersupply.R

open class BaseDialogFragment : DialogFragment() {
    var baseActivity: BaseActivity<*, *>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            setStyle(STYLE_NO_TITLE, R.style.AppDialogAbove)
//        } else {
//            setStyle(STYLE_NO_TITLE, R.style.Theme_Holo_Dialog_NoActionBar_MinWidth)
//        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        baseActivity = context as BaseActivity<*, *>
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}