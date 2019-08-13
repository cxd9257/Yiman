package com.demo.yiman.ui.joke;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.demo.yiman.R;
import com.demo.yiman.base.BaseFragment;
import com.demo.yiman.base.baseMVP.BasePresenter;
import com.demo.yiman.net.ApiConfig;
import com.demo.yiman.ui.adapter.JokeDefaultAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class JokeFragment extends BaseFragment {
    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;

    private JokeFragmentAdapter mAdapter;
    public static JokeFragment newInstance(){
        Bundle args = new Bundle();//进行传值 如 args.putString("id",id);
        JokeFragment fragment = new JokeFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_joke;
    }

    @Override
    public void initData() {
        super.initData();
        List<String> tabList = new ArrayList<>();
        tabList.add("推荐");
        tabList.add("视频");
        tabList.add("图片");
        tabList.add("文字");
        mAdapter = new JokeFragmentAdapter(getChildFragmentManager(),tabList);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(1);
        mTabLayout.setupWithViewPager(mViewPager,true);

    }

    public class JokeFragmentAdapter extends FragmentStatePagerAdapter{
        List<String> titles;

        public JokeFragmentAdapter(FragmentManager fm,List<String> titles){
            super(fm);
            this.titles = titles;
        }

        @Override
        public BaseFragment getItem(int i) {
            switch (i){
                case 0:
                    return JokeDetailFragment.newInstance(ApiConfig.TYPE_ALL,new JokeDefaultAdapter(getActivity(),null));//默认
                case 1:
                    return JokeDetailFragment.newInstance(ApiConfig.TYPE_VIDEO,new JokeDefaultAdapter(getActivity(),null));//默认
                case 2:
                    return JokeDetailFragment.newInstance(ApiConfig.TYPE_IMAGE,new JokeDefaultAdapter(getActivity(),null));//默认
                case 3:
                    return JokeDetailFragment.newInstance(ApiConfig.TYPE_TEXT,new JokeDefaultAdapter(getActivity(),null));//默认
            }
            return null;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return titles.size();
        }
    }




}
