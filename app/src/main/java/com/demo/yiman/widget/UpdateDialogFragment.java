package com.demo.yiman.widget;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;

import com.demo.yiman.R;
import com.demo.yiman.base.BaseDialogFragment;
import com.demo.yiman.utils.ShowToast;

import butterknife.BindView;
import butterknife.OnClick;


public class UpdateDialogFragment extends BaseDialogFragment {
    @BindView(R.id.progress_update)
    CustomProgressBar mProgress;
    @BindView(R.id.rl_btn)
    RelativeLayout mRlBtn;

    Handler mHandler;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public static UpdateDialogFragment newInstance(){
        UpdateDialogFragment updateDialogFragment = new UpdateDialogFragment();
        return updateDialogFragment;
    }

    @Override
    protected Dialog createDialog() {
        Dialog dialog = new Dialog(getContext());
        if (dialog!=null){
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.comment_bg);
            dialog.getWindow().setWindowAnimations(R.style.diaog_animations);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(R.layout.dialog_update);
            // 设置宽度为屏宽, 靠近屏幕底部。
//            Window window = dialog.getWindow();
//            WindowManager.LayoutParams lp = window.getAttributes();
//            lp.windowAnimations = R.style.diaog_animations;
//            lp.gravity = Gravity.BOTTOM; // 紧贴底部
//            lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
//            window.setAttributes(lp);
        }

        return dialog;
    }

    @Override
    protected void initViewData() {
        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                mProgress.setProgress(msg.what);

                return false;
            }
        });
    }
    @OnClick({R.id.btn_cancel,R.id.btn_go})
    public void onClickView(View view){
        switch (view.getId()){
            case R.id.btn_cancel:
                dismiss();
                break;
            case R.id.btn_go:
                mRlBtn.setVisibility(View.GONE);
                mProgress.setVisibility(View.VISIBLE);
                mProgress.setProgress(0);
                //效果的实现
                initAchieve();
                break;
        }
    }

    private void initAchieve() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i =0 ; i<=100 ;i++){
                    mHandler.sendEmptyMessage(i*2);
                    try {
                        Thread.sleep(80);
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
                dismiss();
            }
        }).start();
    }


}
