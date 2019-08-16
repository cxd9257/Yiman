package com.demo.yiman.database.convert;

import com.demo.yiman.bean.NewsDataBean;
import com.demo.yiman.bean.NewsDetailModle;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class NewsConvert implements PropertyConverter<List<NewsDataBean>,String> {

    @Override
    public List<NewsDataBean> convertToEntityProperty(String databaseValue) {
        if (databaseValue == null) {
            return null;
        }
        Type type = new TypeToken<ArrayList<NewsDataBean>>() {
        }.getType();
        return new Gson().fromJson(databaseValue,type);
    }

    @Override
    public String convertToDatabaseValue(List<NewsDataBean> entityProperty) {
        if (entityProperty == null || entityProperty.size()==0) {
            return null;
        }
        return new Gson().toJson(entityProperty);
    }
}
