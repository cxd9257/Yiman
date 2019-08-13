package com.demo.yiman;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo.yiman.base.BaseActivity;
import com.demo.yiman.base.baseMVP.BasePresenter;
import com.demo.yiman.utils.AppConfig;
import com.demo.yiman.utils.ImageLoaderUtil;
import com.demo.yiman.utils.StatusBarUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;

public class SplashActivity extends BaseActivity {
    @BindView(R.id.iv_wallpaper)
    ImageView ivWallpaper;
    @BindView(R.id.tv_time_date)
    TextView tvTime;
    @BindView(R.id.tv_context)
    TextView tvContext;
    @BindView(R.id.tv_skip)
    TextView tvSkip;
    @BindView(R.id.fl_wallpaper)
    FrameLayout flWallpaper;
    CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void bindView(View view, Bundle savedInstanceState) {
        super.bindView(view, savedInstanceState);
        StatusBarUtil.setTranslucentForImageView(this,0 , null);
        getDate();
        ImageLoaderUtil.LoadImage(this,"http://api.dujin.org/bing/1920.php",ivWallpaper);
        mCompositeDisposable.add(countDown(3).doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                tvSkip.setText("跳过 4");
            }
        }).subscribeWith(new DisposableObserver<Integer>() {
            @Override
            public void onNext(Integer integer) {
                tvSkip.setText("跳过"+(integer+1));
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                toMainActivity();
            }
        }));
    }
    private void toMainActivity(){
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
        }
        if (isFirstStart()) {
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();

        }else{
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }

    }
    /**
     * 检测是否第一次启动
     * **/
    private boolean isFirstStart() {
        SharedPreferences sp = getSharedPreferences(getPackageName(),
                Context.MODE_PRIVATE);
        if (sp.getBoolean("isFristLoad", true)) {
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("isFristLoad", false);
            editor.putBoolean(AppConfig.JiCaiBtn,false);
            editor.commit();
            return true;
        } else {
            return false;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
        }
    }
    private void getDate(){
        long time=System.currentTimeMillis();
        Date date=new Date(time);
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat format2 = new SimpleDateFormat("EEEE");
        String a = format1.format(date);
        String b = format2.format(date);
        tvTime.setText(b+" "+a);
        tvContext.setText("原谅自己没有做成功的事");
    }
    public Observable<Integer> countDown(int time){
        if (time<0) time = 0;
        final int countTime = time;
        return Observable.interval(0,1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Long, Integer>() {
                    @Override
                    public Integer apply(Long aLong) throws Exception {
                        return countTime - aLong.intValue();
                    }
                })
                .take(countTime+1);
    }
    @OnClick(R.id.fl_wallpaper)
    public void onViewClick(){
        toMainActivity();
    }
}
