package com.production.vedantwatersupply.ui.dialog

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.production.vedantwatersupply.R
import com.production.vedantwatersupply.core.BaseDialogFragment
import com.production.vedantwatersupply.databinding.FragmentFilterDialogBinding

class FilterDialogFragment : BaseDialogFragment(), View.OnClickListener {

    private var binding: FragmentFilterDialogBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.DialogTheme)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFilterDialogBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
    }

    private fun initListener(){
        binding?.btnClose?.setOnClickListener(this)
        binding?.btnApply?.setOnClickListener(this)
        binding?.btnClear?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnClose -> dismiss()

            R.id.btnApply -> {

                dismiss()
            }

            R.id.btnClear -> {
                dismiss()
            }
        }
    }

}