package com.demo.yiman.utils;

import android.app.Activity;
import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ToolUtil {
    public static int getStateBarHeight(Activity a) {
        int result = 0;
        int resourceId = a.getResources().getIdentifier("status_bar_height",
                "dimen", "android");
        if (resourceId > 0) {
            result = a.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
    /**
     * dp转px
     * @param dip       dp
     * @param context   上下文
     * @return
     */
    public static int dip2px(float dip, Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        int px = (int) (dip * density + 0.5f);// 4.9->4, 4.1->4, 四舍五入
        return px;
    }

    /**
     * px转dp
     * @param px        px
     * @param context   上下文
     * @return
     */
    public static float px2dip(int px, Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        float dp = px / density;
        return dp;
    }
    /**
     * 获取当前日期
     */
    public static String getDate(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");// HH:mm:ss
        Date date = new Date(System.currentTimeMillis());//获取当前时间
        return simpleDateFormat.format(date);
    }

}
