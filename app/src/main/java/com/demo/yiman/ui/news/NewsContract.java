package com.demo.yiman.ui.news;


import com.demo.yiman.base.baseMVP.BasePresenter;
import com.demo.yiman.base.baseMVP.BaseView;
import com.demo.yiman.bean.Channel;

import java.util.List;


public interface NewsContract {

    interface View extends BaseView {

        void loadData(List<Channel> channels);
        /**
         * 初始化频道
         */
        void getChannel();
    }

}
