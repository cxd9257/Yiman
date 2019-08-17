package com.demo.yiman.ui.news;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.demo.yiman.R;
import com.demo.yiman.base.BaseFragment;
import com.demo.yiman.bean.Channel;
import com.demo.yiman.bean.NewTopBean;
import com.demo.yiman.bean.NewsWeather;
import com.demo.yiman.database.ChannelDao;
import com.demo.yiman.event.NewChannelEvent;
import com.demo.yiman.event.SelectChannelEvent;
import com.demo.yiman.ui.adapter.ChannelPagerAdapter;
import com.demo.yiman.ui.adapter.NewTopDataAdapter;
import com.demo.yiman.utils.ScreenUtil;
import com.demo.yiman.utils.ToolUtil;
import com.demo.yiman.widget.ChannelDialogFragment;
import com.demo.yiman.widget.CustomNestedScrollView;
import com.demo.yiman.widget.CustomViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;

public class NewsFragment extends BaseFragment<NewsChannelPresenter> implements NewsView {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.viewPager)
    CustomViewPager mViewPager;
    @BindView(R.id.SlidingTabLayout)
    com.flyco.tablayout.SlidingTabLayout mSlidingTabLayout;
    @BindView(R.id.SlidingTabLayoutTitle)//用于划动隐藏显示指示器
    com.flyco.tablayout.SlidingTabLayout mSlidingTabLayoutTitle;
    @BindView(R.id.nes_scroll_view)
    CustomNestedScrollView mScrollView;
    @BindView(R.id.gv_top_content)
    GridView mGridViewNewTop;
    @BindView(R.id.ib_add)
    ImageButton mImageAdd;
    @BindView(R.id.ib_add_title)
    ImageButton mImageAddTitle;

    @BindView(R.id.tv_temperature)
    TextView mTemperature;
    @BindView(R.id.tv_city)
    TextView mCity;
    @BindView(R.id.tv_info)
    TextView mInfo;
    int toolBarPositionY=0;
    private List<Channel> mSelectedData;
    private List<Channel> mUnSelectedData;
    private List<NewTopBean> mNewTopData;
    private ChannelPagerAdapter mChannelPagerAdapter;
    private NewTopDataAdapter mNewTopDataAdapter;
    private int selectedIndex;
    private String selectedChannel;
    public static NewsFragment newInstance(){
        Bundle args = new Bundle();
        NewsFragment fragment = new NewsFragment();
        fragment.setArguments(args);
        return fragment;
    }
    public int getLayoutId() {
        return R.layout.fragment_news;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void bindView(View view, Bundle savedInstanceState) {
        super.bindView(view, savedInstanceState);
        initToolbar(); //暂时没有对Toolbar进行封装
        mScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            int lastScrollY=0;
            int h = ToolUtil.dip2px(200,mContext);
            int color = ContextCompat.getColor(mContext,R.color.colorPrimary) & 0x00ffffff;
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int[] location = new int[2];
                mSlidingTabLayout.getLocationOnScreen(location);
                int yPositioin = location[1];
                if (yPositioin< toolBarPositionY){
                    mSlidingTabLayoutTitle.setVisibility(View.VISIBLE);
                    mImageAddTitle.setVisibility(View.VISIBLE);
                    mScrollView.setNeedScroll(false);
                }else {
                    mSlidingTabLayoutTitle.setVisibility(View.GONE);
                    mImageAddTitle.setVisibility(View.GONE);
                    mScrollView.setNeedScroll(true);
                }
                lastScrollY = scrollY;
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }
            @Override
            public void onPageSelected(int i) {
                selectedIndex = i;
                selectedChannel = mSelectedData.get(i).getChannelName();
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }

    private void initToolbar() {
        mToolbar.setTitle("38℃");//天气度数
        mToolbar.setTitleTextColor(Color.WHITE);
        AppCompatActivity mAppCompatActivity = (AppCompatActivity) mContext;
        mAppCompatActivity.setSupportActionBar(mToolbar);
        mToolbar.post(new Runnable() {
            @Override
            public void run() {
                dealWithViewPager();
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        initTopData();
        mNewTopDataAdapter = new NewTopDataAdapter(mContext,mNewTopData);
        mGridViewNewTop.setAdapter(mNewTopDataAdapter);
        mSelectedData = new ArrayList<>();
        mUnSelectedData = new ArrayList<>();
        mPresenter.getChannel();
        mPresenter.getNewsWeather("广州");
    }

    @Override
    protected NewsChannelPresenter createPresenter() {
        return new NewsChannelPresenter(this);
    }

    @Override
    public void onNewsSucc() {

    }

    @Override
    public void loadData(List<Channel> channels, List<Channel> otherChannels) {
        if (channels != null){
            mSelectedData.clear();
            mSelectedData.addAll(channels);
            mUnSelectedData.clear();
            mUnSelectedData.addAll(otherChannels);
            mChannelPagerAdapter = new ChannelPagerAdapter(getFragmentManager(),channels);
            mViewPager.setAdapter(mChannelPagerAdapter);
            mViewPager.setOffscreenPageLimit(2);
            mViewPager.setCurrentItem(0,false);
            mSlidingTabLayout.setViewPager(mViewPager);
            mSlidingTabLayoutTitle.setViewPager(mViewPager);
        }else{
            ShowToast("数据异常");
        }

    }
    @Subscribe(threadMode = ThreadMode.MAIN)  //EventBus事件
    public void updateChannel(NewChannelEvent event){
        if (event==null)return;
        if (event.selectedDatas !=null && event.unSelectedDatas != null){
            mSelectedData = event.selectedDatas;
            mUnSelectedData = event.unSelectedDatas;
            mChannelPagerAdapter.updataChannel(mSelectedData);
            mSlidingTabLayout.notifyDataSetChanged();
            ChannelDao.saveChannels(event.allChannels);  //拿到新数据后让指示器改变，并保存新数据

            List<String> integers = new ArrayList<>();
            for (Channel channel : mSelectedData) {
                integers.add(channel.getChannelName());
            }
            if (TextUtils.isEmpty(event.firstChannelName)) {
                if (!integers.contains(selectedChannel)) {
                    selectedChannel = mSelectedData.get(selectedIndex).getChannelName();
                    mViewPager.setCurrentItem(selectedIndex, false);
                } else {
                    setViewpagerPosition(integers, selectedChannel);
                }
            } else {
                setViewpagerPosition(integers, event.firstChannelName);
            }
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void selectChannelEvent(SelectChannelEvent selectChannelEvent) {
        if (selectChannelEvent == null) return;
        List<String> integers = new ArrayList<>();

        for (Channel channel : mSelectedData) {
            integers.add(channel.getChannelName());
        }
        setViewpagerPosition(integers, selectChannelEvent.channelName);
    }
    private void setViewpagerPosition(List<String> integers,String channelName){
        if (TextUtils.isEmpty(channelName)||integers == null)return;
        for (int j=0; j<integers.size(); j++){
            if (integers.get(j).equals(channelName)){
                selectedChannel = integers.get(j);
                selectedIndex = j;
                break;
            }
        }
        mViewPager.postDelayed(new Runnable() {
            @Override
            public void run() {
                mViewPager.setCurrentItem(selectedIndex,false);
            }
        },100);
    }

    @Override
    public void getChannel() {

    }

    @Override
    public void loaWeatherdData(NewsWeather newsWeatherModle) {
        if (newsWeatherModle !=null){
            mTemperature.setText(newsWeatherModle.getResult().getRealtime().getTemperature()+"°");
            mCity.setText(newsWeatherModle.getResult().getCity()+" "+newsWeatherModle.getResult().getRealtime().getDirect());
            mInfo.setText(newsWeatherModle.getResult().getRealtime().getInfo());
        }
    }

    private void dealWithViewPager() {
        toolBarPositionY = mToolbar.getHeight()+ ToolUtil.getStateBarHeight(getActivity());
        ViewGroup.LayoutParams params = mViewPager.getLayoutParams();
        params.height = ScreenUtil.getScreenHeightPx(mContext)-toolBarPositionY-mSlidingTabLayout.getHeight()-ToolUtil.getStateBarHeight(getActivity())+1;
        mViewPager.setLayoutParams(params);
    }
    /**
     * 获取头部目录，图片地址是自己搭建的
     */
    private void initTopData(){
        mNewTopData = new ArrayList<>();
        String[] nameList = new String[]{"直播","小说","漫画","想听FM","游戏","百度",
                            "体育","彩票","交友","提问","天气","更多"};
        String[] imageList = new String[]{"http://120.78.221.95/Resource/wx/ic_news_live.png",
                                          "http://120.78.221.95/Resource/wx/ic_news_fiction.png",
                                          "http://120.78.221.95/Resource/wx/ic_news_cartoon.png",
                                          "http://120.78.221.95/Resource/wx/ic_news_fm.png",
                                          "http://120.78.221.95/Resource/wx/ic_news_game.png",
                                          "http://120.78.221.95/Resource/wx/ic_news_baidu.png",
                                          "http://120.78.221.95/Resource/wx/ic_news_sports.png",
                                          "http://120.78.221.95/Resource/wx/ic_news_lottery.png",
                                          "http://120.78.221.95/Resource/wx/ic_news_jy.png",
                                          "http://120.78.221.95/Resource/wx/ic_news_question.png",
                                          "http://120.78.221.95/Resource/wx/ic_news_weather.png",
                                          "http://120.78.221.95/Resource/wx/ic_news_util.png"};
        for (int i= 0;i<nameList.length;i++){
            NewTopBean newTopBean = new NewTopBean();
            newTopBean.setTitle(nameList[i]);
            newTopBean.setImageUrl(imageList[i]);
            mNewTopData.add(newTopBean);
        }
    }

    @OnClick({R.id.ib_add,R.id.ib_add_title})
    public void onViewClicked(View view){
        switch (view.getId()){
            case R.id.ib_add:
                startChannelDialog();
                break;
            case R.id.ib_add_title:
                startChannelDialog();
                break;
        }
    }
    private void startChannelDialog(){
        ChannelDialogFragment channelDialogFragment =  ChannelDialogFragment.newInstance(mSelectedData,mUnSelectedData);
        channelDialogFragment.show(getChildFragmentManager(),"CHANNEL");
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }
}
