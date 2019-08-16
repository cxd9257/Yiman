package com.demo.yiman.database.convert;
import com.demo.yiman.bean.NewsDetailModle;
import com.google.gson.Gson;

import org.greenrobot.greendao.converter.PropertyConverter;


public class ResultBeanConvert implements PropertyConverter<NewsDetailModle.ResultBean,String> {

    @Override
    public NewsDetailModle.ResultBean convertToEntityProperty(String databaseValue) {
        return new Gson().fromJson(databaseValue, NewsDetailModle.ResultBean.class);
    }

    @Override
    public String convertToDatabaseValue(NewsDetailModle.ResultBean entityProperty) {
        return new Gson().toJson(entityProperty);
    }
}
