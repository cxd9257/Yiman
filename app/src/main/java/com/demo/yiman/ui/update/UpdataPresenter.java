package com.demo.yiman.ui.update;

import android.util.Log;

import com.demo.yiman.base.baseMVP.BaseModel;
import com.demo.yiman.base.baseMVP.BasePresenter;
import com.demo.yiman.utils.DownloadUtils;



import java.io.File;

public class UpdataPresenter extends BasePresenter<Contract.View> {
    public UpdataPresenter(Contract.View baseView) {
        super(baseView);
    }
    public void download(String url,JsDownloadListener jsDownloadListener) {
        DownloadUtils downloadUtils = new DownloadUtils(url,jsDownloadListener);

        downloadUtils.downLoad(url,jsDownloadListener);
    }
}
