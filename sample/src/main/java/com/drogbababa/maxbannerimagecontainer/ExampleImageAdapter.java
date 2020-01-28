package com.drogbababa.maxbannerimagecontainer;

import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

public class ExampleImageAdapter extends MaxBannerBaseAdapter<MaxBannerImageContainer.MaxBannerImageView> {
    private List<Integer> mData;

    public ExampleImageAdapter(List<Integer> data) {
        this.mData = data;
    }

    @Override
    MaxBannerImageContainer.MaxBannerImageView onCreateView(MaxBannerImageContainer parent, int viewType) {
        ImageView imageView = parent.new MaxBannerImageView(parent.getContext());
        return (MaxBannerImageContainer.MaxBannerImageView) imageView;
    }

    @Override
    void onFillView(MaxBannerImageContainer.MaxBannerImageView view, final int position) {
        view.setImageResource(mData.get(position));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "example" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    int getItemCount() {
        return mData.size();
    }

}
