package com.demo.yiman.ui.news;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.demo.yiman.R;
import com.demo.yiman.base.BaseActivity;
import com.demo.yiman.base.baseMVP.BasePresenter;

import butterknife.BindView;
import q.rorbin.badgeview.QBadgeView;

public class NewsDetailActivity extends BaseActivity {
    @BindView(R.id.news_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.news_progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.news_webView)
    WebView mWebView;
    @BindView(R.id.iv_sum_comment)
    ImageView mSumComment;


    private static final String Url="url";
    private static final String Source="source";
    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_news_detail_web;
    }
    public static void launch(Context context, String url,String source){
        Intent intent = new Intent(context,NewsDetailActivity.class);
        intent.putExtra(Url,url);
        intent.putExtra(Source,source);
        context.startActivity(intent);
    }

    @Override
    public void bindView(View view, Bundle savedInstanceState) {
        super.bindView(view, savedInstanceState);
        setWebView(mWebView);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (mProgressBar == null){
                    return;
                }
                if (newProgress == 100){
                    mProgressBar.setVisibility(View.GONE);
                }
                else {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mProgressBar.setProgress(newProgress);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void initData() {
        super.initData();
        if (getIntent() == null){
            return;
        }
        String url = getIntent().getStringExtra(Url);
        String source = getIntent().getStringExtra(Source);
        if (!TextUtils.isEmpty(url)){
            mWebView.loadUrl(url);
            if (!TextUtils.isEmpty(source)){
                mToolbar.setTitle(source);
            }
        }
        new QBadgeView(NewsDetailActivity.this).bindTarget(mSumComment).setBadgeGravity(Gravity.END|Gravity.TOP).setBadgeNumber(12).setBadgeBackgroundColor(Color.WHITE).setBadgeTextColor(Color.BLACK).setBadgeTextSize(10,true).setShowShadow(false).setGravityOffset(0,-8,true);
    }

    /**
     * WebView 通用设置
     */
    private void setWebView(WebView webView){
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearHistory();
            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.destroy();
            mWebView = null;
        }

    }
}
