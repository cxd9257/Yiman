package com.demo.yiman.ui.image;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

import com.demo.yiman.bean.ImageModle;
import com.demo.yiman.utils.ImageLoaderUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ImageService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param
     */
    public ImageService() {
        super("");
    }
    public static void startService(Context context, ImageModle datas, String subtype) {
        Intent intent =new Intent(context, ImageService.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", (Serializable) datas.getResults());
        intent.putExtras(bundle);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent == null) {
            return;
        }
        List<ImageModle.ResultsBean> datas = (List<ImageModle.ResultsBean>) intent.getSerializableExtra("data");
        String subtype = intent.getStringExtra("subtype");
        handleGirlItemData(datas, subtype);
    }
    private void handleGirlItemData(List<ImageModle.ResultsBean> imageDatas, String subtype) {
        if (imageDatas.size() == 0) {
            EventBus.getDefault().post("finish");
            return;
        }
        for (ImageModle.ResultsBean results : imageDatas) {
            Bitmap bitmap = ImageLoaderUtil.load(this, results.getUrl());
            if (bitmap != null) {
                results.setWidth(bitmap.getWidth());
                results.setHeight(bitmap.getHeight());
            }
            //results.setSubtype(subtype);
        }
        EventBus.getDefault().post(imageDatas);

    }


}
