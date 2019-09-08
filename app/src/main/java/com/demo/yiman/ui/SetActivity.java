package com.demo.yiman.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.demo.yiman.MainActivity;
import com.demo.yiman.R;
import com.demo.yiman.base.BaseActivity;
import com.demo.yiman.base.baseMVP.BasePresenter;
import com.demo.yiman.event.NightEvent;
import com.demo.yiman.utils.AppConfig;
import com.demo.yiman.utils.SharePrefUtil;
import com.demo.yiman.utils.ShowToast;
import com.tencent.bugly.beta.Beta;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

public class SetActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView mTitle;
    @BindView(R.id.tv_tool_right)
    TextView mTitleRight;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.sw_night)
    SwitchCompat mSwitchNight;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void restoreSaveInstanceState(Bundle savedInstanceState) {

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
                startActivity(new Intent(this, MainActivity.class));
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {//当返回按键被按下
            Bundle datain = new Bundle();
            datain.putInt("flag",1);
            Intent intent = new Intent(this,MainActivity.class);
            intent.putExtras(datain);
            startActivity(intent);
            finish();
        }
        return false;
    }

    @Override
    public void bindView(View view, Bundle savedInstanceState) {
        super.bindView(view, savedInstanceState);
        setStatusBarColor(Color.parseColor("#008577"), 0);
        if (SharePrefUtil.getBoolean("night")){
            mSwitchNight.setChecked(true);
        }else{
            mSwitchNight.setChecked(false);
        }

        mSwitchNight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    SharePrefUtil.putBoolean("night",true);
                    SharePrefUtil.commit();
                    recreate();
                    ShowToast.showShort(SetActivity.this,"夜间开启，重启软件生效");
                    EventBus.getDefault().post(new NightEvent(true));
                }else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    SharePrefUtil.putBoolean("night",false);
                    SharePrefUtil.commit();
                    recreate();
                    ShowToast.showShort(SetActivity.this,"夜间关闭，重启软件生效");
                    EventBus.getDefault().post(new NightEvent(false));
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
