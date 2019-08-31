package com.demo.yiman.ui.about;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.demo.yiman.R;
import com.demo.yiman.base.BaseFragment;
import com.demo.yiman.base.baseMVP.BasePresenter;
import com.demo.yiman.ui.SetActivity;
import com.demo.yiman.widget.RadarData;
import com.demo.yiman.widget.RadarView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class AboutFragment extends BaseFragment {
    @BindView(R.id.radarView)
    RadarView mRadarView;
    @BindView(R.id.tv_title)
    TextView mTitle;
    @BindView(R.id.tv_tool_right)
    TextView mTitleRight;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tvBlogUrl)
    TextView mBlog;
    @BindView(R.id.cl_first)
    ConstraintLayout mCl;

    public static AboutFragment newInstance(){
        Bundle args = new Bundle();
        AboutFragment fragment = new AboutFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
    @Override
    public int getLayoutId() {
        return R.layout.fragment_about;
    }
    @Override
    public void bindView(View view, Bundle savedInstanceState) {
        super.bindView(view, savedInstanceState);
        initToolbar();
        mTitleRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, SetActivity.class));
            }
        });
        mBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("长按复制");
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        getData();
        mRadarView.setData(getData(),12);
        mRadarView.setNum(6);

    }

    @Override
    public void initView() {
        super.initView();
        initToolbar();
    }

    public void initToolbar() {
        AppCompatActivity mAppCompatActivity = (AppCompatActivity) mContext;
        mAppCompatActivity.setSupportActionBar(mToolbar);
        mToolbar.setTitle("");
        mToolbar.setBackgroundColor(getResources().getColor(R.color.transparent));
        mTitle.setText(getResources().getString(R.string.about));
        mTitleRight.setText(getResources().getString(R.string.set));

    }
    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    private List<RadarData> getData(){
        List<RadarData> data = new ArrayList<>();
        data.add(new RadarData("android",10));
        data.add(new RadarData(".net",7));
        data.add(new RadarData("java",8));
        data.add(new RadarData("前端",5));
        data.add(new RadarData("ps",6));
        data.add(new RadarData("pr",5));
        return data;
    }
}
