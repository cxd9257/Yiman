package com.demo.yiman.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

public class CustomViewPager extends ViewPager {
    public CustomViewPager(@NonNull Context context) {
        super(context);
    }

    public CustomViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    public void setCurrentItem(int item,boolean smoothScrol){
        super.setCurrentItem(item,smoothScrol);
    }
    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item, false);
    }
}
