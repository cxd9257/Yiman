package com.demo.yiman.ui.update;

import com.demo.yiman.base.baseMVP.BaseView;

public interface Contract extends BaseView {
    interface View extends BaseView {
        void showError(String s);
        void showUpdate();
        void downLoading(int i);
        void downSuccess();
        void downFial();
        void setMax(long l);
    }
    interface Presenter extends BaseView {
        void getApkInfo();
        void downFile(String url);
    }
}
