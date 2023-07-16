package com.transportermanger.util.filter

import android.view.View

interface IFilterItem {
    fun onFilterItemSelected(view: View?, pos: Int)
}