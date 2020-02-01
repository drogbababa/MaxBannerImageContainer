package com.drogbababa.maxbannerimagecontainer;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class MaxBannerBaseAdapter extends PagerAdapter {
    private List<MaxBaseItem> mData;
    private MaxBannerImageContainer.OnItemClickListener mOnItemClickListener;

    @Override
    public int getCount() {
        if (mData == null) {
            return 0;
        }
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        final View view = mData.get(position).getView();
        mData.get(position).displayImage();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, position);
                }
            }
        });
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(mData.get(position).getView());
    }

    public void refreshAdapter(final List<MaxBaseItem> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(MaxBannerImageContainer.OnItemClickListener onItemClickListener){
        this.mOnItemClickListener = onItemClickListener;
    }
}
