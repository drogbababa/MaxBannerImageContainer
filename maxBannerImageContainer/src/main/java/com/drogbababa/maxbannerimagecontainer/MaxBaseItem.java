package com.drogbababa.maxbannerimagecontainer;

import android.content.Context;
import android.view.View;

public class MaxBaseItem {
    private View mView;
    private String mTitle;
    private Object mData;
    private IImageLoader mImageLoader;

    public MaxBaseItem(View view, Object data) {
        this.mView = view;
        this.mData = data;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public void setImageLoader(IImageLoader imageLoader) {
        this.mImageLoader = imageLoader;
    }

    public String getTitle() {
        return mTitle;
    }

    public View getView() {
        return mView;
    }

    public void displayImage() {
        mImageLoader.loadImage(mView.getContext(), mView, mData);
    }

    public interface IImageLoader{
        void loadImage(Context context, View view, Object data);
    }
}
