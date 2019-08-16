package com.demo.yiman.database;

import com.demo.yiman.bean.NewsDataBean;
import com.demo.yiman.bean.NewsDetailModle;

import java.util.List;

public class YimanDbController {
    private static YimanDbController instance;//写成单例形势
    public static DaoSession mDaoSession;
    public NewsDataBeanDao dao;
    public static YimanDbController getInstance() {
        if (instance == null){
            instance = new YimanDbController();
        }
        return instance;
    }

    private YimanDbController(){
        mDaoSession = YimanDbOpenHelper.mDaoSession;
        dao = mDaoSession.getNewsDataBeanDao();
    }

    public List<NewsDataBean> queryAll(){
        return dao.queryBuilder().list();
    }

    public void insertOrReplace(List<NewsDataBean> list){
        this.dao.insertOrReplaceInTx(list);
    }

    public void delete(List<NewsDataBean> list){
        this.dao.deleteAll();
    }
}
