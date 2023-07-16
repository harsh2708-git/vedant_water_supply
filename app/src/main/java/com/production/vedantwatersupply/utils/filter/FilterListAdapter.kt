package com.production.vedantwatersupply.utils.filter

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.production.vedantwatersupply.R
import com.production.vedantwatersupply.databinding.ListItemFilterBinding
import com.transportermanger.util.filter.FilterItem
import com.transportermanger.util.filter.IFilterItem

class FilterListAdapter(var filterItems: List<FilterItem>, var callback: IFilterItem) :
    RecyclerView.Adapter<FilterListAdapter.LabelHolder>() {

    private var selectedItemPos: Int = -1
    var font: Int = R.font.montserrat_semibold
    var fontSizeResId: Int = R.dimen._10ssp
    private var isClickable = true

    constructor(filterItems: List<FilterItem>, isClickable: Boolean, callback: IFilterItem) : this(filterItems, callback) {
        this.filterItems = filterItems
        this.isClickable = isClickable
        this.callback = callback
        this.selectedItemPos = -1
        this.font = R.font.montserrat_semibold
        this.fontSizeResId = R.dimen._10ssp
        selectedItemPos = filterItems.indexOfFirst { it.isSelected }
    }

    init {
        selectedItemPos = filterItems.indexOfFirst { it.isSelected }
    }

    inner class LabelHolder(val binding: ListItemFilterBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            binding.txtValue.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            if (isClickable) {
                if (selectedItemPos == adapterPosition)
                    return
                if (selectedItemPos > -1) {
                    filterItems[selectedItemPos].isSelected = false
                    notifyItemChanged(selectedItemPos)
                }
                selectedItemPos = adapterPosition
                filterItems[selectedItemPos].isSelected = true
                notifyItemChanged(selectedItemPos)
                callback.onFilterItemSelected(p0, selectedItemPos)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LabelHolder {
        val binding: ListItemFilterBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_item_filter,
            parent,
            false
        )
        if (font != R.font.montserrat_semibold) {
            binding.txtValue.typeface = ResourcesCompat.getFont(parent.context, font)
        }
        if (fontSizeResId != R.dimen._10ssp) {
            binding.txtValue.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                parent.context.resources.getDimension(fontSizeResId)
            )
        }
        return LabelHolder(binding)
    }

    override fun onBindViewHolder(holder: LabelHolder, position: Int) {
        val item = filterItems[position]
        holder.binding.item = item
        holder.binding.txtValue.isSelected = item.isSelected
    }

    override fun getItemCount(): Int {
        return filterItems.size
    }

    fun getItem(pos: Int) = filterItems[pos]

    fun getSelectedItem() = filterItems.find { it.isSelected }
}