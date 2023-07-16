package com.production.vedantwatersupply.custome

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class TruckRegNumberEditText : AppCompatEditText {

    private lateinit var alphabetRegex: Regex
    private lateinit var numberRegex: Regex
    private var lock: Boolean = false

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(p0: Editable?) {
            if (lock) {
                return
            }

            p0?.let {
                if (it.length > 1) {
                    val last = it.get(it.length - 1)
                    val secondLast = it.get(it.length - 2)
                    if ((alphabetRegex.matches(secondLast.toString()) && numberRegex.matches(last.toString())) || (alphabetRegex.matches(
                            last.toString()
                        ) && numberRegex.matches(secondLast.toString()))
                    ) {
                        lock = true
                        val temp = it.substring(0, it.length - 1).plus(" ").plus(last)
                        setText(temp)
                        setSelection(temp.length)
                        lock = false
                    }
                }
            }
        }

    }

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        val alphabets = "[A-Z]"
        val numeric = "[0-9]"
        alphabetRegex = Regex(alphabets)
        numberRegex = Regex(numeric)

        addTextChangedListener(textWatcher)
    }

    fun getTrimmedText(): String {
        return if (text == null) "" else text.toString().trim()
    }
}