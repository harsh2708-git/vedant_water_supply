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
import com.production.vedantwatersupply.R
import com.production.vedantwatersupply.ui.MainActivity
import com.production.vedantwatersupply.utils.calendar.CaldroidFragment
import com.production.vedantwatersupply.utils.calendar.CaldroidListener
import java.util.Calendar
import java.util.Date

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

    private var caldroidFragment: CaldroidFragment? = null
    open fun setNormalCalender(fragmentListener: CaldroidListener?, selectedDate: Date?, minSelectedDate: Date?, maxSelectedDate: Date?) {
        fragmentListener?.let { setNormalCalender(it, selectedDate, minSelectedDate, maxSelectedDate, false) }
    }

    open fun setNormalCalender(
        fragmentListener: CaldroidListener, selectedDate: Date?, minSelectedDate: Date?, maxSelectedDate: Date?,
        showTimePicker: Boolean
    ) {
        val fragment = supportFragmentManager.findFragmentByTag("Calendar")
        if (fragment != null) {
            return
        }
        caldroidFragment = CaldroidFragment()
        val args = Bundle()

        args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true)
        args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, false)
        val currentDate = Date()
        caldroidFragment?.setMaxDate(maxSelectedDate)
        caldroidFragment?.setMinDate(minSelectedDate)
        caldroidFragment?.setArguments(args)
        if (selectedDate != null) {
            caldroidFragment?.setSelectedDates(selectedDate, selectedDate)
            caldroidFragment?.setBackgroundResourceForDate(R.color.yellow, selectedDate)
            caldroidFragment?.setTextColorForDate(R.color.white, selectedDate)
            caldroidFragment?.setSelectedTime(selectedDate.hours, selectedDate.minutes)
        } else {
            caldroidFragment?.setSelectedDates(currentDate, currentDate)
            caldroidFragment?.setSelectedTime(currentDate.hours, currentDate.minutes)
        }
        caldroidFragment?.show(supportFragmentManager, "Calendar")
        val calendar = Calendar.getInstance()
        calendar[Calendar.DAY_OF_MONTH] = 28
        val listener: CaldroidListener = object : CaldroidListener() {
            override fun onSelectDate(date: Date?, view: View?) {
                if (!showTimePicker) {
                    fragmentListener.onSelectDate(date, view)
                    caldroidFragment?.dismiss()
                } else {
                    caldroidFragment?.refreshView()
                }
            }

            override fun onChangeMonth(month: Int, year: Int) {}
            override fun onLongClickDate(date: Date?, view: View?) {}
            override fun onCaldroidViewCreated() {
                if (showTimePicker) {
                    caldroidFragment?.showTimePicker()
                } else {
                    caldroidFragment?.hideTimePicker()
                }
            }

            override fun onOkClicked(date: Date?) {
                super.onOkClicked(date)
                fragmentListener.onSelectDate(date, null)
                caldroidFragment?.dismiss()
            }
        }
        // Setup Caldroid
        caldroidFragment?.setCaldroidListener(listener)
    }
}