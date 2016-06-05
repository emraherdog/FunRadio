package com.ufxmeng.je.funradio.utils;

import android.graphics.Rect;
import android.support.v4.view.MarginLayoutParamsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by JE on 6/3/2016.
 */
public class RadioDecoration extends RecyclerView.ItemDecoration {

    private static final int MARGIN = 6;

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(MARGIN,MARGIN,MARGIN,MARGIN);
    }
}
