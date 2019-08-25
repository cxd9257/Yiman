package com.demo.yiman.base;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.demo.yiman.MyApp;
import com.demo.yiman.base.baseMVP.BaseModel;
import com.demo.yiman.base.baseMVP.BasePresenter;
import com.demo.yiman.base.baseMVP.BaseView;
import com.demo.yiman.utils.ShowToast;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment<P extends BasePresenter> extends SupportFragment implements BaseView {
    protected Context mContext;
    protected View mRootView;
    private ProgressDialog dialog;
    private Dialog mLoadingDialog = null;
    Unbinder unbinder;

    protected P mPresenter;
    protected abstract P createPresenter();
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(getLayoutId(),container,false);
        mPresenter = createPresenter();
        unbinder = ButterKnife.bind(this,view);

        return view;
    };
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        if (mRootView != null){
            ViewGroup parent = (ViewGroup) mRootView.getParent();
            if (parent !=null){
                parent.removeView(mRootView);
            }
        }else {
            mRootView = createView(inflater,container,savedInstanceState);
        }
        mContext = mRootView.getContext();
        return mRootView;
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        attachView();
        bindView(view,savedInstanceState);
        initView();
    }

    public void bindView(View view, Bundle savedInstanceState) {
    }

    private void attachView() {
        if (mPresenter != null){
            //mPresenter.addDisposable(this);
        }

    }
    public void onrRetry(){

    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initData();
    }
    public void initView(){

    }
    public void initData() {
    }

    @Nullable
    @Override
    public View getView() {
        return mRootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null){
            mPresenter.detachView();
        }
        try {
            if (unbinder != null)
                unbinder.unbind();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
    }

    @Override
    public void showLoading() {
        if (dialog == null){
            dialog = new ProgressDialog(mContext);
        }
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    public void hideLoading() {
        if (dialog!=null && dialog.isShowing()){
            dialog.dismiss();
        }
    }
    protected void ShowToast(String string){
        ShowToast.showShort(MyApp.getContext(),string);
    }
    private  static Toast mToast;
    public void showToast(String msg){
        if (TextUtils.isEmpty(msg)){
            return;
        }
        if (mToast == null){
            mToast = Toast.makeText(mContext,msg,Toast.LENGTH_SHORT);
        }else{
            mToast.setText(msg);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();

    }

    @Override
    public void showError(String msg) {
        showToast(msg);
    }

    @Override
    public void onErrorCode(BaseModel model) {

    }
}
