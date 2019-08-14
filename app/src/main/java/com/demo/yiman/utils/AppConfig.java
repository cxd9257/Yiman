package com.demo.yiman.utils;

import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class AppConfig {
    public static final String JiCaiBtn = "jicaibtn";
    public static final String ShopBtn = "shopbtn";

    public static final String share_pre_name = "name";
    public static final String BUGLY_KEY = "99af23e8-8149-470b-8eba-55ad493b72e1";
    public static final String DB_NAME = "yiman.db";

    public static void setWebView(WebView webView){
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setUseWideViewPort(false);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//LOAD_CACHE_ELSE_NETWORK模式下，无论是否有网络，只要本地有缓存，都使用缓存。本地没有缓存时才从网络上获取
        webView.getSettings().setDomStorageEnabled(true);
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return true;
            }
        });
    }
}
