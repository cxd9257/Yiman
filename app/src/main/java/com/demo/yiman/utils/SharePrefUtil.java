package com.demo.yiman.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.demo.yiman.MyApp;

public class SharePrefUtil {
    private static SharedPreferences pref;
    private static SharedPreferences.Editor edit;

    static {
        pref = PreferenceManager
                .getDefaultSharedPreferences(MyApp.getContext().getApplicationContext());
        edit = pref.edit();
    }

    public static void putBoolean(String key, boolean value) {
        edit.putBoolean(key, value);
    }

    public static void putInt(String key, int value) {
        edit.putInt(key, value);
    }

    public static void putString(String key, String value) {
        edit.putString(key, value);
    }

    public static void putFloat(String key, float value) {
        edit.putFloat(key, value);
    }

    public static void putLong(String key, long value) {
        edit.putLong(key, value);
    }

    public static String getString(String key) {
        // if (key.equals(AppConfig.TICKET)) {
        // return "8888";
        // }
        return pref.getString(key, "");
    }

    public static int getInt(String key) {
        return pref.getInt(key, 0);
    }

    public static boolean getBoolean(String key) {
        return pref.getBoolean(key, false);
    }

    public static float getFloat(String key) {
        return pref.getFloat(key, 0);
    }

    public static long getLong(String key) {
        return pref.getLong(key, 0);
    }

    public static void commit() {
        edit.commit();
    }
}
