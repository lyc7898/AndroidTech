package com.android.androidtech.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.android.androidtech.R;

/**
 * Created by yuchengluo on 2016/3/17.
 */
public class TopBar extends LinearLayout{
    public TopBar(Context context) {
        this(context, null);
    }

    public TopBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        InitView(context);
    }

    public TopBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        InitView(context);
    }

    public TopBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        InitView(context);
    }
    private void InitView(Context ctx){
        View view = LayoutInflater.from(ctx).inflate(R.layout.common_top_bar, this, true);
    }

    @Override
    public boolean isInEditMode() {
        return super.isInEditMode();
    }
}
