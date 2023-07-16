package com.production.vedantwatersupply.utils.filter;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class VerticalItemDecorator extends RecyclerView.ItemDecoration {
    private int topSpace;
    private int midSpace;
    private int bottomSpace;

    public VerticalItemDecorator(int topSpace, int midSpace, int bottomSpace) {
        this.topSpace = topSpace;
        this.midSpace = midSpace;
        this.bottomSpace = bottomSpace;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int pos = parent.getChildAdapterPosition(view);
        int lastPos = 0;
        if (parent.getAdapter() != null) {
            lastPos = parent.getAdapter().getItemCount() - 1;
        }

        if (pos == 0) {
            outRect.top = topSpace;
            outRect.bottom = midSpace / 2;
        } else if (pos == lastPos) {
            outRect.bottom = bottomSpace;
            outRect.top = midSpace / 2;
        } else {
            outRect.top = midSpace / 2;
            outRect.bottom = midSpace / 2;
        }
    }
}
