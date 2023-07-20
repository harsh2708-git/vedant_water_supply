package com.production.vedantwatersupply.utils.calendar;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.production.vedantwatersupply.R;

import java.util.List;

/**
 * Customize the weekday gridview
 */
public class WeekdayArrayAdapter extends ArrayAdapter<String> {
    public static int textColor = Color.LTGRAY;
    private final LayoutInflater inflator;

    public WeekdayArrayAdapter(Context context, int textViewResourceId,
                               List<String> objects) {
        super(context, textViewResourceId, objects);
        inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // To prevent cell highlighted when clicked
    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    // Set color to gray and text size to 12sp
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // To customize text size and color

        TextView textView = (TextView) inflator.inflate(R.layout.calender_view_item, parent, false);
        textView.setText(String.valueOf(getItem(position)));
		/*TextView textView = (TextView) super.getView(position, convertView,
				parent);

		// Set content
		String item = getItem(position);

		// Show smaller text if the size of the text is 4 or more in some
		// locale
		if (item.length() <= 3) {
			textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
		} else {
			textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);
		}
		*//*textView.setTypeface(FontManager.getInstance(textView.getContext())
				.loadFont(0));*//*
		textView.setTextColor(textColor);
		textView.setSingleLine(true);
		textView.setEllipsize(TruncateAt.MARQUEE);
		textView.setMarqueeRepeatLimit(-1);
		textView.setSelected(true);
		textView.setGravity(Gravity.CENTER);*/
        return textView;
    }
}
