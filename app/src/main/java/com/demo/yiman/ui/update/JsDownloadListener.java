package com.demo.yiman.ui.update;

public interface JsDownloadListener{
    void onStartDownload(long length);
    void onProgress(int progress);
    void onFail(String errorInfo);
    void onSuccess();

}
