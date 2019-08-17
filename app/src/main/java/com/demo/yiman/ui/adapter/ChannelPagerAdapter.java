package com.demo.yiman.ui.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


import com.demo.yiman.base.BaseFragment;
import com.demo.yiman.bean.Channel;
import com.demo.yiman.ui.news.DetailFragment;

import java.util.List;

public class ChannelPagerAdapter extends FragmentStatePagerAdapter {
    private List<Channel> mChannels;
    public ChannelPagerAdapter(FragmentManager fm, List<Channel> channels) {
        super(fm);
        this.mChannels = channels;
    }
    public void updataChannel(List<Channel> channels){
        this.mChannels = channels;
        notifyDataSetChanged();
    }
    @Override
    public BaseFragment getItem(int i) {
        return DetailFragment.newInstance(mChannels.get(i).getChannelId(),i);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mChannels.get(position).getChannelName();
    }

    @Override
    public int getCount() {
        return mChannels!=null? mChannels.size():0;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
