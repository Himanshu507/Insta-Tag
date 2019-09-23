package com.tagfeature.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


import com.tagfeature.InstaTagApplication;
import com.tagfeature.R;
import com.tagfeature.adapters.TaggedPhotoAdapter;
import com.tagfeature.interfaces.TaggedPhotoClickListener;
import com.tagfeature.models.TaggedPhoto;

import java.util.ArrayList;

public class TaggedPhotoActivity extends AppCompatActivity implements TaggedPhotoClickListener {

    private final ArrayList<Object> mObjectArrayList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tagged_photo);
        mObjectArrayList.addAll(InstaTagApplication.getInstance().getTaggedPhotos());
        RecyclerView mRecyclerViewTaggedPhotos = findViewById(R.id.rv_tagged_photos);
        TaggedPhotoAdapter taggedPhotoAdapter = new TaggedPhotoAdapter(mObjectArrayList,
                this, this);
        mRecyclerViewTaggedPhotos.setAdapter(taggedPhotoAdapter);
        mRecyclerViewTaggedPhotos.setLayoutManager(new LinearLayoutManager(this));
    }


    @Override
    public void onTaggedPhotoClick(TaggedPhoto taggedPhoto, int position) {

    }
}
