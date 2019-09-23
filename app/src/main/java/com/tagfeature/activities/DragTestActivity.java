package com.tagfeature.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import com.tagfeature.R;
import com.tagfeature.instatag.TagOnTouchListener;

public class DragTestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_test);
        ImageView mImageView = findViewById(R.id.image);
        mImageView.setOnTouchListener(new TagOnTouchListener(mImageView));
    }
}