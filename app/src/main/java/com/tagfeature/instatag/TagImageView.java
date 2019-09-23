package com.tagfeature.instatag;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

public class TagImageView extends AppCompatImageView {
    public TagImageView(Context context) {
        super(context);
    }

    public TagImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TagImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
    }
}