package com.drogbababa.maxbannerimagecontainer;

import android.view.View;

public abstract class MaxBannerBaseAdapter<V extends View> {
    /*return the view you want to create.*/
    abstract V onCreateView(MaxBannerImageContainer parent, int viewType);

    /*bind data to view.*/
    abstract void onFillView(V view, int position);

    /*equal to your data set.*/
    abstract int getItemCount();

    /*MultiType.*/
    public int getItemViewType(int position) {
        return 0;
    }
}
