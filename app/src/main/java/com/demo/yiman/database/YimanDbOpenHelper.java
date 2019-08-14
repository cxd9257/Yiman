package com.demo.yiman.database;

import android.database.sqlite.SQLiteDatabase;

import com.demo.yiman.MyApp;
import com.demo.yiman.utils.AppConfig;

public class YimanDbOpenHelper {


    private static DaoSession mDaoSession;
    public static void initDataBase(){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(MyApp.getContext(), AppConfig.DB_NAME);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        mDaoSession = daoMaster.newSession();
    }
    public static DaoSession getmDaoSession() {
        return mDaoSession;
    }
}
