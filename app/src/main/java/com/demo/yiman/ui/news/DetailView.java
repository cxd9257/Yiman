package com.demo.yiman.ui.news;

import com.demo.yiman.base.baseMVP.BaseView;
import com.demo.yiman.bean.NewsDetailModle;

import java.util.List;

public interface DetailView extends BaseView {
    void onNewsSucc(NewsDetailModle newsDetailModle);
    void loadData(List<NewsDetailModle.ResultBean.DataBean> newsDetailModle);
}
