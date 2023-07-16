package com.production.vedantwatersupply.custome;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.production.vedantwatersupply.R;
import com.production.vedantwatersupply.utils.CenteredImageSpan;
/**
 * Created by hb on 29/10/17.
 */

public class CustomTextView extends AppCompatTextView {

    public CustomTextView(Context context) {
        super(context);
        init(context, null);
    }

    public CustomTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView);
//            int font = a.getInt(R.styleable.CustomTextView_customFont, 0);
            boolean asterisk = a.getBoolean(R.styleable.CustomEditText_astrisk, false);
            if (asterisk) {
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
//                if (getTypeface() == null) {
//                    setTypeface(ResourcesCompat.getFont(context, fontId));
//                } else {
//                    setTypeface(ResourcesCompat.getFont(context, fontId), getTypeface().getStyle());
//                }
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
            a.recycle();
        }
    }

    public void setCompulsoryAsterisk() {
        if (getText() != null) {
            setText(getText().toString(), true);
        }
    }

    public void setText(String text, boolean asterisk) {
        if (!asterisk) {
            setText(text);
            return;
        }
        String colored = "*";
        SpannableStringBuilder strBuilder = new SpannableStringBuilder();
        strBuilder.append(text);
        int start = strBuilder.length();
        strBuilder.append(colored);
        int end = strBuilder.length();
        strBuilder.setSpan(new ForegroundColorSpan(Color.RED), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        setText(strBuilder);
    }

    public void setText(String text, boolean asterisk, int iconResId) {
//        if (!asterisk) {
//            setText(text);
//            return;
//        }
        text = text.trim();
        SpannableStringBuilder strBuilder = new SpannableStringBuilder();
        strBuilder.append(text);

        if (asterisk) {
            String colored = "*";
            int start = strBuilder.length();
            strBuilder.append(colored);
            int end = strBuilder.length();
            strBuilder.setSpan(new ForegroundColorSpan(Color.RED), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        strBuilder.append(" ");

        CenteredImageSpan imageSpan = new CenteredImageSpan(getContext(), iconResId);
        strBuilder.setSpan(imageSpan, strBuilder.length() - 1, strBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        setText(strBuilder);
    }

    public String getTrimmedText() {
        return getText() == null ? "" : getText().toString().trim();
    }
}