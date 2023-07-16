package com.production.vedantwatersupply.utils

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.production.vedantwatersupply.R

object CommonUtils {

    fun showToast(context: Context?, message: String?, showNativeToast: Boolean = false) {
        if (showNativeToast) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        } else {
            showCustomToast(context, message)
        }
    }

    fun showCustomToast(context: Context?, message: String?) {

        var mToast: Toast? = null
        val view: View = (context as AppCompatActivity).layoutInflater.inflate(R.layout.toast, null)

        mToast?.cancel()
        mToast = Toast(context)
        val toastTextView = view.findViewById(R.id.tvMessage) as TextView
//        val fvClose = view.findViewById(R.id.fvCloseToast) as AppCompatTextView
        toastTextView.text = message

        /*fvClose.setOnClickListener {
            mToast.cancel()
        }*/

        mToast.view = view
        mToast.setGravity(Gravity.TOP or Gravity.FILL_HORIZONTAL, 0, 0)
        mToast.duration = Toast.LENGTH_SHORT
        mToast.show()
    }


    /**
     * Get Numeric value from a formatted number string
     *
     * @param doubleString number string
     * @return respective no. 0.0 if string is not a valid number.
     */
     fun getNumeric(doubleString: String): Double {
        var doubleString = doubleString
        return try {
            if (doubleString.contains(",")) {
                doubleString = doubleString.replace(",", "")
            }
            doubleString.toDouble()
        } catch (e: Exception) {
            0.0
        }
    }
}