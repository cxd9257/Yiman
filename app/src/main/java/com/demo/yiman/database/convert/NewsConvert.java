package com.demo.yiman.database.convert;

import com.demo.yiman.bean.NewsDetailModle;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class NewsConvert implements PropertyConverter<List<NewsDetailModle.ResultBean.DataBean>,String> {

    @Override
    public List<NewsDetailModle.ResultBean.DataBean> convertToEntityProperty(String databaseValue) {
        Type type = new TypeToken<ArrayList<NewsDetailModle.ResultBean.DataBean>>() {
        }.getType();
        return new Gson().fromJson(databaseValue,type);
    }

    @Override
    public String convertToDatabaseValue(List<NewsDetailModle.ResultBean.DataBean> entityProperty) {
        return new Gson().toJson(entityProperty);
    }
}
