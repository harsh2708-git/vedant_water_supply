package com.production.vedantwatersupply.utils.calendar;

import android.content.Context;
import android.util.AttributeSet;
import com.production.vedantwatersupply.custome.CustomTextView;

public class SquareTextView extends CustomTextView {

    public SquareTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, w, oldw, oldh);
    }
}
