package com.demo.yiman.ui.image;

import com.demo.yiman.base.baseMVP.BaseView;
import com.demo.yiman.bean.ImageModle;

public interface ImageView extends BaseView {
    void onImageSucc(ImageModle iamgeModle);
    void loadMoreData(ImageModle imageModle);
}
