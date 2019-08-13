package com.demo.yiman.ui.joke;

import com.demo.yiman.base.baseMVP.BaseView;
import com.demo.yiman.bean.JokeModle;
import com.demo.yiman.bean.NewsDetailModle;

public interface JokeView extends BaseView {

    void onJokeSucc(JokeModle jokeModle);

    void loadMoreData(String type,JokeModle jokeModle);
}
