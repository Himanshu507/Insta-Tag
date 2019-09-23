package com.tagfeature.instatag;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RotateDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tagfeature.R;
import com.tagfeature.models.TagsView;

import java.util.ArrayList;

public class InstaTag extends RelativeLayout {

    private int mPosX;
    private int mPosY;

    private float mRawX;
    private float mRawY;

    private int mRootWidth;
    private int mRootHeight;
    private final TagGestureListener mTagGestureListener = new TagGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (canWeAddTags) {
                if (mIsRootIsInTouch) {

                    float x = (e.getX() * 100) / mRootWidth;   //left
                    float y = (e.getY() * 100) / mRootHeight;  //top

                    mRawX = e.getRawX();
                    mRawY = e.getRawY();


                    Log.e("Step 1", "X - > " + x + " Y-> " + y);
                    switch (e.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                        case MotionEvent.ACTION_MOVE:
                        case MotionEvent.ACTION_UP:
                    }
                    if (mTaggedImageEvent != null) {
                        mTaggedImageEvent.singleTapConfirmedAndRootIsInTouch(x, y);
                    }
                } else {
                    hideRemoveButtonFromAllTagView();
                    mIsRootIsInTouch = true;
                }
            }
            return false;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (mTaggedImageEvent != null) {
                return mTaggedImageEvent.onDoubleTap(e);
            } else {
                return true;
            }
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            if (mTaggedImageEvent != null) {
                return mTaggedImageEvent.onDoubleTapEvent(e);
            } else {
                return true;
            }
        }

        @Override
        public void onLongPress(MotionEvent e) {
            if (mTaggedImageEvent != null) {
                mTaggedImageEvent.onLongPress(e);
            }
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return true;
        }
    };

    private Context mContext;

    private int mTagTextColor;
    private int mTagBackgroundColor;
    private int mCarrotTopBackGroundColor;
    private int mCarrotLeftBackGroundColor;
    private int mCarrotRightBackGroundColor;
    private int mCarrotBottomBackGroundColor;

    private Drawable mTagTextDrawable;
    private Drawable mCarrotTopDrawable;
    private Drawable mCarrotLeftDrawable;
    private Drawable mCarrotRightDrawable;
    private Drawable mCarrotBottomDrawable;

    private boolean tagsAreAdded;
    private boolean canWeAddTags;
    private boolean showAllCarrots;
    private boolean mIsRootIsInTouch = true;

    private Animation mShowAnimation;
    private Animation mHideAnimation;

    private GestureDetector mGestureDetector;

    private TaggedImageEvent mTaggedImageEvent;

    private ViewGroup mRoot;
    private ImageView mLikeImage;
    private TagImageView mTagImageView;
    private final ArrayList<TagsView> mTagList = new ArrayList<>();

    private final Runnable mSetRootHeightWidth = new Runnable() {
        @Override
        public void run() {
            mRootWidth = mRoot.getWidth();
            mRootHeight = mRoot.getHeight();
        }
    };

    private final OnTouchListener mTagOnTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return mGestureDetector.onTouchEvent(event);
        }
    };
    private boolean isDragging;

    public interface Constants {
        int DEFAULT_COLOR = 0xFF303F9F;
        int TAG_TEXT_COLOR = Color.WHITE;

        String CARROT_TOP = "CARROT_TOP";
        String CARROT_LEFT = "CARROT_LEFT";
        String CARROT_RIGHT = "CARROT_RIGHT";
        String CARROT_BOTTOM = "CARROT_BOTTOM";
    }

    private void initViewWithId(Context context, TypedArray obtainStyledAttributes) {
        mContext = context;

        LayoutInflater inflater =
                (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.tag_root, this);


        mRoot = findViewById(R.id.tag_root);
        mTagImageView = findViewById(R.id.tag_image_view);
        mLikeImage = new ImageView(context);

        int likeColor, likeSrc, likeSize;
        if (obtainStyledAttributes != null) {
            likeColor = obtainStyledAttributes.getColor(R.styleable.InstaTag_likeColor,
                    ContextCompat.getColor(context, R.color.colorAccent));
            likeSrc = obtainStyledAttributes.getResourceId(R.styleable.InstaTag_likeSrc,
                    R.drawable.ic_like);
            likeSize = obtainStyledAttributes
                    .getDimensionPixelSize(R.styleable.InstaTag_likeSize,
                            getResources().getDimensionPixelSize(R.dimen.dp256));
        } else {
            likeColor = ContextCompat.getColor(context, R.color.colorAccent);
            likeSrc = R.drawable.ic_like;
            likeSize = getResources().getDimensionPixelSize(R.dimen.dp256);
        }
        LayoutParams heartParams = new LayoutParams(likeSize, likeSize);
        heartParams.addRule(CENTER_IN_PARENT, TRUE);

        mLikeImage.setLayoutParams(heartParams);
        mLikeImage.setVisibility(GONE);
        mLikeImage.setImageResource(likeSrc);
        mLikeImage.setColorFilter(likeColor);

        setLayoutParamsToBeSetForRootLayout(mContext);
        mRoot.post(mSetRootHeightWidth);
        mRoot.setOnTouchListener(mTagOnTouchListener);
        mGestureDetector = new GestureDetector(mRoot.getContext(), mTagGestureListener);
    }

    public InstaTag(Context context) {
        super(context);
        initViewWithId(context, null);
    }

    public InstaTag(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            initView(attrs, context);
        } else {
            initView(attrs, context);
        }
    }

    private float getThumbCenterX(View mIndicatorView) {
        //the background track on the thumb start
        Rect rect = new Rect();
        mIndicatorView.getLocalVisibleRect(rect);
        return rect.right;
    }

    private void hideRemoveButtonFromAllTagView() {
        if (!mTagList.isEmpty()) {
            for (TagsView tagsView : mTagList) {
                tagsView.view.findViewById(R.id.remove_tag_image_view).setVisibility(View.GONE);
            }
        }
    }

    private void initView(AttributeSet attrs, Context context) {
        mContext = context;

        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attrs,
                R.styleable.InstaTag, 0, 0);

        mTagTextDrawable = obtainStyledAttributes.
                getDrawable(R.styleable.InstaTag_tagTextBackground);
        mCarrotTopDrawable = obtainStyledAttributes.
                getDrawable(R.styleable.InstaTag_carrotTopBackground);
        mCarrotLeftDrawable = obtainStyledAttributes.
                getDrawable(R.styleable.InstaTag_carrotLeftBackground);
        mCarrotRightDrawable = obtainStyledAttributes.
                getDrawable(R.styleable.InstaTag_carrotRightBackground);
        mCarrotBottomDrawable = obtainStyledAttributes.
                getDrawable(R.styleable.InstaTag_carrotBottomBackground);

        canWeAddTags = obtainStyledAttributes.
                getBoolean(R.styleable.InstaTag_canWeAddTags, false);
        showAllCarrots = obtainStyledAttributes.
                getBoolean(R.styleable.InstaTag_showAllCarrots, false);

        mTagTextColor = obtainStyledAttributes.
                getColor(R.styleable.InstaTag_instaTextColor, Constants.TAG_TEXT_COLOR);

        int overrideDefaultColor = obtainStyledAttributes.
                getColor(R.styleable.InstaTag_overrideDefaultColor, Constants.DEFAULT_COLOR);

        if (overrideDefaultColor == Constants.DEFAULT_COLOR) {
            mCarrotTopBackGroundColor = obtainStyledAttributes.
                    getColor(R.styleable.InstaTag_carrotTopColor, Constants.DEFAULT_COLOR);
            mCarrotLeftBackGroundColor = obtainStyledAttributes.
                    getColor(R.styleable.InstaTag_carrotLeftColor, Constants.DEFAULT_COLOR);
            mCarrotRightBackGroundColor = obtainStyledAttributes.
                    getColor(R.styleable.InstaTag_carrotRightColor, Constants.DEFAULT_COLOR);
            mCarrotBottomBackGroundColor = obtainStyledAttributes.
                    getColor(R.styleable.InstaTag_carrotBottomColor, Constants.DEFAULT_COLOR);
            mTagBackgroundColor = obtainStyledAttributes.
                    getColor(R.styleable.InstaTag_instaBackgroundColor, Constants.DEFAULT_COLOR);
        } else {
            mTagBackgroundColor = overrideDefaultColor;
            mCarrotTopBackGroundColor = overrideDefaultColor;
            mCarrotLeftBackGroundColor = overrideDefaultColor;
            mCarrotRightBackGroundColor = overrideDefaultColor;
            mCarrotBottomBackGroundColor = overrideDefaultColor;
        }

        mHideAnimation = AnimationUtils.loadAnimation(context, obtainStyledAttributes.
                getResourceId(R.styleable.InstaTag_hideAnimation, R.anim.zoom_out));

        mShowAnimation = AnimationUtils.loadAnimation(context, obtainStyledAttributes.
                getResourceId(R.styleable.InstaTag_showAnimation, R.anim.zoom_in));

        initViewWithId(context, obtainStyledAttributes);
        obtainStyledAttributes.recycle();
    }

    private boolean isInitialized = false;

    private void actionTagMove(View tagView, int pointerCount, int X, int Y) {
        int width = tagView.getWidth();
        int height = tagView.getHeight();
        int x = (int) tagView.getX();
        int y = (int) tagView.getY();

        if (x <= width && y <= height) {
            carrotType(Constants.CARROT_TOP, tagView, pointerCount, X, mPosX, Y, mPosY);
        } else if (x + width >= mRootWidth && y + height >= mRootHeight) {
            carrotType(Constants.CARROT_BOTTOM, tagView, pointerCount, X, mPosX, Y, mPosY);
        } else if (x - width <= 0 && y + height >= mRootHeight) {
            carrotType(Constants.CARROT_BOTTOM, tagView, pointerCount, X, mPosX, Y, mPosY);
        } else if (x + width >= mRootWidth && y <= height / 2) {
            carrotType(Constants.CARROT_TOP, tagView, pointerCount, X, mPosX, Y, mPosY);
        } else if (x <= 0 && y > height && y + height < mRootHeight) {
            carrotType(Constants.CARROT_LEFT, tagView, pointerCount, X, mPosX, Y, mPosY);
        } else if (x > width && x + width < mRootWidth && y - height <= 0) {
            carrotType(Constants.CARROT_TOP, tagView, pointerCount, X, mPosX, Y, mPosY);
        } else if (x + width >= mRootWidth && y > height && y + height < mRootHeight) {
            carrotType(Constants.CARROT_RIGHT, tagView, pointerCount, X, mPosX, Y, mPosY);
        } else if (x > width && x + width < mRootWidth && y + height >= mRootHeight) {
            carrotType(Constants.CARROT_BOTTOM, tagView, pointerCount, X, mPosX, Y, mPosY);
        } else {
            carrotType(Constants.CARROT_TOP, tagView, pointerCount, X, mPosX, Y, mPosY);
        }
    }

    private void carrotType(String carrotType,
                            View tagView,
                            int pointerCount, int X, int posX, int Y, int posY) {
        switch (carrotType) {
            case Constants.CARROT_TOP:
                setCarrotVisibility(tagView, Constants.CARROT_TOP);
                setLayoutParamsForTagView(Constants.CARROT_TOP,
                        pointerCount, X, posX, Y, posY, tagView);
                break;
            case Constants.CARROT_LEFT:
                setCarrotVisibility(tagView, Constants.CARROT_LEFT);
                setLayoutParamsForTagView(Constants.CARROT_LEFT,
                        pointerCount, X, posX, Y, posY, tagView);
                break;
            case Constants.CARROT_RIGHT:
                setCarrotVisibility(tagView, Constants.CARROT_RIGHT);
                setLayoutParamsForTagView(Constants.CARROT_RIGHT,
                        pointerCount, X, posX, Y, posY, tagView);
                break;
            case Constants.CARROT_BOTTOM:
                setCarrotVisibility(tagView, Constants.CARROT_BOTTOM);
                setLayoutParamsForTagView(Constants.CARROT_BOTTOM,
                        pointerCount, X, posX, Y, posY, tagView);
                break;
        }
    }

    private int width;
    private float maxLeft;

    private boolean tagNotTaggedYet(String tagName) {
        boolean tagFound = true;
        if (!mTagList.isEmpty()) {
            for (TagsView tagsView : mTagList) {
                if (((TextView) tagsView.view.
                        findViewById(R.id.tag_text_view)).
                        getText().toString().equals(tagName)) {
                    tagFound = false;
                    break;
                }
            }
        } else {
            tagFound = true;
        }
        return tagFound;
    }

    private float maxRight;
    private float dX;
    private int height;
    private float maxTop;
    private float maxBottom;
    private float dY;

    private void setCarrotVisibility(View tagView, String carrotType) {
        if (!showAllCarrots) {
            switch (carrotType) {
                case Constants.CARROT_TOP:
                    tagView.findViewById(R.id.indicator_arrow).setVisibility(View.VISIBLE);

                    tagView.findViewById(R.id.indicator_arrow_down).setVisibility(View.GONE);
                    //tagView.findViewById(R.id.carrot_right).setVisibility(View.INVISIBLE);
                    //tagView.findViewById(R.id.carrot_bottom).setVisibility(View.INVISIBLE);
                    break;
                case Constants.CARROT_LEFT:
                    tagView.findViewById(R.id.indicator_arrow).setVisibility(View.VISIBLE);

                    tagView.findViewById(R.id.indicator_arrow_down).setVisibility(View.GONE);
                    //tagView.findViewById(R.id.carrot_right).setVisibility(View.INVISIBLE);
                    //tagView.findViewById(R.id.carrot_bottom).setVisibility(View.INVISIBLE);
                    break;
                case Constants.CARROT_RIGHT:
                    tagView.findViewById(R.id.indicator_arrow).setVisibility(View.VISIBLE);

                    tagView.findViewById(R.id.indicator_arrow_down).setVisibility(View.GONE);
                    //tagView.findViewById(R.id.carrot_left).setVisibility(View.INVISIBLE);
                    //tagView.findViewById(R.id.carrot_bottom).setVisibility(View.INVISIBLE);
                    break;
                case Constants.CARROT_BOTTOM:
                    tagView.findViewById(R.id.indicator_arrow_down).setVisibility(View.VISIBLE);

                    tagView.findViewById(R.id.indicator_arrow).setVisibility(View.GONE);
                    //tagView.findViewById(R.id.carrot_left).setVisibility(View.INVISIBLE);
                    //tagView.findViewById(R.id.carrot_right).setVisibility(View.INVISIBLE);
                    break;
            }
        }
    }

    private void setLayoutParamsForTagView(String carrotType,
                                           int pointerCount, int X, int posX, int Y, int posY,
                                           View tagView) {
        int left = (X - posX);
        int top = (Y - posY);

        if (left < 0) {
            left = 0;
        } else if (left + tagView.getWidth() > mRootWidth) {
            left = mRootWidth - tagView.getWidth();
        }

        if (top < 0) {
            top = 0;
        } else if (top + tagView.getHeight() > mRootHeight) {
            top = mRootHeight - tagView.getHeight();
        }

        LayoutParams tagViewLayoutParams =
                (LayoutParams) tagView.getLayoutParams();
        if (pointerCount == 1) {
            switch (carrotType) {
                case Constants.CARROT_TOP:
                case Constants.CARROT_LEFT:
                case Constants.CARROT_RIGHT:
                case Constants.CARROT_BOTTOM:
                    tagViewLayoutParams.setMargins(left, top, 0, 0);
                    tagView.setLayoutParams(tagViewLayoutParams);
                    break;
            }
        }
    }

    private void setColorForTag(View tagView) {
        ((TextView) tagView.findViewById(R.id.tag_text_view)).setTextColor(mTagTextColor);

        if (mCarrotTopDrawable == null) {
            setColor((tagView.findViewById(R.id.indicator_arrow)).
                    getBackground(), mCarrotTopBackGroundColor);
        }
        if (mCarrotLeftDrawable == null) {
            setColor((tagView.findViewById(R.id.indicator_arrow)).
                    getBackground(), mCarrotLeftBackGroundColor);
        }
        if (mCarrotRightDrawable == null) {
            setColor((tagView.findViewById(R.id.indicator_arrow)).
                    getBackground(), mCarrotRightBackGroundColor);
        }
        if (mCarrotBottomDrawable == null) {
            setColor((tagView.findViewById(R.id.indicator_arrow_down)).
                    getBackground(), mCarrotBottomBackGroundColor);
        }

        if (mTagTextDrawable == null) {
            setColor((tagView.findViewById(R.id.indicator_container)).
                    getBackground(), mTagBackgroundColor);
        }
    }

    public void addTag(float x, float y, String tagText) {
        if (tagNotTaggedYet(tagText)) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);

            final View tagView = layoutInflater.
                    inflate(R.layout.view_for_tag, mRoot, false);
            final TextView tagTextView = tagView.findViewById(R.id.tag_text_view);
            final ArrowView arrowView = tagView.findViewById(R.id.indicator_arrow);
            final ImageView removeTagImageView =
                    tagView.findViewById(R.id.remove_tag_image_view);

            tagTextView.setText(tagText);
            tagView.measure(0, 0);
            setColorForTag(tagView);

            LayoutParams layoutParams =
                    new LayoutParams(
                            LayoutParams.WRAP_CONTENT,
                            LayoutParams.WRAP_CONTENT);

            int w = tagView.getMeasuredWidth() / 2;
            Log.e("Step 4", "W - > " + w);


            layoutParams.setMargins(-w,
                    0,
                    0,
                    0);
            tagView.setLayoutParams(layoutParams);

            Log.e("Step 4", "X - > " + x + " Y-> " + y);
            x = (x * mRootWidth) / 100;
            y = (y * mRootHeight) / 100;
            Log.e("Step 5", "X - > " + x + " Y-> " + y);

            tagView.setX(x);
            tagView.setY(y);


            final TagsView tagsView = new TagsView();
            tagsView.view = tagView;
            tagsView.width = w;
            tagsView.height = 0;

            mTagList.add(tagsView);
            mRoot.addView(tagView);

            removeTagImageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mTagList.remove(tagsView);
                    mRoot.removeView(tagView);
                }
            });

          /*  tagView.setOnTouchListener(new OnTouchListener() {
                public boolean onTouch(final View view, MotionEvent event) {
                    if (canWeAddTags) {
                        mIsRootIsInTouch = false;
                        float x = (view.getX() * 100) / mRootWidth;   //left
                        float y = (view.getY() * 100) / mRootHeight;  //top

                        Log.e("Step 1", "X - > " + x + " Y-> " + y);

                        int a = (int) event.getRawX();
                        int b = (int) event.getRawY();

                        int pointerCount = event.getPointerCount();

                        switch (event.getAction() & MotionEvent.ACTION_MASK) {
                            case MotionEvent.ACTION_DOWN:
                                LayoutParams layoutParams =
                                        (LayoutParams) view.getLayoutParams();

                                mPosX = (a - layoutParams.leftMargin);
                                mPosY = (b - layoutParams.topMargin);

                                removeTagImageView.setVisibility(View.VISIBLE);
                                break;
                            case MotionEvent.ACTION_UP:
                                break;
                            case MotionEvent.ACTION_POINTER_DOWN:
                                break;
                            case MotionEvent.ACTION_POINTER_UP:
                                break;
                            case MotionEvent.ACTION_MOVE:
                                 actionTagMove(tagView, pointerCount, a, b);
                                // updateStayIndicator(tagView,arrowView);
                                break;
                        }
                        mRoot.invalidate();
                    }
                    return true;
                }
            });*/

            tagView.setOnTouchListener(new OnTouchListener() {
                public boolean onTouch(final View v, MotionEvent event) {
                    if (isDragging) {
                        width = tagView.getMeasuredWidth();
                        height = tagView.getMeasuredHeight();

                        float[] bounds = new float[4];
                        // LEFT
                        bounds[0] = event.getRawX() + dX;
                        if (bounds[0] < maxLeft) {
                            Log.e("Check Left", bounds[0] + " < " + maxLeft);
                            tagView.findViewById(R.id.indicator_arrow).setVisibility(View.VISIBLE);
                            tagView.findViewById(R.id.indicator_arrow_down).setVisibility(View.GONE);

                            bounds[0] = maxLeft;
                        }
                        // RIGHT

                        bounds[2] = bounds[0] + width;
                        if (bounds[2] > maxRight) {
                            Log.e("Check Right", bounds[2] + " > " + maxRight);
                            tagView.findViewById(R.id.indicator_arrow).setVisibility(View.VISIBLE);
                            tagView.findViewById(R.id.indicator_arrow_down).setVisibility(View.GONE);
                            bounds[2] = maxRight;
                            bounds[0] = bounds[2] - width;
                        }
                        // TOP
                        bounds[1] = event.getRawY() + dY;
                        if (bounds[1] < maxTop) {
                            Log.e("Check Top", bounds[1] + " < " + maxTop);
                            tagView.findViewById(R.id.indicator_arrow).setVisibility(View.VISIBLE);
                            tagView.findViewById(R.id.indicator_arrow_down).setVisibility(View.GONE);
                            bounds[1] = maxTop;
                        }
                        // BOTTOM
                        bounds[3] = bounds[1] + height;
                        if (bounds[3] > maxBottom) {
                            Log.e("Check Bottom", bounds[3] + " > " + maxBottom);
                            tagView.findViewById(R.id.indicator_arrow).setVisibility(View.GONE);
                            tagView.findViewById(R.id.indicator_arrow_down).setVisibility(View.VISIBLE);

                            bounds[3] = maxBottom;
                            bounds[1] = bounds[3] - height;
                        } else {
                            tagView.findViewById(R.id.indicator_arrow).setVisibility(View.VISIBLE);
                            tagView.findViewById(R.id.indicator_arrow_down).setVisibility(View.GONE);
                        }

                        switch (event.getAction()) {
                            case MotionEvent.ACTION_CANCEL:
                            case MotionEvent.ACTION_UP:
                                onDragFinish();
                                break;
                            case MotionEvent.ACTION_MOVE:
                                tagView.animate().x(bounds[0]).y(bounds[1]).setDuration(0).start();
                                break;
                        }
                        return true;
                    } else {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                isDragging = true;
                                if (!isInitialized) {
                                    updateBounds();
                                }
                                dX = v.getX() - event.getRawX();
                                dY = v.getY() - event.getRawY();
                                return true;
                        }
                    }
                    return false;
                }
            });
        } else {
            Toast.makeText(mContext, "This user is already tagged", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateBounds() {
        updateViewBounds();
        updateParentBounds();
        isInitialized = true;
    }

    private void updateViewBounds() {
        dX = 0;

        dY = 0;
    }

    private void updateParentBounds() {
        maxLeft = 0;
        maxRight = maxLeft + mRootWidth;

        maxTop = 0;
        maxBottom = maxTop + mRootHeight;
    }

    private void onDragFinish() {
        dX = 0;
        dY = 0;
        isDragging = false;
    }

    private void setColor(Drawable drawable, int color) {
        if (drawable instanceof ShapeDrawable) {
            ((ShapeDrawable) drawable).getPaint().setColor(color);
        } else if (drawable instanceof GradientDrawable) {
            ((GradientDrawable) drawable).setColor(color);
        } else if (drawable instanceof ColorDrawable) {
            ((ColorDrawable) drawable).setColor(color);
        } else if (drawable instanceof LayerDrawable) {
            LayerDrawable layerDrawable = (LayerDrawable) drawable;
            RotateDrawable rotateDrawable =
                    (RotateDrawable) layerDrawable.findDrawableByLayerId(R.id.carrot_shape_top);
            setColor(rotateDrawable.getDrawable(), color);
        } else if (drawable instanceof RotateDrawable) {
            setColor(((RotateDrawable) drawable).getDrawable(), color);
        }
    }

    private void setLayoutParamsToBeSetForRootLayout(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;

        int rootLayoutHeightWidth =
                (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, dpWidth, getResources().getDisplayMetrics());

        ViewGroup.LayoutParams params = mRoot.getLayoutParams();

        params.height = rootLayoutHeightWidth;
        params.width = rootLayoutHeightWidth;

        mRoot.setLayoutParams(params);
    }

    public void addTagViewFromTagsToBeTagged(ArrayList<TagToBeTagged> tagsToBeTagged) {
        if (!tagsAreAdded) {
            for (TagToBeTagged tagToBeTagged : tagsToBeTagged) {
                addTag(tagToBeTagged.getX_co_ord(),
                        tagToBeTagged.getY_co_ord(),
                        tagToBeTagged.getUnique_tag_id());
            }
            tagsAreAdded = true;
        }
    }

    public ArrayList<TagToBeTagged> getListOfTagsToBeTagged() {
        ArrayList<TagToBeTagged> tagsToBeTagged = new ArrayList<>();
        if (!mTagList.isEmpty()) {
            for (int i = 0; i < mTagList.size(); i++) {
                TagsView tagsView = mTagList.get(i);
                Log.e("Step 2", "X - > " + tagsView.view.getX() + " Y-> " + tagsView.view.getY());

                float xm = tagsView.view.getX() + tagsView.width;
                float ym = tagsView.view.getY() + tagsView.height;

                float x = (xm * 100) / mRootWidth;   //left
                float y = (ym * 100) / mRootHeight;  //top
                Log.e("Step 3", "X - > " + x + " Y-> " + y);
                tagsToBeTagged.
                        add(new TagToBeTagged(((TextView) tagsView.view.
                                findViewById(R.id.tag_text_view)).getText().toString(), x, y));
            }
        }
        return tagsToBeTagged;
    }

    public TagImageView getTagImageView() {
        return mTagImageView;
    }

    public void showTags() {
        if (!mTagList.isEmpty()) {
            for (TagsView tagsView : mTagList) {
                tagsView.view.setVisibility(VISIBLE);
                tagsView.view.startAnimation(mShowAnimation);
            }
        }
    }

    public void setImageToBeTaggedEvent(TaggedImageEvent taggedImageEvent) {
        if (this.mTaggedImageEvent == null) {
            this.mTaggedImageEvent = taggedImageEvent;
        }
    }

    public void hideTags() {
        if (!mTagList.isEmpty()) {
            for (TagsView tagsView : mTagList) {
                tagsView.view.startAnimation(mHideAnimation);
                tagsView.view.setVisibility(GONE);
            }
        }
    }

    public void removeTags() {
        if (!mTagList.isEmpty()) {
            for (TagsView tagsView : mTagList) {
                mRoot.removeView(tagsView.view);
            }
            mTagList.clear();
        }
    }

    private void updateStayIndicator(View mIndicatorView, View mArrowView) {

        mIndicatorView.measure(0, 0);
        int measuredWidth = mIndicatorView.getMeasuredWidth();
        float thumbCenterX = getThumbCenterX(mIndicatorView);

        int indicatorOffset;
        int arrowOffset;
        if (measuredWidth / 2 + thumbCenterX > mRootWidth) {
            indicatorOffset = mRootWidth - measuredWidth;
            arrowOffset = (int) (thumbCenterX - indicatorOffset - measuredWidth / 2);
        } else if (thumbCenterX - measuredWidth / 2 < 0) {
            indicatorOffset = 0;
            arrowOffset = -(int) (measuredWidth / 2 - thumbCenterX);
        } else {
            indicatorOffset = (int) (getThumbCenterX(mIndicatorView) - measuredWidth / 2);
            arrowOffset = 0;
        }

        updateIndicatorLocation(mIndicatorView, indicatorOffset);
        updateArrowViewLocation(mArrowView, arrowOffset);
    }

    public void animateLike() {
        try {
            mRoot.addView(mLikeImage);
        } catch (Exception ignored) {
            // Illegal Exception is being thrown here while adding mLikeImage
        }
        mLikeImage.setVisibility(View.VISIBLE);
        mLikeImage.setScaleY(0f);
        mLikeImage.setScaleX(0f);

        AnimatorSet animatorSet = new AnimatorSet();

        ObjectAnimator likeScaleUpYAnimator = ObjectAnimator
                .ofFloat(mLikeImage, ImageView.SCALE_Y, 0f, 1f);
        likeScaleUpYAnimator.setDuration(400);
        likeScaleUpYAnimator.setInterpolator(new OvershootInterpolator());

        ObjectAnimator likeScaleUpXAnimator = ObjectAnimator
                .ofFloat(mLikeImage, ImageView.SCALE_X, 0f, 1f);
        likeScaleUpXAnimator.setDuration(400);
        likeScaleUpXAnimator.setInterpolator(new OvershootInterpolator());

        ObjectAnimator likeScaleDownYAnimator = ObjectAnimator
                .ofFloat(mLikeImage, ImageView.SCALE_Y, 1f, 0f);
        likeScaleDownYAnimator.setDuration(100);

        ObjectAnimator likeScaleDownXAnimator = ObjectAnimator
                .ofFloat(mLikeImage, ImageView.SCALE_X, 1f, 0f);
        likeScaleDownXAnimator.setDuration(100);

        animatorSet.playTogether(likeScaleUpXAnimator,
                likeScaleUpYAnimator);

        animatorSet.play(likeScaleDownXAnimator).
                with(likeScaleDownYAnimator).
                after(800);

        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLikeImage.setVisibility(View.GONE);
                mRoot.removeView(mLikeImage);
            }
        });

        animatorSet.start();
    }

    public int getRootWidth() {
        return mRootWidth;
    }

    public void setRootWidth(int mRootWidth) {
        this.mRootWidth = mRootWidth;
    }

    public int getRootHeight() {
        return mRootHeight;
    }

    public void setRootHeight(int mRootHeight) {
        this.mRootHeight = mRootHeight;
    }

    public int getTagTextColor() {
        return mTagTextColor;
    }

    public int getTagBackgroundColor() {
        return mTagBackgroundColor;
    }

    public int getCarrotTopBackGroundColor() {
        return mCarrotTopBackGroundColor;
    }

    public int getCarrotLeftBackGroundColor() {
        return mCarrotLeftBackGroundColor;
    }

    public int getCarrotRightBackGroundColor() {
        return mCarrotRightBackGroundColor;
    }

    public int getCarrotBottomBackGroundColor() {
        return mCarrotBottomBackGroundColor;
    }

    public Drawable getTagTextDrawable() {
        return mTagTextDrawable;
    }

    public Drawable getCarrotTopDrawable() {
        return mCarrotTopDrawable;
    }

    public Drawable getCarrotLeftDrawable() {
        return mCarrotLeftDrawable;
    }

    public Drawable getCarrotRightDrawable() {
        return mCarrotRightDrawable;
    }

    public Drawable getCarrotBottomDrawable() {
        return mCarrotBottomDrawable;
    }

    public boolean canWeAddTags() {
        return canWeAddTags;
    }

    public void setCanWeAddTags(boolean mCanWeAddTags) {
        this.canWeAddTags = mCanWeAddTags;
    }

    public boolean isShowAllCarrots() {
        return showAllCarrots;
    }

    public void setLikeResource(@DrawableRes int resource) {
        mLikeImage.setImageResource(resource);
    }

    public void setLikeDrawable(Drawable drawable) {
        mLikeImage.setImageDrawable(drawable);
    }

    public void setLikeColor(@ColorInt int color) {
        mLikeImage.setColorFilter(color);
    }

    void updateIndicatorLocation(View mIndicatorView, int offset) {

        //Todo indicatore or bubble move
        setMargin(mIndicatorView, offset, -1, -1, -1);
    }

    void updateArrowViewLocation(View mArrowView, int offset) {
        //Todo arrow move
        setMargin(mArrowView, offset, -1, -1, -1);
    }

    private void setMargin(View view, int left, int top, int right, int bottom) {
        if (view == null) {
            return;
        }
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            layoutParams.setMargins(left == -1 ? layoutParams.leftMargin : left, top == -1 ? layoutParams.topMargin : top, right == -1 ? layoutParams.rightMargin : right, bottom == -1 ? layoutParams.bottomMargin : bottom);
            view.requestLayout();
        }
    }

    public interface TaggedImageEvent {
        void singleTapConfirmedAndRootIsInTouch(float x, float y);

        boolean onDoubleTap(MotionEvent e);

        boolean onDoubleTapEvent(MotionEvent e);

        void onLongPress(MotionEvent e);
    }
}

