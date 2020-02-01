package com.drogbababa.maxbannerimagecontainer;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import java.util.List;

public class MaxBannerImageContainer extends FrameLayout{
    private int mRadius;
    private Path mPath;
    private RectF mRect;

    private ViewPager mViewPager;
    private MaxBannerBaseAdapter mAdapter;

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
        initViewPager(context);
    }

    private void initViewPager(Context context) {
        mViewPager = new ViewPager(context);
        mAdapter = new MaxBannerBaseAdapter();
        mViewPager.setAdapter(mAdapter);
        mViewPager.setPageTransformer(true, new MaxParallaxTransformer());
        mViewPager.setOffscreenPageLimit(3);
        addView(mViewPager);
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

    public void setData(final List<MaxBaseItem> list) {
        mAdapter.refreshAdapter(list);
        if (list != null && !list.isEmpty()) {
            mViewPager.setCurrentItem(0);
        }
    }

    public void setAdapter(MaxBannerBaseAdapter adapter) {
        mAdapter = adapter;
        mViewPager.setAdapter(adapter);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mAdapter.setOnItemClickListener(onItemClickListener);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

}

