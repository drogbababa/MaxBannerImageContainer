package com.drogbababa.maxbannerimagecontainer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MaxBannerImageContainer maxBannerImageContainer = findViewById(R.id.max_banner_view);
        List<Integer> data = new ArrayList<>();
        data.add(R.drawable.chelsea);
        data.add(R.drawable.jose);
        data.add(R.drawable.arsenal);
        ExampleImageAdapter exampleImageAdapter = new ExampleImageAdapter(data);
        maxBannerImageContainer.setMaxBannerBaseAdapter(exampleImageAdapter);
        maxBannerImageContainer.update();
    }
}
