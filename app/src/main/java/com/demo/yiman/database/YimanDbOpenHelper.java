package com.demo.yiman.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.demo.yiman.MyApp;
import com.demo.yiman.utils.AppConfig;

public class YimanDbOpenHelper extends DaoMaster.OpenHelper {


    public static DaoSession mDaoSession;

    public YimanDbOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    public static void initDataBase(){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(MyApp.getContext(), AppConfig.DB_NAME);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        mDaoSession = daoMaster.newSession();
    }
}
