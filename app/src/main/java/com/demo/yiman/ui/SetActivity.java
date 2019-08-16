package com.demo.yiman.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.demo.yiman.R;
import com.demo.yiman.base.BaseActivity;
import com.demo.yiman.base.baseMVP.BasePresenter;
import com.demo.yiman.utils.AppConfig;
import com.demo.yiman.utils.SharePrefUtil;
import com.tencent.bugly.beta.Beta;

import butterknife.BindView;
import butterknife.OnClick;

public class SetActivity extends BaseActivity {
    @BindView(R.id.sw_shop)
    Switch mShop;
    @BindView(R.id.sw_game)
    Switch mGame;
    @BindView(R.id.tv_title)
    TextView mTitle;
    @BindView(R.id.tv_tool_right)
    TextView mTitleRight;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_set;
    }

    @Override
    public void initView() {
        super.initView();
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        setTitle(getResources().getString(R.string.set_btn));
        mToolbar.setTitle("");
        setTitleBack(true);
        SwichState();
    }
    private void SwichState(){
        if (SharePrefUtil.getBoolean(AppConfig.JiCaiBtn)){
            mGame.setChecked(true);
        }
        if (SharePrefUtil.getBoolean(AppConfig.ShopBtn)){
            mShop.setChecked(true);
        }
        mShop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    SharePrefUtil.putBoolean(AppConfig.ShopBtn,true);
                    SharePrefUtil.commit();
                }else{
                    SharePrefUtil.putBoolean(AppConfig.ShopBtn,false);
                    SharePrefUtil.commit();
                }
            }
        });
        mGame.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    SharePrefUtil.putBoolean(AppConfig.JiCaiBtn,true);
                    SharePrefUtil.commit();
                }else{
                    SharePrefUtil.putBoolean(AppConfig.JiCaiBtn,false);
                    SharePrefUtil.commit();
                }
            }
        });

    }


    protected void setTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            mTitle.setText(title);
        }
    }

    protected void setTitleRight(String title) {
        if (!TextUtils.isEmpty(title)) {
            mTitleRight.setText(title);
        }
    }
    /**
     * 设置显示返回按钮
     */
    protected void setTitleBack(boolean visible) {
        if (visible) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            this.getSupportActionBar().setDisplayShowTitleEnabled(false);

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        };
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void initData() {
        super.initData();

    }

    @Override
    public void bindView(View view, Bundle savedInstanceState) {
        super.bindView(view, savedInstanceState);
        setStatusBarColor(Color.parseColor("#008577"), 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @OnClick(R.id.btn_update)
    public void onClick(){
        Beta.checkUpgrade();
    }

}
