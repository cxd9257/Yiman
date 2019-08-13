package com.demo.yiman.ui.adapter;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.demo.yiman.R;
import com.demo.yiman.bean.ImageModle;
import com.demo.yiman.utils.ImageLoaderUtil;
import com.demo.yiman.widget.ScaleImageView;

import java.util.List;

public class ImageAdapter extends BaseQuickAdapter<ImageModle.ResultsBean, BaseViewHolder> {
    public ImageAdapter(List<ImageModle.ResultsBean> data) {
        super(R.layout.item_image_layout, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ImageModle.ResultsBean item) {
        ScaleImageView imageView = helper.getView(R.id.girl_item_iv);
        imageView.setInitSize(item.getWidth(), item.getHeight());
        ImageLoaderUtil.LoadImage(mContext,item.getUrl(), imageView);
    }
}
