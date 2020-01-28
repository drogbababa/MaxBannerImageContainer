package com.drogbababa.maxbannerimagecontainer;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

public class MaxBannerImageContainer extends FrameLayout{
    private List<View> mItemViewList;
    private MaxBannerBaseAdapter mMaxBannerBaseAdapter; // for data.

    private int mRadius;
    private Path mPath;
    private RectF mRect;

    private GestureDetector mGestureDetector;
    private ValueAnimator mSuccessAnimator;
    private ValueAnimator mRestAnimator;

    private int mCurIndex;
    private View mCurImageView;
    private View mToShowImageView;
    private float mLastX;
    private float mOffsetX;
    private float mFinalOffsetX; // Final move distance when trigger up event.
    private boolean mIsToShowNext; // Whether to show the next child view, true-next, false-previous.
    private boolean mIsFirstMove; // Whether the action that just triggered the move.

    private boolean mIsScrolling;
    private int mTouchSlop; // Minimum distance to trigger the move event

    private OnItemClickListener mOnItemClickListener;

    private final static int FLING_THRESHOLD = 1000;
    private final static int DEFAULT_RADIUS = 40;

    public MaxBannerImageContainer(Context context) {
        this(context, null);
    }

    public MaxBannerImageContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MaxBannerImageContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initField(context, attrs);
        init(context);
    }

    private void initField(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MaxBannerImageContainer);
        mRadius = (int) typedArray.getDimension(R.styleable.MaxBannerImageContainer_radius, DEFAULT_RADIUS);
        typedArray.recycle();
    }

    private void init(Context context) {
        mPath = new Path();
        mRect = new RectF();
        mItemViewList = new ArrayList<>();

        mSuccessAnimator = ValueAnimator.ofFloat(0, 1).setDuration(200);
        mSuccessAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                float diffX = (getWidth() - Math.abs(mFinalOffsetX)) * value;
                if (mIsToShowNext) {
                    mOffsetX = mFinalOffsetX - diffX;
                } else {
                    mOffsetX = mFinalOffsetX + diffX;
                }
                requestLayout();
            }
        });
        mSuccessAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                removeView(mCurImageView);
                mOffsetX = 0;
                if (mIsToShowNext) {
                    mCurIndex++;
                } else {
                    mCurIndex--;
                }
                mIsScrolling = false;
                requestLayout();
            }
        });

        mRestAnimator = ValueAnimator.ofFloat(0, 1).setDuration(200);
        mRestAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                float diffX = Math.abs(mFinalOffsetX) * value;
                if (mIsToShowNext) {
                    mOffsetX = mFinalOffsetX + diffX;
                } else {
                    mOffsetX = mFinalOffsetX - diffX;
                }
                mIsScrolling = false;
                requestLayout();
            }
        });

        mGestureDetector = new GestureDetector(context, mSimpleGestureDetector);
        mGestureDetector.setIsLongpressEnabled(false);

        ViewConfiguration mViewConfiguration = ViewConfiguration.get(context);
        mTouchSlop = mViewConfiguration.getScaledTouchSlop() / 4;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (mCurImageView == null || mToShowImageView == null) {
            return;
        }
        if (mIsToShowNext) {
            layoutSingleImageView(mCurImageView, getPaddingLeft(), getPaddingTop(), (int) (getPaddingLeft() + getWidth() + mOffsetX), getPaddingTop() + getHeight());
        } else {
            layoutSingleImageView(mCurImageView, (int) (getPaddingLeft() + mOffsetX), getPaddingTop(), getPaddingLeft() + getWidth(), getPaddingTop() + getHeight());
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mPath.reset();
        mRect.set(getPaddingLeft(), getPaddingTop(), getPaddingLeft() + w, getPaddingTop() + h);
        mPath.addRoundRect(mRect, mRadius, mRadius, Path.Direction.CW);
    }

    @Override
    public void draw(Canvas canvas) {
        int save = canvas.save();
        canvas.clipPath(mPath);
        super.draw(canvas);
        canvas.restoreToCount(save);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        int save = canvas.save();
        canvas.clipPath(mPath);
        super.dispatchDraw(canvas);
        canvas.restoreToCount(save);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            mIsScrolling = false;
            return false;
        } else if (action == MotionEvent.ACTION_MOVE) {
            if (mIsScrolling) {
                return true;
            }
            float xDiff = calculateDistanceX(ev);
            if (xDiff > mTouchSlop) {
                mIsScrolling = true;
                return true;
            }
            return false;
        } else if (action == MotionEvent.ACTION_DOWN) {
            mOffsetX = 0;
            mLastX = ev.getX();
            mIsFirstMove = true;
            if (mCurIndex < 0 || mCurIndex >= mItemViewList.size()) {
                mCurImageView = null;
            } else {
                mCurImageView = mItemViewList.get(mCurIndex);
            }
            return false;
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
            if (!mGestureDetector.onTouchEvent(event)) { // In order to use mGestureDetector.onFling, if return false, it is a normal up event.
                onActionUp(false);
            }
            return true;
        }
        return mGestureDetector.onTouchEvent(event);
    }

    // 自定义手势操作
    private GestureDetector.SimpleOnGestureListener mSimpleGestureDetector = new GestureDetector.SimpleOnGestureListener(){
        @Override
        public boolean onDown(MotionEvent e) {
            mOffsetX = 0;
            mLastX = e.getX();
            mIsFirstMove = true;
            if (mCurIndex < 0 || mCurIndex >= mItemViewList.size()) {
                mCurImageView = null;
            } else {
                mCurImageView = mItemViewList.get(mCurIndex);
            }
            return mCurImageView != null;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return onActionMove(e2, distanceX);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return onActionFling(velocityX);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(mCurImageView, mCurIndex);
            }
            return true;
        }
    };

    private boolean onActionMove(MotionEvent event, float distanceX) {
        mOffsetX += (event.getX() - mLastX); // Total travel distance
        if (event.getX() == mLastX) { // fix bug mOffsetX = 0
            mOffsetX += (-distanceX);
        }
        if (mOffsetX < -getWidth()) {
            mOffsetX = -getWidth();
        }
        if (mOffsetX > getWidth()) {
            mOffsetX = getWidth();
        }
        if (mIsFirstMove) {
            mIsFirstMove = false;
            mIsToShowNext = mOffsetX < 0; // If show next.
            if (mIsToShowNext) {
                mToShowImageView = getNext();
            } else {
                mToShowImageView = getPrevious();
            }
            if (mToShowImageView == null) {
                return false;
            }
            removeView(mToShowImageView);
            addView(mToShowImageView, 0);
        }
        mLastX = event.getX();
        requestLayout();
        return true;
    }

    private void onActionUp(boolean isForceSuccess) {
        if (mToShowImageView == null || mToShowImageView.getParent() == null) {
            return;
        }
        mFinalOffsetX = mOffsetX;
        if ((Math.abs(mOffsetX) >= getWidth() / 2) || isForceSuccess) { // success
            if (mSuccessAnimator != null) {
                mSuccessAnimator.cancel();
                mSuccessAnimator.start();
            }
        } else { // bounce back
            if (mRestAnimator != null) {
                mRestAnimator.cancel();
                mRestAnimator.start();
            }
        }
    }

    private boolean onActionFling(float velocityX) {
        if (velocityX < -FLING_THRESHOLD) { // If the speed exceeds, it is directly next without having to consider the limitation of the sliding distance. onScroll is called before onFling, so you don't need to deal with the sliding distance.
            if (mToShowImageView == null) {
                mToShowImageView = getNext();
            }
            onActionUp(true);
            return true;
        } else if (velocityX > FLING_THRESHOLD) { // previous
            if (mToShowImageView == null) {
                mToShowImageView = getPrevious();
            }
            onActionUp(true);
            return true;
        } else { // up and reset
            return false;
        }
    }

    private void layoutSingleImageView(View view, int left, int top, int right, int bottom) {
        view.layout(left, top, right, bottom);
    }

    private View getNext() {
        if (mCurImageView == null) {
            return null;
        }
        if (mCurIndex + 1 < 0 || mCurIndex + 1 >= mItemViewList.size()) {
            return null;
        }
        return mItemViewList.get(mCurIndex + 1);
    }

    private View getPrevious() {
        if (mCurImageView == null) {
            return null;
        }
        if (mCurIndex - 1 < 0 || mCurIndex - 1 >= mItemViewList.size()) {
            return null;
        }
        return mItemViewList.get(mCurIndex - 1);
    }

    private float calculateDistanceX(MotionEvent event) {
        return Math.abs(event.getX() - mLastX);
    }

    /**
     * Call this after {@link #setMaxBannerBaseAdapter(MaxBannerBaseAdapter maxBannerBaseAdapterAdapter)}.
     */
    public void update() {
        if (mMaxBannerBaseAdapter == null) {
            return;
        }
        removeAllViews();
        mItemViewList.clear();
        mCurIndex = 0;
        mCurImageView = null;
        mToShowImageView = null;
        for (int i = 0; i < mMaxBannerBaseAdapter.getItemCount(); i++) {
            View childItem = mMaxBannerBaseAdapter.onCreateView(this, mMaxBannerBaseAdapter.getItemViewType(i));
            mMaxBannerBaseAdapter.onFillView(childItem, i);
            mItemViewList.add(childItem);
            layoutSingleImageView(childItem, getPaddingLeft(), getPaddingTop(), getPaddingLeft() + getWidth(), getPaddingTop() + getHeight());
            if (childItem instanceof MaxBannerImageView) {
                ((MaxBannerImageView) childItem).mIsFirstInit = true;
            }
        }
        if (!mItemViewList.isEmpty()) {
            addView(mItemViewList.get(0));
            mItemViewList.get(0).requestLayout();
        }
        requestLayout();
    }

    public void setMaxBannerBaseAdapter(MaxBannerBaseAdapter maxBannerBaseAdapter) {
        mMaxBannerBaseAdapter = maxBannerBaseAdapter;
    }

    /**
     *
     * @param onItemClickListener Click listener for every item view.
     * You can choose this or set android.OnClickListener in MaxBannerBaseAdapter.onFillView.
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }


    /**
     * Recommend ImageView as containers' item, which scaleType is matrix.
     * The drawable always coincides with the left top corner of the original view.
     */
    public class MaxBannerImageView extends android.support.v7.widget.AppCompatImageView {
        private boolean mIsFirstInit;
        private int originalWidth;
        private int originalHeight;

        public MaxBannerImageView(Context context) {
            this(context, null);
        }

        public MaxBannerImageView(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public MaxBannerImageView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init();
        }

        private void init() {
            mIsFirstInit = true;
            setScaleType(ScaleType.MATRIX);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            if (mIsFirstInit) {
                originalWidth = w;
                originalHeight = h;
                mIsFirstInit = false;
            }
        }

        @Override
        protected boolean setFrame(int l, int t, int r, int b) {
            boolean result = super.setFrame(l, t, r, b);
            recomputeImgMatrix(l);
            return result;
        }

        private void recomputeImgMatrix(int left) {
            final Drawable drawable = getDrawable();
            if (drawable == null || originalWidth == 0 || originalHeight == 0) {
                return;
            }
            final Matrix matrix = getImageMatrix();

            float scale;
            final int drawableWidth = drawable.getIntrinsicWidth();
            final int drawableHeight = drawable.getIntrinsicHeight();

            if (drawableWidth * originalHeight > drawableHeight * originalWidth) {
                scale = (float) originalHeight / (float) drawableHeight;
            } else {
                scale = (float) originalWidth / (float) drawableWidth;
            }

            matrix.setScale(scale, scale);
            matrix.postTranslate(-left, 0);
            setImageMatrix(matrix);
        }

    }

}

