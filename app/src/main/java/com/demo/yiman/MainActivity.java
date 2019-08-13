package com.demo.yiman;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.view.View;

import com.demo.yiman.base.BaseActivity;
import com.demo.yiman.base.SupportFragment;
import com.demo.yiman.base.baseMVP.BasePresenter;
import com.demo.yiman.ui.about.AboutFragment;
import com.demo.yiman.ui.find.FindsFragment;
import com.demo.yiman.ui.image.ImageFragment;
import com.demo.yiman.ui.joke.JokeFragment;
import com.demo.yiman.ui.news.NewsFragment;
import com.demo.yiman.utils.StatusBarUtil;


import butterknife.BindView;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

public class MainActivity extends BaseActivity {
    private int lastIndex;
    private SupportFragment[] mFragment = new SupportFragment[5];
    @BindView(R.id.nav_view)
    BottomNavigationView navView;
    @Override
    public void initView() {
        super.initView();


    }

    @Override
    public void initData() {
        super.initData();
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
        setFragmentPosition(0);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_new:
                    setFragmentPosition(0);
                    break;
                case R.id.navigation_video:
                    setFragmentPosition(1);
                    break;
                case R.id.navigation_jc:
                    setFragmentPosition(2);
                    break;
                case R.id.navigation_joke:
                    setFragmentPosition(3);
                    break;
                case R.id.navigation_me:
                    setFragmentPosition(4);

                    break;
            }
            return true;
        }
    };
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
    protected BasePresenter createPresenter() {
        return null;
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
    public int getLayoutId() {
        return R.layout.activity_main;
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

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }
}
