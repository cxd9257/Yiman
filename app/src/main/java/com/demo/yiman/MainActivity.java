package com.demo.yiman;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.demo.yiman.base.BaseActivity;
import com.demo.yiman.base.SupportFragment;
import com.demo.yiman.base.baseMVP.BasePresenter;
import com.demo.yiman.event.NewChannelEvent;
import com.demo.yiman.event.NightEvent;
import com.demo.yiman.ui.about.AboutFragment;
import com.demo.yiman.ui.find.FindsFragment;
import com.demo.yiman.ui.image.ImageFragment;
import com.demo.yiman.ui.joke.JokeFragment;
import com.demo.yiman.ui.news.NewsFragment;
import com.demo.yiman.utils.StatusBarUtil;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

public class MainActivity extends BaseActivity {
    private int lastIndex;
    private int endIndex;
    private SupportFragment[] mFragment = new SupportFragment[5];
    @BindView(R.id.nav_view)
    BottomNavigationView navView;

    @Override
    protected void onStart() {
        super.onStart();
//        if (!EventBus.getDefault().isRegistered(this)) {
//            EventBus.getDefault().register(this);
//        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }
    @Override
    public void bindView(View view, Bundle savedInstanceState) {
        StatusBarUtil.setTranslucentForImageViewInFragment(MainActivity.this,0,null);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        if (savedInstanceState == null){
            mFragment[0] = NewsFragment.newInstance();
            mFragment[1] = ImageFragment.newInstance();
            mFragment[2] = FindsFragment.newInstance();
            mFragment[3] = JokeFragment.newInstance();
            mFragment[4] = AboutFragment.newInstance();
            getSupportDelegate().loadMultipleRootFragment(R.id.contentContainer,0,
                    mFragment[0],
                    mFragment[1],
                    mFragment[2],
                    mFragment[3],
                    mFragment[4]);
        }else{
            mFragment[0] = findFragment(NewsFragment.class);
            mFragment[1] = findFragment(ImageFragment.class);
            mFragment[2] = findFragment(FindsFragment.class);
            mFragment[3] = findFragment(JokeFragment.class);
            mFragment[4] = findFragment(AboutFragment.class);
        }
        Intent intent = getIntent();
        try{
            if (intent.getExtras() != null){
                if (intent.getExtras().getInt("flag") == 1){
                    setFragmentPosition(4);
                    endIndex = 4;
                    navView.setSelectedItemId(R.id.navigation_me);
                }
            }else{
                setFragmentPosition(0);
                endIndex = 0;
            }
        }catch (NullPointerException ex){
            ex.printStackTrace();
        }

        com.jaeger.library.StatusBarUtil.setDarkMode(MainActivity.this);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_new:
                    setFragmentPosition(0);
                    com.jaeger.library.StatusBarUtil.setDarkMode(MainActivity.this);
                    if (endIndex == 0){
                        Intent intent = new Intent("updata");
                        intent.putExtra("refresh","yes");
                        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                    }
                    endIndex =0;
                    break;
                case R.id.navigation_video:
                    setFragmentPosition(1);
                    endIndex =1;
                    com.jaeger.library.StatusBarUtil.setDarkMode(MainActivity.this);
                    break;
                case R.id.navigation_jc:
                    setFragmentPosition(2);
                    endIndex =2;
                    com.jaeger.library.StatusBarUtil.setLightMode(MainActivity.this);
                    break;
                case R.id.navigation_joke:
                    setFragmentPosition(3);
                    endIndex =3;
                    com.jaeger.library.StatusBarUtil.setDarkMode(MainActivity.this);
                    break;
                case R.id.navigation_me:
                    setFragmentPosition(4);
                    endIndex =4;
                    com.jaeger.library.StatusBarUtil.setLightMode(MainActivity.this);
                    break;
            }
            return true;
        }
    };

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void restoreSaveInstanceState(Bundle savedInstanceState) {
        //setFragmentPosition(endIndex);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putString("end",endIndex+"");
    }

    private void setFragmentPosition(int position){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment currentFragment = mFragment[position];
        Fragment lastFragment = mFragment[lastIndex];
        lastIndex = position;
        ft.hide(lastFragment);
        if (!currentFragment.isAdded()){
            getSupportFragmentManager().beginTransaction().remove(currentFragment).commit();
            ft.add(R.id.contentContainer,currentFragment);
        }
        ft.show(currentFragment);
        ft.commitAllowingStateLoss();
    }

    @Override
    public void initView() {
        super.initView();
    }
    
    @Override
    public void initData() {
        super.initData();
    }

    @Override
    public void onBackPressedSupport() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressedSupport();
    }

    @Override
    public boolean isSupportSwipeBack() {
        return false;
    }
//    @Subscribe(threadMode = ThreadMode.MAIN)  //EventBus事件
//    public void nightEvent(NightEvent event){
//        Log.e("ssss",event.aBoolean+"");
//        if (event.aBoolean == true){
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//        }else{
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//        }
//        //recreate();
//    }
    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    protected void onDestroy() {
       // EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
