package com.demo.yiman.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.demo.yiman.R;
import com.demo.yiman.bean.JokeModle;
import com.demo.yiman.net.ApiConfig;
import com.demo.yiman.ui.joke.ImageBrowseActivity;
import com.demo.yiman.utils.ImageLoaderUtil;
import com.demo.yiman.widget.CircleImageView;
import com.demo.yiman.widget.CustomRoundAngleImageView;
import com.demo.yiman.widget.MultiImageView;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;


public class JokeDefaultAdapter extends BaseQuickAdapter<JokeModle.ResultBean, BaseViewHolder> implements BaseQuickAdapter.OnItemClickListener {

    private Activity mContext;

    public JokeDefaultAdapter(Activity context, @Nullable List<JokeModle.ResultBean> data) {
        super(R.layout.item_joke_defaul, data);
        this.mContext = context;
    }



    @Override
    protected void convert(BaseViewHolder helper, final JokeModle.ResultBean item) {
        String type = item.getType();
        if (type.isEmpty()) return;
        if (type.equals(ApiConfig.TYPE_GIF)){
            helper.setGone(R.id.iv_context,true);
            helper.setGone(R.id.jz_video,false);
            CustomRoundAngleImageView imageView = helper.getView(R.id.iv_context);
            ImageLoaderUtil.LoadImage(mContext,item.getThumbnail(),imageView);
//            imageView.setList(Collections.singletonList(item.getImages().toString()));
//            imageView.setOnItemClickListener(new MultiImageView.OnItemClickListener() {
//                @Override
//                public void onItemClick(View view, int position) {
//                    ImageBrowseActivity.launch(mContext, new String[]{item.getImages().toString()},position);
//                }
//            });
        }else if(type.equals(ApiConfig.TYPE_VIDEO)){
            helper.setGone(R.id.iv_context,false);
            helper.setGone(R.id.ll_context,true);
            JCVideoPlayerStandard jzvdStd = helper.getView(R.id.jz_video);
            jzvdStd.setUp(item.getVideo(), JCVideoPlayer.SCREEN_LAYOUT_NORMAL,"");
            ImageLoaderUtil.LoadImage(mContext,item.getThumbnail(),jzvdStd.thumbImageView);
        }else if(type.equals(ApiConfig.TYPE_IMAGE)){
            helper.setGone(R.id.iv_context,true);
            helper.setGone(R.id.jz_video,false);
            CustomRoundAngleImageView imageView = helper.getView(R.id.iv_context);
            ImageLoaderUtil.LoadImage(mContext,item.getImages(),imageView);
            String str = item.getImages().toString();
            final String[] arr = str.split(","); // 用,分割
            helper.getView(R.id.iv_context).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageBrowseActivity.launch(mContext, arr,0);
                }
            });
        }else{
            helper.setGone(R.id.include_context,false);
        }
        ImageLoaderUtil.LoadImage(mContext,item.getHeader(),(CircleImageView)helper.getView(R.id.iv_header));
        helper.setText(R.id.tv_name,item.getName());
        helper.setText(R.id.tv_context,item.getText());
        helper.setText(R.id.tv_cry,item.getDown()+"");
        helper.setText(R.id.tv_smile,item.getUp()+"");
        helper.setText(R.id.tv_comment,item.getComment()+"");

        if (item.getTop_comments_content()!=null){
            helper.setGone(R.id.tv_top_comment,true);
            helper.setGone(R.id.tv_top_name,true);
            helper.setText(R.id.tv_top_comment,item.getTop_comments_content());
            helper.setText(R.id.tv_top_name,item.getTop_comments_name());
        }else{
            helper.setGone(R.id.tv_top_comment,false);
            helper.setGone(R.id.tv_top_name,false);
        }



    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

    }
}
