package com.tagfeature.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.tagfeature.InstaTagApplication;
import com.tagfeature.R;
import com.tagfeature.adapters.SomeOneAdapter;
import com.tagfeature.instatag.InstaTag;
import com.tagfeature.instatag.TagImageView;
import com.tagfeature.interfaces.SomeOneClickListener;
import com.tagfeature.models.SomeOne;
import com.tagfeature.models.TaggedPhoto;
import com.tagfeature.utilities.CommonUtil;
import com.tagfeature.utilities.SomeOneData;

import java.util.ArrayList;
import java.util.Calendar;

public class TagPhotoActivity extends AppCompatActivity implements SomeOneClickListener,
        View.OnClickListener {

    private InstaTag mInstaTag;
    private Uri mPhotoToBeTaggedUri;
    private RecyclerView mRecyclerViewSomeOneToBeTagged;
    private LinearLayout mHeaderSomeOneToBeTagged, mHeaderSearchSomeOne;
    private TextView mTapPhotoToTagSomeOneTextView;
    private float mAddTagInX, mAddTagInY;
    private EditText mEditSearchForSomeOne;
    private SomeOneAdapter mSomeOneAdapter;
    private final ArrayList<SomeOne> mSomeOnes = new ArrayList<>();
    private RequestOptions requestOptions =
            new RequestOptions()
                    .placeholder(0)
                    .fallback(0)
                    .centerCrop()
                    .skipMemoryCache(false)
                    .diskCacheStrategy(DiskCacheStrategy.ALL);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_photo);

        mPhotoToBeTaggedUri = getIntent().getData();

        mInstaTag = findViewById(R.id.insta_tag);
        mInstaTag.setImageToBeTaggedEvent(taggedImageEvent);

        final TextView cancelTextView = findViewById(R.id.cancel);
        final TagImageView doneImageView = findViewById(R.id.done);
        final TagImageView backImageView = findViewById(R.id.get_back);

        mRecyclerViewSomeOneToBeTagged = findViewById(R.id.rv_some_one_to_be_tagged);
        mTapPhotoToTagSomeOneTextView = findViewById(R.id.tap_photo_to_tag_someone);
        mHeaderSomeOneToBeTagged = findViewById(R.id.header_tag_photo);
        mHeaderSearchSomeOne = findViewById(R.id.header_search_someone);
        mEditSearchForSomeOne = findViewById(R.id.search_for_a_person);

        mEditSearchForSomeOne.addTextChangedListener(textWatcher);

        cancelTextView.setOnClickListener(this);
        doneImageView.setOnClickListener(this);
        backImageView.setOnClickListener(this);

        loadImage();

        mSomeOnes.addAll(SomeOneData.getDummySomeOneList());
        mSomeOneAdapter = new SomeOneAdapter(mSomeOnes, this, this);
        mRecyclerViewSomeOneToBeTagged.setAdapter(mSomeOneAdapter);
        mRecyclerViewSomeOneToBeTagged.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadImage() {
        Glide
                .with(this)
                .load(mPhotoToBeTaggedUri)
                .apply(requestOptions)
                .into(mInstaTag.getTagImageView());
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                CommonUtil.hideKeyboard(this);
                mRecyclerViewSomeOneToBeTagged.scrollToPosition(0);
                mRecyclerViewSomeOneToBeTagged.setVisibility(View.GONE);
                mTapPhotoToTagSomeOneTextView.setVisibility(View.VISIBLE);
                mHeaderSearchSomeOne.setVisibility(View.GONE);
                mHeaderSomeOneToBeTagged.setVisibility(View.VISIBLE);
                break;
            case R.id.done:
                if (mInstaTag.getListOfTagsToBeTagged().isEmpty()) {
                    Toast.makeText(this,
                            "Please tag at least one user", Toast.LENGTH_SHORT).show();
                } else {
                    ArrayList<TaggedPhoto> taggedPhotoArrayList = InstaTagApplication
                            .getInstance().getTaggedPhotos();
                    taggedPhotoArrayList.add(
                            new TaggedPhoto(
                                    Calendar.getInstance().getTimeInMillis() + "",
                                    mPhotoToBeTaggedUri.toString(),
                                    mInstaTag.getListOfTagsToBeTagged()));
                    InstaTagApplication.getInstance().setTaggedPhotos(taggedPhotoArrayList);
                    Toast.makeText(this,
                            "Photo tagged successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            case R.id.get_back:
                finish();
                break;
        }
    }

    private final InstaTag.TaggedImageEvent taggedImageEvent = new InstaTag.TaggedImageEvent() {
        @Override
        public void singleTapConfirmedAndRootIsInTouch(final float x, final float y) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {


                }
            });
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }
    };

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (mEditSearchForSomeOne.getText().toString().trim().equals("")) {
                mSomeOnes.clear();
                mSomeOnes.addAll(SomeOneData.getDummySomeOneList());
                mSomeOneAdapter.notifyDataSetChanged();
            } else {
                mSomeOnes.clear();
                mSomeOnes.addAll(SomeOneData.
                        getFilteredUser(mEditSearchForSomeOne.getText().toString().trim()));
                mSomeOneAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    @Override
    public void onSomeOneClicked(final SomeOne someOne, int position) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CommonUtil.hideKeyboard(TagPhotoActivity.this);
                mInstaTag.addTag(mAddTagInX, mAddTagInY, someOne.getUserName());
                mRecyclerViewSomeOneToBeTagged.setVisibility(View.GONE);
                mTapPhotoToTagSomeOneTextView.setVisibility(View.VISIBLE);
                mHeaderSearchSomeOne.setVisibility(View.GONE);
                mHeaderSomeOneToBeTagged.setVisibility(View.VISIBLE);
            }
        });
    }
}
