package com.drogbababa.maxbannerimagecontainer;

import android.support.v4.view.ViewPager;
import android.view.View;

public class MaxParallaxTransformer implements ViewPager.PageTransformer {

    @Override
    public void transformPage(View page, float position) {
        int width = page.getWidth();
        if (position < -1) {
            page.setScrollX((width * -1));
        } else if (position <= 1) {
            if (position < 0) {
                page.setScrollX((int) (width * position));
            } else {
                page.setScrollX((int) (width * position));
            }
        } else {
            page.setScrollX(width);
        }
    }
}
