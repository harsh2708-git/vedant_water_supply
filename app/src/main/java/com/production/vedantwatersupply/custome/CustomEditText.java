package com.production.vedantwatersupply.custome;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

import com.production.vedantwatersupply.R;


/**
 * Created by hb on 29/10/17.
 */

public class CustomEditText extends AppCompatEditText {

    public CustomEditText(Context context) {
        super(context);
        init(context, null);
    }

    public CustomEditText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomEditText);
//            int font = a.getInt(R.styleable.CustomEditText_customFont, 0);
            boolean astrisk = a.getBoolean(R.styleable.CustomEditText_astrisk, false);
            if (astrisk) {
                setCompulsoryAsterisk();
            }
//            int fontId = R.font.lato_regular;
//            switch (font) {
//                case 1:
//                    fontId = R.font.lato_light;
//                    break;
//                case 2:
//                    fontId = R.font.lato_regular;
//                    break;
//                case 3:
//                    fontId = R.font.lato_semibold;
//                    break;
//                case 4:
//                    fontId = R.font.lato_bold;
//                    break;
//            }
//            try {
//                setTypeface(ResourcesCompat.getFont(context, fontId));
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
            a.recycle();
        }
    }

    public void setCompulsoryAsterisk() {
        String colored = "*";
        SpannableStringBuilder strBuilder = new SpannableStringBuilder();
        strBuilder.append(getText());
        int start = strBuilder.length();
        strBuilder.append(colored);
        int end = strBuilder.length();
        strBuilder.setSpan(new ForegroundColorSpan(Color.RED), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        setText(strBuilder);
    }

    public String getTrimmedText() {
        return getText() == null ? "" : getText().toString().trim();
    }

    public boolean isEmpty() {
        return TextUtils.isEmpty(getTrimmedText());
    }
}