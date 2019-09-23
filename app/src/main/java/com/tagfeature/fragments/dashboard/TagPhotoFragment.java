package com.tagfeature.fragments.dashboard;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
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

public class TagPhotoFragment extends Fragment implements SomeOneClickListener, View.OnClickListener {

    private static final int CHOOSE_A_PHOTO_TO_BE_TAGGED = 5000;

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

    public TagPhotoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tag_photo, container, false);

        mInstaTag = rootView.findViewById(R.id.insta_tag);
        mInstaTag.setImageToBeTaggedEvent(taggedImageEvent);

        final TextView cancelTextView = rootView.findViewById(R.id.cancel);
        final TagImageView doneImageView = rootView.findViewById(R.id.done);
        final TagImageView backImageView = rootView.findViewById(R.id.get_back);

        mRecyclerViewSomeOneToBeTagged = rootView.findViewById(R.id.rv_some_one_to_be_tagged);
        mTapPhotoToTagSomeOneTextView = rootView.findViewById(R.id.tap_photo_to_tag_someone);
        mHeaderSomeOneToBeTagged = rootView.findViewById(R.id.header_tag_photo);
        mHeaderSearchSomeOne = rootView.findViewById(R.id.header_search_someone);
        mEditSearchForSomeOne = rootView.findViewById(R.id.search_for_a_person);

        mEditSearchForSomeOne.addTextChangedListener(textWatcher);

        mTapPhotoToTagSomeOneTextView.setOnClickListener(this);
        cancelTextView.setOnClickListener(this);
        doneImageView.setOnClickListener(this);
        backImageView.setOnClickListener(this);

        mSomeOnes.addAll(SomeOneData.getDummySomeOneList());
        mSomeOneAdapter = new SomeOneAdapter(mSomeOnes, getActivity(),
                TagPhotoFragment.this);
        mRecyclerViewSomeOneToBeTagged.setAdapter(mSomeOneAdapter);
        mRecyclerViewSomeOneToBeTagged.setLayoutManager(new LinearLayoutManager(getActivity()));

        return rootView;
    }

    private void loadImage() {
        Glide
                .with(this)
                .load(mPhotoToBeTaggedUri)
                .apply(requestOptions)
                .into(mInstaTag.getTagImageView());
    }

    @Override
    public void onSomeOneClicked(final SomeOne someOne, int position) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CommonUtil.hideKeyboard(getActivity());
                mInstaTag.addTag(mAddTagInX, mAddTagInY, someOne.getUserName());
                mRecyclerViewSomeOneToBeTagged.setVisibility(View.GONE);
                //dialog();
                mTapPhotoToTagSomeOneTextView.setVisibility(View.VISIBLE);
                mHeaderSearchSomeOne.setVisibility(View.GONE);
                mHeaderSomeOneToBeTagged.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                CommonUtil.hideKeyboard(getActivity());
                mRecyclerViewSomeOneToBeTagged.scrollToPosition(0);
                mRecyclerViewSomeOneToBeTagged.setVisibility(View.GONE);
                mTapPhotoToTagSomeOneTextView.setVisibility(View.VISIBLE);
                mHeaderSearchSomeOne.setVisibility(View.GONE);
                mHeaderSomeOneToBeTagged.setVisibility(View.VISIBLE);
                break;
            case R.id.done:
                if (mPhotoToBeTaggedUri != null) {
                    if (mInstaTag.getListOfTagsToBeTagged().isEmpty()) {
                        Toast.makeText(getActivity(),
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
                        Toast.makeText(getActivity(),
                                "Photo tagged successfully", Toast.LENGTH_SHORT).show();
                        ((ViewPagerFragmentForDashBoard) getParentFragment()).setHomeAsSelectedTab();
                        reset();
                        LocalBroadcastManager.getInstance(getActivity())
                                .sendBroadcast(new Intent(HomeFragment.NEW_PHOTO_IS_TAGGED));
                    }
                } else {
                    Toast.makeText(getActivity(),
                            "Please choose a photo", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.get_back:
                ((ViewPagerFragmentForDashBoard) getParentFragment()).setHomeAsSelectedTab();
                reset();
                break;
            case R.id.tap_photo_to_tag_someone:
                if (mPhotoToBeTaggedUri == null) {
                    Intent photoToBeTagged = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(photoToBeTagged, CHOOSE_A_PHOTO_TO_BE_TAGGED);
                }
                break;
        }
    }

    private final InstaTag.TaggedImageEvent taggedImageEvent = new InstaTag.TaggedImageEvent() {
        @Override
        public void singleTapConfirmedAndRootIsInTouch(final float x, final float y) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAddTagInX = x;
                    mAddTagInY = y;
                    mRecyclerViewSomeOneToBeTagged.setVisibility(View.VISIBLE);
                    mHeaderSomeOneToBeTagged.setVisibility(View.GONE);
                    mTapPhotoToTagSomeOneTextView.setVisibility(View.GONE);
                    mHeaderSearchSomeOne.setVisibility(View.VISIBLE);
                }
            });
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Toast.makeText(getContext(),"Double ttap from first",Toast.LENGTH_SHORT).show();
            return true;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            Toast.makeText(getContext(),"Double ttap from second",Toast.LENGTH_SHORT).show();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_A_PHOTO_TO_BE_TAGGED
                && resultCode == android.app.Activity.RESULT_OK
                && data != null) {
            mPhotoToBeTaggedUri = data.getData();
            loadImage();
            mTapPhotoToTagSomeOneTextView
                    .setText(getString(R.string.tap_photo_tag_someone_drag_to_move_or_tap_to_delete));
        }
    }

    private void reset() {
        mPhotoToBeTaggedUri = null;
        loadImage();
        mInstaTag.removeTags();
        mTapPhotoToTagSomeOneTextView.setText(getString(R.string.tap_here_to_choose_a_photo));
    }

    public void dialog() {
        final RecyclerView mRecyclerViewSomeOneToBeTagged;
        EditText mEditSearchForSomeOne;
        TextView cancel;
        View view = View.inflate(getContext(), R.layout.tag_dialog, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setView(view)
                .show();

        mRecyclerViewSomeOneToBeTagged = view.findViewById(R.id.rv_some_one_to_be_tagged);
        mRecyclerViewSomeOneToBeTagged.setAdapter(mSomeOneAdapter);
        mRecyclerViewSomeOneToBeTagged.setLayoutManager(new LinearLayoutManager(getActivity()));


        cancel = view.findViewById(R.id.cancel);
        mEditSearchForSomeOne = view.findViewById(R.id.search_for_a_person);
        mEditSearchForSomeOne.addTextChangedListener(textWatcher);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonUtil.hideKeyboard(getActivity());
                mRecyclerViewSomeOneToBeTagged.scrollToPosition(0);
                mTapPhotoToTagSomeOneTextView.setVisibility(View.VISIBLE);
                mHeaderSearchSomeOne.setVisibility(View.GONE);
                //mHeaderSomeOneToBeTagged.setVisibility(View.VISIBLE);
                alertDialog.dismiss();
            }
        });

    }
}
