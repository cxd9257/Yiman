package com.demo.yiman.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.demo.yiman.R;
import com.demo.yiman.bean.NewsDataBean;
import com.demo.yiman.bean.NewsDetailModle;
import com.demo.yiman.utils.ImageLoaderUtil;
import com.demo.yiman.widget.CustomRoundAngleImageView;

import java.util.List;

import static android.support.constraint.ConstraintSet.GONE;

public class NewsDetailAdapter extends BaseQuickAdapter<NewsDataBean,BaseViewHolder>{


    public NewsDetailAdapter(int layoutResId, @Nullable List<NewsDataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, NewsDataBean bean) {
                helper.setText(R.id.tv_news_title,bean.getTitle());
                helper.setText(R.id.tv_news_source,bean.getAuthor_name());
                helper.setText(R.id.tv_news_date,bean.getDate());
                CustomRoundAngleImageView iv2 = helper.getView(R.id.iv_news_2);
                CustomRoundAngleImageView iv3 = helper.getView(R.id.iv_news_3);
                try {
                    ImageLoaderUtil.LoadImage(mContext,bean.getThumbnail_pic_s(), (CustomRoundAngleImageView) helper.getView(R.id.iv_news_1));
                    if (bean.getThumbnail_pic_s02().isEmpty()){
                        iv2.setVisibility(View.GONE);
                    }else{
                        ImageLoaderUtil.LoadImage(mContext,bean.getThumbnail_pic_s02(), iv2);
                    }
                    if (bean.getThumbnail_pic_s03().isEmpty()){
                        iv3.setVisibility(View.GONE);
                    }else{
                        ImageLoaderUtil.LoadImage(mContext,bean.getThumbnail_pic_s03(), iv3);
                    }

                }catch (Exception e){
                    iv3.setVisibility(View.GONE);
                    try {
                        if (bean.getThumbnail_pic_s02().isEmpty()){
                            iv3.setVisibility(View.GONE);
                        }else{
                            ImageLoaderUtil.LoadImage(mContext,bean.getThumbnail_pic_s02(), iv2);
                        }
                    }catch (Exception e1){
                        iv2.setVisibility(View.GONE);
                    }
                }
                helper.addOnClickListener(R.id.iv_news_close);

    }
}
