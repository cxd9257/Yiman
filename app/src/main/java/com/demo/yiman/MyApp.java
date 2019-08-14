package com.demo.yiman;

import com.demo.yiman.database.YimanDbOpenHelper;
import com.demo.yiman.utils.AppConfig;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;

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
        Beta.autoCheckUpgrade = true;
        Bugly.init(getApplicationContext(), AppConfig.BUGLY_KEY,false);
        YimanDbOpenHelper.initDataBase();

    }

    public synchronized static MyApp getInstance() {
        if (null == instance) {
            instance = new MyApp();
        }
        return instance;
    }
}
