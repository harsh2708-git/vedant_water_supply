package com.production.vedantwatersupply.utils.filter;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int space;
    private int startSpace;
    private int endSpace;

    public SpaceItemDecoration(int startSpace, int space, int endSpace) {
        this.startSpace = startSpace;
        this.space = space;
        this.endSpace = endSpace;
    }

    @Override
    public void getItemOffsets(@NonNull @NotNull Rect outRect, @NonNull @NotNull View view, @NonNull @NotNull RecyclerView parent,
                               @NonNull @NotNull RecyclerView.State state) {
        int pos = parent.getChildAdapterPosition(view);
        int lastPos = 0;
        if (parent.getAdapter() != null) {
            lastPos = parent.getAdapter().getItemCount() - 1;
        }

        if (pos == 0) {
            outRect.left = startSpace;
            outRect.right = space / 2;
        } else if (pos == lastPos) {
            outRect.left = space / 2;
            outRect.right = endSpace;

        } else {
            outRect.left = space / 2;
            outRect.right = space / 2;
        }
    }
}
