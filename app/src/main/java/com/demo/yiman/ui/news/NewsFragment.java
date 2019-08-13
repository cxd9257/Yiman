package com.demo.yiman.ui.news;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.style.LineHeightSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.demo.yiman.R;
import com.demo.yiman.base.BaseFragment;
import com.demo.yiman.bean.Channel;
import com.demo.yiman.bean.NewTopBean;
import com.demo.yiman.ui.adapter.ChannelPagerAdapter;
import com.demo.yiman.ui.adapter.NewTopDataAdapter;
import com.demo.yiman.utils.ImageLoaderUtil;
import com.demo.yiman.utils.ScreenUtil;
import com.demo.yiman.utils.StatusBarUtil;
import com.demo.yiman.utils.ToolUtil;
import com.demo.yiman.widget.ChannelDialogFragment;
import com.demo.yiman.widget.CircleImageView;
import com.demo.yiman.widget.CustomNestedScrollView;
import com.demo.yiman.widget.CustomViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.OnClick;

public class NewsFragment extends BaseFragment<NewsPresenter> implements NewsView {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.viewPager)
    CustomViewPager mViewPager;
    @BindView(R.id.SlidingTabLayout)
    com.flyco.tablayout.SlidingTabLayout mSlidingTabLayout;
    @BindView(R.id.SlidingTabLayoutTitle)
    com.flyco.tablayout.SlidingTabLayout mSlidingTabLayoutTitle;
    @BindView(R.id.nes_scroll_view)
    CustomNestedScrollView mScrollView;
    @BindView(R.id.ll_search_title)
    RelativeLayout mRelativeLayoutSearch;
    @BindView(R.id.ll_xxxx)
    RelativeLayout mLinearLayoutXXXX;
    @BindView(R.id.gv_top_content)
    GridView mGridViewNewTop;
    @BindView(R.id.ib_add)
    ImageButton mImageAdd;
    @BindView(R.id.ib_add_title)
    ImageButton mImageAddTitle;
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
    public void bindView(View view, Bundle savedInstanceState) {
        super.bindView(view, savedInstanceState);
        mToolbar.setTitle("38℃");
        mToolbar.setTitleTextColor(Color.WHITE);
        AppCompatActivity mAppCompatActivity = (AppCompatActivity) mContext;
        mAppCompatActivity.setSupportActionBar(mToolbar);
        mToolbar.post(new Runnable() {
            @Override
            public void run() {
                dealWithViewPager();
            }
        });
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

//                if (lastScrollY < h) {
//                    scrollY = Math.min(h, scrollY);
//                    mScrollY = scrollY > h ? h : scrollY;
//                    buttonBarLayout.setAlpha(1f * mScrollY / h);
//                    toolbar.setBackgroundColor(((255 * mScrollY / h) << 24) | color);
//                    ivHeader.setTranslationY(mOffset - mScrollY);
//                }
//                if (scrollY == 0) {
//                    ivBack.setImageResource(R.drawable.back_white);
//                    ivMenu.setImageResource(R.drawable.icon_menu_white);
//                } else {
//                    ivBack.setImageResource(R.drawable.back_black);
//                    ivMenu.setImageResource(R.drawable.icon_menu_black);
//                }

                lastScrollY = scrollY;
            }
        });
        //mToolbar.setBackgroundColor(0);
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

    @Override
    public void initData() {
        super.initData();
        //获取数据
        //mPresenter.getBaseView();

        initTopData();
        mNewTopDataAdapter = new NewTopDataAdapter(mContext,mNewTopData);
        mGridViewNewTop.setAdapter(mNewTopDataAdapter);
        mSelectedData = new ArrayList<>();
        mUnSelectedData = new ArrayList<>();
        mPresenter.getChannel();
    }

    @Override
    protected NewsPresenter createPresenter() {
        return new NewsPresenter(this);
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
            mChannelPagerAdapter = new ChannelPagerAdapter(getChildFragmentManager(),channels);
            mViewPager.setAdapter(mChannelPagerAdapter);
            mViewPager.setOffscreenPageLimit(2);
            mViewPager.setCurrentItem(0,false);
            mSlidingTabLayout.setViewPager(mViewPager);
            mSlidingTabLayoutTitle.setViewPager(mViewPager);
        }
    }



    @Override
    public void getChannel() {

    }

    private void dealWithViewPager() {
        //+ ToolUtil.getStateBarHeight(getActivity())
        toolBarPositionY = mToolbar.getHeight()+ ToolUtil.getStateBarHeight(getActivity());
        ViewGroup.LayoutParams params = mViewPager.getLayoutParams();
        params.height = ScreenUtil.getScreenHeightPx(mContext)-toolBarPositionY-mSlidingTabLayout.getHeight()-ToolUtil.getStateBarHeight(getActivity())+1;
        mViewPager.setLayoutParams(params);
        Log.e("ViewPager高",params.height+"");
    }
/**
 * 模拟获取头部目录
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
                ChannelDialogFragment channelDialogFragment =  ChannelDialogFragment.newInstance(mSelectedData,mUnSelectedData);
                channelDialogFragment.show(getChildFragmentManager(),"CHANNEL");
                break;
            case R.id.ib_add_title:
                ChannelDialogFragment channelDialogFragment1 =  ChannelDialogFragment.newInstance(mSelectedData,mUnSelectedData);
                channelDialogFragment1.show(getChildFragmentManager(),"CHANNEL");
                break;
        }
    }

}
