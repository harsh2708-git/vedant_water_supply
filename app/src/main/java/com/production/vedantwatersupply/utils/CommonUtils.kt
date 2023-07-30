package com.production.vedantwatersupply.utils

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.google.gson.Gson
import com.production.vedantwatersupply.R
import com.production.vedantwatersupply.ui.dialog.AlertDialogFragment
import org.json.JSONException
import org.json.JSONObject
import java.text.DateFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


object CommonUtils {

    const val DISPLAY_DATE_FORMAT = "dd/MM/yyyy" // 02/12/1989
    const val DISPLAY_DATE_FORMAT_2 = "dd MMM, yyyy" // 02 July, 1989
    const val SERVER_DATE_FORMAT_2 = "yyyy/MM/dd"
    const val SERVER_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss" //2018-05-18 13:53:17
    const val EMPTY_DATE = "0000-00-00 00:00:00"
    const val SERVER_DATE_FORMAT_D = "yyyy-MM-dd"


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

    fun <T> toFieldStringMap(model: T): LinkedHashMap<String, Any> {
        val param: LinkedHashMap<String, Any> = LinkedHashMap<String, Any>()
        val gson = Gson()
        val json = gson.toJson(model)
        var jsonObject: JSONObject? = null
        try {
            jsonObject = JSONObject(json)
            val iterator: Iterator<*> = jsonObject.keys()
            while (iterator.hasNext()) {
                val key = iterator.next() as String
                val value = jsonObject[key]
                if (value != null && value is String) {
                    param[key] = value
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return param
    }

//    fun getDateFromDisplay(displayDateStr: String?): Date? {
//        return try {
//            SimpleDateFormat(CommonUtils.DISPLAY_DATE_FORMAT, Locale.ENGLISH).parse(displayDateStr)
//        } catch (ex: java.lang.Exception) {
//            null
//        }
//    }

    fun getFormattedDateFromV2(calendar: Date?): String? {
        val format = SimpleDateFormat(CommonUtils.SERVER_DATE_FORMAT_D, Locale.ENGLISH)
        return format.format(calendar)
    }

    fun getFormattedDateFrom(calendar: Date?): String? {
        val format = SimpleDateFormat(DISPLAY_DATE_FORMAT, Locale.ENGLISH)
        return format.format(calendar)
    }

    fun isEmpty(textView: TextView): Boolean {
        return isEmpty(textView.text.toString().trim { it <= ' ' })
    }

    fun isEmpty(text: String?): Boolean {
        return text == null || TextUtils.isEmpty(text.trim { it <= ' ' })
    }

    fun showAlert(
        fragManager: FragmentManager?, ScreenIcon: Int, title: String?, message: String?, positiveText: String?, negativeText: String?, cancelable: Boolean,
        listener: AlertDialogFragment.IAlertDialogFragment?
    ) {
        val alertFrag: AlertDialogFragment = AlertDialogFragment.newInstance(ScreenIcon, title.toString(), message.toString(), positiveText.toString(), negativeText.toString(), cancelable, listener)
        fragManager?.let { alertFrag.show(it, AlertDialogFragment::class.java.simpleName) }
    }

    fun currentDate(): String {
        return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
    }

    fun currentMonth(): String {
        val dateFormat: DateFormat = SimpleDateFormat("MMMM")
        val date = Date()
        Log.d("Month", dateFormat.format(date))
        return dateFormat.format(date)
    }

    /**
     * Get Formatted number with comma in Locale.ENGLISH format
     *
     * @param number Number to be formatted
     * @return Formatted String
     */
    fun getNumberFormat(number: Double): String? {
        val myFormat = NumberFormat.getInstance(Locale.ENGLISH)
        myFormat.isGroupingUsed = true
        //        myFormat.setMaximumFractionDigits(0);
        return myFormat.format(number)
    }

    fun getNumberFormat(number: Double, fractionDigits: Int): String? {
        val myFormat = NumberFormat.getInstance(Locale.ENGLISH)
        myFormat.isGroupingUsed = true
        myFormat.maximumFractionDigits = fractionDigits
        return myFormat.format(number)
    }

    /**
     * Restricting given number up to 2 decimal.
     *
     * @param number number to be converted.
     * @return Formatted string
     */
    fun getCurrencyFormat(number: Double): String? {
        return getCurrencyFormat(number, 2)
    }

    /**
     * Restricting given number up to given digits.
     *
     * @param number number to be converted.
     * @param digits decimal points.
     * @return formatted string
     */
    fun getCurrencyFormat(number: Double, digits: Int): String? {
        val myFormat = NumberFormat.getInstance(Locale.ENGLISH)
        myFormat.isGroupingUsed = true
        myFormat.minimumFractionDigits = digits
        myFormat.maximumFractionDigits = digits
        return myFormat.format(number)
    }

    fun getNumberFormat(doubleString: String): String? {
        var doubleString = doubleString
        return try {
            if (doubleString.contains(",")) {
                doubleString = doubleString.replace(",", "")
            }
            getNumberFormat(doubleString.toDouble())
        } catch (e: java.lang.Exception) {
            doubleString
        }
    }

    /**
     * Restricting given number up to 2 decimal.
     *
     * @param doubleString number to be converted.
     * @return Formatted string
     */
    fun getCurrencyFormat(doubleString: String): String? {
        var doubleString = doubleString
        return try {
            if (doubleString.contains(",")) {
                doubleString = doubleString.replace(",", "")
            }
            if (isEmpty(doubleString)) {
                doubleString = "0"
            }
            getCurrencyFormat(doubleString.toDouble())
        } catch (e: java.lang.Exception) {
            doubleString
        }
    }

    fun getQuantityFormat(quantity: Double, productType: String?): String? {
        return getQuantityFormat(quantity.toString(), productType)
    }

    fun getQuantityFormat(quantity: String, productType: String?): String? {
        var quantity = quantity
        return if (isEmpty(productType) ) {
            getCurrencyFormat(quantity)
        } else {
            try {
                if (quantity.contains(",")) {
                    quantity = quantity.replace(",", "")
                }
                if (quantity.contains(".")) {
                    quantity = quantity.substring(0, quantity.indexOf("."))
                }
                val temp = quantity.toInt()
                val myFormat = NumberFormat.getInstance(Locale.ENGLISH)
                myFormat.isGroupingUsed = true
                myFormat.format(temp.toLong())
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                quantity
            }
        }
    }

    fun getCurrencyFormat(currencyCode: String?, doubleString: String): String? {
        return String.format(Locale.ENGLISH, "%s%s", currencyCode, getCurrencyFormat(doubleString))
    }

    fun getCurrencyFormat(currencyCode: String?, number: Double): String? {
        return String.format(Locale.ENGLISH, "%s%s", currencyCode, getCurrencyFormat(number))
    }

    /**
     * Trim comma of string string.
     *
     * @param string the string
     * @return the string
     */
    //Trims all the comma of the string and returns
    fun trimCommaOfString(string: String): String? {
//        String returnString;
        return if (string.contains(",")) {
            string.replace(",", "")
        } else {
            string
        }
    }

    fun showFormattedDate(textView: TextView, date: Date?) {
        val format = SimpleDateFormat(DISPLAY_DATE_FORMAT, Locale.ENGLISH)
        if (date == null) {
            textView.text = ""
        } else {
            textView.text = format.format(date)
        }
    }

    fun formatAPIDate(date: Date): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(date)
    }

    fun getServerDateFromDisplay(displayDateStr: String?): String? {
        var serverDateStr: String? = ""
        try {
            val displayDate = SimpleDateFormat(DISPLAY_DATE_FORMAT, Locale.ENGLISH).parse(displayDateStr)
            serverDateStr = SimpleDateFormat(SERVER_DATE_FORMAT_D, Locale.ENGLISH).format(displayDate)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return serverDateStr
    }

    fun getDateFromDisplay(displayDateStr: String): Date? {
        return try {
            SimpleDateFormat(DISPLAY_DATE_FORMAT, Locale.ENGLISH).parse(displayDateStr)
        } catch (ex: java.lang.Exception) {
            null
        }
    }

    fun getDisplayDateFromServer(serverDateStr: String): String? {
        return getDisplayDateFromServer(serverDateStr, "")
    }

    fun getDisplayDateFromServer(serverDateStr: String, defaultValue: String?): String? {
        if (!isEmpty(serverDateStr)) {
            return defaultValue
        }
        var displayDateStr: String? = ""
        try {
            val serverDate: Date
            serverDate = if (serverDateStr.length == 10) {
                SimpleDateFormat(CommonUtils.SERVER_DATE_FORMAT_D, Locale.ENGLISH).parse(serverDateStr)
            } else {
                SimpleDateFormat(CommonUtils.SERVER_DATE_FORMAT, Locale.ENGLISH).parse(serverDateStr)
            }
            displayDateStr = SimpleDateFormat(DISPLAY_DATE_FORMAT, Locale.ENGLISH).format(serverDate)
        } catch (ignored: java.lang.Exception) {
        }
        return displayDateStr
    }

    fun getSpannableText(firstPart: String, separator: String, secondPart: String, firstColor: Int, secondColor: Int): Spannable? {
        val temp = firstPart + separator
        val fullText = temp + secondPart
        val spannable: Spannable = SpannableString(fullText)
        spannable.setSpan(ForegroundColorSpan(firstColor), 0, temp.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(ForegroundColorSpan(secondColor), temp.length, fullText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spannable
    }

    fun getSpannableThreeText(firstPart: String, secondPart: String?, thirdPart: String?, firstColor: Int, secondColor: Int, thirdColor: Int): Spannable? {
        val firstString: Spannable = SpannableString(firstPart)
        firstString.setSpan(ForegroundColorSpan(firstColor), 0, firstPart.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        val secondString: Spannable = SpannableString(secondPart)
        secondString.setSpan(ForegroundColorSpan(secondColor), 0, secondString.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        val thirdString: Spannable = SpannableString(thirdPart)
        thirdString.setSpan(ForegroundColorSpan(thirdColor), 0, thirdString.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return SpannableString(TextUtils.concat(firstString, secondString, thirdString))
    }

    fun getCurrentYear(): Int {
        return Calendar.getInstance().get(Calendar.YEAR);
    }
}