package com.demo.yiman.base.gson;

import android.text.TextUtils;
import android.util.Log;

import com.demo.yiman.base.baseMVP.BaseException;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

public class BaseResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;
    BaseResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String jsonString = value.string();
        try{
            JSONObject object = new JSONObject(jsonString);
            Log.e("xxx","会来吗？");
//            int status = object.getInt("result");
//            if (status != 1){
//                String msg = object.getString("msg");
//                if (TextUtils.isEmpty(msg)){
//                    msg = object.getString("error");
//                }
//                throw  new BaseException(msg,status);
//            }
            return adapter.fromJson(jsonString);
        }catch (JSONException e){
            e.printStackTrace();
            //数据解析异常
            throw new BaseException(BaseException.PARSE_ERROR_MSG,BaseException.PARSE_ERROR);
        }finally {
            value.close();
        }
    }
}
