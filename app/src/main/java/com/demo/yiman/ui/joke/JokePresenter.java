package com.demo.yiman.ui.joke;

import com.demo.yiman.base.baseMVP.BaseObserver;
import com.demo.yiman.base.baseMVP.BasePresenter;
import com.demo.yiman.bean.JokeModle;

public class JokePresenter extends BasePresenter<JokeView> {
    public JokePresenter(JokeView baseView) {
        super(baseView);
    }
    public void getData(final int page, int count, final String type){
        addDisposable(apiServer.getJokeDate(page,count,type), new BaseObserver<JokeModle>(baseView) {
            @Override
            protected void onSuccess(JokeModle jokeModle) {
                if (page>1){
                    baseView.loadMoreData(type,jokeModle);
                }else{
                    baseView.onJokeSucc(jokeModle);
                }
                baseView.hideLoading();
            }

            @Override
            public void onError(String msg) {
                baseView.showError(msg);
                baseView.hideLoading();
            }
        });
    }
}
