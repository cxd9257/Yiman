package com.demo.yiman;

import org.litepal.LitePal;
import org.litepal.LitePalApplication;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;

public class MyApp extends LitePalApplication {
    public static MyApp instance;

    @Override
    public void onCreate() {
        super.onCreate();
        BGASwipeBackHelper.init(this,null);
        LitePal.initialize(this);
    }

    public synchronized static MyApp getInstance() {
        if (null == instance) {
            instance = new MyApp();
        }
        return instance;
    }
}
