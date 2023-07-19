package com.production.vedantwatersupply.custome;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class VWSSpinnerAdapter<T> extends ArrayAdapter<T> {
    private int selectedPosition = 0;

    public VWSSpinnerAdapter(@NonNull Context context, int resource, @NonNull List<T> objects) {
        super(context, resource, objects);
    }

    public VWSSpinnerAdapter(Context context, int resource, T[] objects) {
        super(context, resource, objects);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = super.getDropDownView(position, convertView, parent);
        if (position == selectedPosition) {
            view.setBackgroundColor(Color.parseColor("#E3E3E3"));
        } else {
            view.setBackgroundColor(Color.TRANSPARENT);
        }
        return view;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }
}