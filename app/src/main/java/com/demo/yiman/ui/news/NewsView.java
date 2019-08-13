package com.demo.yiman.ui.news;

import com.demo.yiman.base.baseMVP.BaseView;
import com.demo.yiman.bean.Channel;

import java.util.List;

public interface  NewsView extends BaseView {
    void onNewsSucc();
    void loadData(List<Channel> channels,List<Channel> otherChannels);
    /**
     * 初始化频道
     */
    void getChannel();
}
