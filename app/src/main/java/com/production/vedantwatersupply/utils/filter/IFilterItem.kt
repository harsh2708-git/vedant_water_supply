package com.production.vedantwatersupply.utils.filter

import android.view.View

interface IFilterItem {
    fun onFilterItemSelected(view: View?, pos: Int)
}