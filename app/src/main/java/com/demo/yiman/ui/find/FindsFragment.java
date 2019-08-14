package com.demo.yiman.ui.find;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.demo.yiman.R;
import com.demo.yiman.base.BaseFragment;
import com.demo.yiman.base.baseMVP.BasePresenter;
import com.demo.yiman.net.ApiConfig;
import com.demo.yiman.ui.SetActivity;
import com.demo.yiman.ui.WebViewActivity;
import com.demo.yiman.utils.AppConfig;
import com.demo.yiman.utils.SharePrefUtil;
import com.demo.yiman.utils.StatusBarUtil;

import butterknife.BindView;

public class FindsFragment extends BaseFragment {
    @BindView(R.id.tv_set)
    TextView mGoSet;

    @BindView(R.id.cl_first)
    ConstraintLayout mShop;
    @BindView(R.id.cl_second)
    ConstraintLayout mGame;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_title)
    TextView mTitle;
    @BindView(R.id.fake_status_bar)
    View mStatusBar;
    public static FindsFragment newInstance(){
        Bundle args = new Bundle();
        FindsFragment fragment = new FindsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //这只是针对6.0以上系统，没做过多适配
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_find;
    }

    @Override
    public void initView() {
        super.initView();
        initToolbar();
    }
    private void initToolbar() {
        AppCompatActivity mAppCompatActivity = (AppCompatActivity) mContext;
        mAppCompatActivity.setSupportActionBar(mToolbar);
        mToolbar.setTitle("");
        mToolbar.setBackgroundColor(getResources().getColor(R.color.bg_theme));
        mTitle.setTextColor(Color.BLACK);
        mTitle.setText(getResources().getString(R.string.title_find));

        ConstraintLayout.LayoutParams params=new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,getStateBarHeight(getActivity()));
        mStatusBar.setLayoutParams(params);

    }
    public static int getStateBarHeight(Activity a) {
        int result = 0;
        int resourceId = a.getResources().getIdentifier("status_bar_height",
                "dimen", "android");
        if (resourceId > 0) {
            result = a.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
    @Override
    public void initData() {
        super.initData();
        if (SharePrefUtil.getBoolean(AppConfig.ShopBtn)){
            mGoSet.setVisibility(View.GONE);
            mShop.setVisibility(View.VISIBLE);

        }else {
            mShop.setVisibility(View.GONE);
        }
        if (SharePrefUtil.getBoolean(AppConfig.JiCaiBtn)){
            mGoSet.setVisibility(View.GONE);
            mGame.setVisibility(View.VISIBLE);
        }else {
            mGame.setVisibility(View.GONE);
        }
        if (!SharePrefUtil.getBoolean(AppConfig.JiCaiBtn)&&!SharePrefUtil.getBoolean(AppConfig.ShopBtn)){
            showSet();
        }

    }
    private void showSet(){
        mGame.setVisibility(View.GONE);
        mShop.setVisibility(View.GONE);
        mGoSet.setVisibility(View.VISIBLE);
        mGoSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goSet();
            }
        });
    }

    private void goSet(){
        Intent intent = new Intent(mContext,SetActivity.class);
        startActivity(intent);
    }
    @Override
    public void bindView(View view, Bundle savedInstanceState) {
        super.bindView(view, savedInstanceState);
        initToolbar();
        mGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebViewActivity.launch(getActivity(), ApiConfig.JCUrl);
            }
        });
        mShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("只是来陪游戏的而已！！！！");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
