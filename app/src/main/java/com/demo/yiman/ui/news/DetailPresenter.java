package com.demo.yiman.ui.news;

import com.demo.yiman.base.baseMVP.BaseModel;
import com.demo.yiman.base.baseMVP.BaseObserver;
import com.demo.yiman.base.baseMVP.BasePresenter;
import com.demo.yiman.bean.NewsDetailModle;
import com.demo.yiman.net.ApiConfig;

import java.util.List;

public class DetailPresenter extends BasePresenter<DetailView> {
    public DetailPresenter(DetailView baseView) {
        super(baseView);
    }
    public void getNews(String type){
        addDisposable(apiServer.newsByRx(type, ApiConfig.JuHeKey), new BaseObserver<NewsDetailModle>(baseView) {

            @Override
            protected void onSuccess(NewsDetailModle  newsDetailModle) {
                baseView.onNewsSucc(newsDetailModle);
            }

            @Override
            public void onError(String msg) {
                baseView.showError(msg);
            }
        });

    }
}
