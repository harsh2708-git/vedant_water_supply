package com.production.vedantwatersupply.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.production.vedantwatersupply.R
import com.production.vedantwatersupply.core.BaseDialogFragment
import com.production.vedantwatersupply.databinding.FragmentAlertBinding
import com.production.vedantwatersupply.utils.CommonUtils.isEmpty

class AlertDialogFragment : BaseDialogFragment(), View.OnClickListener {
    interface IAlertDialogFragment {
        fun onPositiveButtonClicked()
        fun onNegativeButtonClicked()
    }

    private var binding: FragmentAlertBinding? = null
    private var title = ""
    private var message = ""
    private var screenIcon = 0
    private var positiveText = ""
    private var negativeText = ""
    private var cancelable = false
    private var listener: IAlertDialogFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.DialogTheme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_alert, container, false)
        this.isCancelable = cancelable
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.positiveBtn?.setOnClickListener(this)
        binding?.negativeBtn?.setOnClickListener(this)
        binding?.btnClose?.setOnClickListener(this)
        binding?.tvTitle?.text = title
        binding?.tvMessageLabel?.text = message
        binding?.icon?.setImageResource(screenIcon)
        val positiveButtonTitle = if (isEmpty(positiveText)) getString(android.R.string.ok) else positiveText
        binding?.positiveBtn?.text = positiveButtonTitle
        binding?.negativeBtn?.text = negativeText
        if (isEmpty(title)) {
            binding?.tvTitle?.visibility = View.GONE
        }
        if (isEmpty(negativeText)) {
            binding?.negativeBtn?.visibility = View.GONE
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.positiveBtn -> {
                listener?.onPositiveButtonClicked()
                dismiss()
            }

            R.id.btnClose -> dismiss()
            R.id.negativeBtn -> {
                listener?.onNegativeButtonClicked()
                dismiss()
            }
        }
    }

    companion object {
        fun newInstance(screenIcon: Int, title: String, message: String, positiveText: String, negativeText: String, cancelable: Boolean, listener: IAlertDialogFragment?): AlertDialogFragment {
            val alertDialogFragment = AlertDialogFragment()
            alertDialogFragment.title = title
            alertDialogFragment.message = message
            alertDialogFragment.screenIcon = screenIcon
            alertDialogFragment.positiveText = positiveText
            alertDialogFragment.negativeText = negativeText
            alertDialogFragment.cancelable = cancelable
            alertDialogFragment.listener = listener
            return alertDialogFragment
        }
    }
}