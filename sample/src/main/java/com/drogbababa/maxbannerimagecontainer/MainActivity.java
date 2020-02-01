package com.drogbababa.maxbannerimagecontainer;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MaxBannerImageContainer maxBannerImageContainer = findViewById(R.id.max_banner_view);
        maxBannerImageContainer.setData(getViewPagerDatas());
        maxBannerImageContainer.setOnItemClickListener(new MaxBannerImageContainer.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(MainActivity.this, "position " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private List<MaxBaseItem> getViewPagerDatas() {
        List<MaxBaseItem> pagerItemBeanList = new ArrayList<>();
        int[] drawables = {R.drawable.jose, R.drawable.chelsea, R.drawable.arsenal};
        MaxBaseItem.IImageLoader imageLoader = new MaxBaseItem.IImageLoader() {
            @Override
            public void loadImage(Context context, View view, Object data) {
                ((ImageView) view).setImageResource((Integer) data);
            }
        };
        for (int i = 0; i < 3; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            MaxBaseItem maxBaseItem = new MaxBaseItem(imageView, drawables[i]);
            maxBaseItem.setImageLoader(imageLoader);
            pagerItemBeanList.add(maxBaseItem);
        }
        return pagerItemBeanList;
    }

    private List<MaxBaseItem> getViewPagerDatas2() {
        List<MaxBaseItem> pagerItemBeanList = new ArrayList<>();
        String[] drawables = {"http://img4.imgtn.bdimg.com/it/u=2384826157,3968396881&fm=26&gp=0.jpg"
                , "http://img0.imgtn.bdimg.com/it/u=1114614454,1728895346&fm=26&gp=0.jpg", "http://img5.imgtn.bdimg.com/it/u=2544405930,1726145189&fm=26&gp=0.jpg"};
        MaxBaseItem.IImageLoader imageLoader = new MaxBaseItem.IImageLoader() {
            @Override
            public void loadImage(Context context, View view, Object data) {
                Glide.with(context).load((String)data).into((ImageView) view);
            }
        };
        for (int i = 0; i < 3; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            MaxBaseItem maxBaseItem = new MaxBaseItem(imageView, drawables[i]);
            maxBaseItem.setImageLoader(imageLoader);
            pagerItemBeanList.add(maxBaseItem);
        }
        return pagerItemBeanList;
    }
}
