package com.demo.yiman.ui.image;

import com.demo.yiman.base.baseMVP.BaseObserver;
import com.demo.yiman.base.baseMVP.BasePresenter;
import com.demo.yiman.bean.ImageModle;

class ImagePresenter extends BasePresenter<ImageView> {
    public ImagePresenter(ImageView baseView) {
        super(baseView);
    }

    public void getImageData(final int count , final int page){
        addDisposable(apiServer.getImageDate(count, page), new BaseObserver<ImageModle>(baseView) {

            @Override
            protected void onSuccess(ImageModle iamgeModle) {
                if (page>1){
                    baseView.loadMoreData(iamgeModle);
                }else{
                    baseView.onImageSucc(iamgeModle);
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
