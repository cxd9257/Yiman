package com.demo.yiman.base.baseMVP;

public interface BaseView {
    /**
     * 显示dialog
     */
    void showLoading();
    /**
     * 隐藏dialog
     */
    void hideLoading();
    /**
     * 显示错误
     * @param msg
     */
    void showError(String msg);
    /**
     * 错误码code
     */



    void onErrorCode(BaseModel model);
    int getLayoutId();
}
