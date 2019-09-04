package com.demo.yiman.ui.update;

import com.demo.yiman.base.baseMVP.BasePresenter;
import com.demo.yiman.utils.DownloadUtils;



public class UpdataPresenter extends BasePresenter<Contract.View> {
    public UpdataPresenter(Contract.View baseView) {
        super(baseView);
    }
    public void download(String url,JsDownloadListener jsDownloadListener) {
        DownloadUtils downloadUtils = new DownloadUtils(url,jsDownloadListener);

        downloadUtils.downLoad(url,jsDownloadListener);
    }
}
