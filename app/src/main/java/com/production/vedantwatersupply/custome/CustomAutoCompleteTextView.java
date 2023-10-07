package com.production.vedantwatersupply.custome;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;

import com.production.vedantwatersupply.R;


/**
 * Created by hb on 29/10/17.
 */

public class CustomAutoCompleteTextView extends AppCompatAutoCompleteTextView {

    private ICustomAutoCompleteTextView listener;

    public CustomAutoCompleteTextView(Context context) {
        super(context);
        init(context, null);
    }

    public CustomAutoCompleteTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomAutoCompleteTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomAutoCompleteTextView);
//            int font = a.getInt(R.styleable.CustomEditText_customFont, 0);
            setUpClearButton();
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
//            setTypeface(ResourcesCompat.getFont(context, fontId));
            a.recycle();
        }
    }

    public String getTrimmedText() {
        return getText().toString().trim();
    }

    public boolean isEmpty() {
        return TextUtils.isEmpty(getTrimmedText());
    }


    private void setUpClearButton() {
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean clearIcon = s.length() > 0;
                if (clearIcon && isEnabled()) {
                    setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.btn_clear, 0);
                } else {
                    setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
            }
        });
        setOnTouchListener(new OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (getCompoundDrawables()[2] != null) {
                        if (event.getRawX() >= (CustomAutoCompleteTextView.this.getRight() - CustomAutoCompleteTextView.this.getCompoundPaddingRight())) {
                            setText("");
                            if (listener!=null){
                                listener.onClearButtonClicked();
                            }
                            return true;
                        }
                    }
                }
                return false;
            }
        });
    }

    public void setListener(ICustomAutoCompleteTextView listener) {
        this.listener = listener;
    }

    public interface ICustomAutoCompleteTextView{
        void onClearButtonClicked();
    }
}