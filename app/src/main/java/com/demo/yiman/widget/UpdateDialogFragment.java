package com.demo.yiman.widget;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.demo.yiman.R;
import com.demo.yiman.base.BaseDialogFragment;
import com.demo.yiman.ui.update.Contract;
import com.demo.yiman.ui.update.JsDownloadListener;
import com.demo.yiman.ui.update.UpdataPresenter;
import com.demo.yiman.utils.ShowToast;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;


public class UpdateDialogFragment extends BaseDialogFragment<UpdataPresenter> implements Contract.View {
    @BindView(R.id.progress_update)
    CustomProgressBar mProgress;
    @BindView(R.id.rl_btn)
    RelativeLayout mRlBtn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public static UpdateDialogFragment newInstance() {
        UpdateDialogFragment updateDialogFragment = new UpdateDialogFragment();
        return updateDialogFragment;
    }


    @Override
    protected UpdataPresenter createPresenter() {
        return new UpdataPresenter(this);
    }

    @Override
    protected Dialog createDialog() {
        Dialog dialog = new Dialog(getContext());
        if (dialog != null) {
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.comment_bg);
            dialog.getWindow().setWindowAnimations(R.style.dialog_animations);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(R.layout.dialog_update);
        }

        return dialog;
    }

    @Override
    protected void initViewData() {
//        mHandler = new Handler(new Handler.Callback() {
//            @Override
//            public boolean handleMessage(Message msg) {
//                mProgress.setProgress(msg.what);
//
//                return false;
//            }
//        });
    }

    @OnClick({R.id.btn_cancel, R.id.btn_go})
    public void onClickView(View view) {
        switch (view.getId()) {
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

    /**
     * 拉取更新Url
     */
    private void initAchieve() {
        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            //验证是否许可权限
            for (String str : permissions) {
                if (getContext().checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    getActivity().requestPermissions(permissions, REQUEST_CODE_CONTACT);
                    return;
                }
            }
        }
        downFile("http://www.izis.cn/mygoedu/yztv_1.apk");
    }

    private void downFile(String url) {
        mPresenter.download(url, new JsDownloadListener() {
            @Override
            public void onStartDownload(long length) {

            }

            @Override
            public void onProgress(final int progress) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProgress.setProgress(progress);
                    }
                });

            }

            @Override
            public void onFail(String errorInfo) {

            }

            @Override
            public void onSuccess() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ShowToast.showShort(getActivity(),"成功");
                        showInstall();//执行安装检测
                    }
                });
                dismiss();
            }


        });
    }

    @Override
    public void showUpdate() {

    }

    @Override
    public void downLoading(int i) {

    }

    @Override
    public void downSuccess() {

    }

    @Override
    public void downFial() {

    }

    @Override
    public void setMax(long l) {

    }

    /**
     * 唤起安装APK
     */
    private void showInstall(){
        Intent intent = new Intent(Intent.ACTION_VIEW);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { //android N的权限问题
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//授权读权限
            Uri contentUri = FileProvider.getUriForFile(getActivity(), "com.demo.yiman.fileProvider", new File(getApkPath(),"yztv_1.apk"));//注意修改
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(new File(getApkPath(), "Good.apk")), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        startActivity(intent);
    }
    public String getApkPath() {
        String directoryPath="";
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ) {//判断外部存储是否可用
            directoryPath = getContext().getExternalFilesDir("apk").getAbsolutePath();
        }else{//没外部存储就使用内部存储
            directoryPath = getContext().getFilesDir()+File.separator+"apk";
        }
        File file = new File(directoryPath);
        Log.e("测试路径",directoryPath);
        if(!file.exists()){//判断文件目录是否存在
            file.mkdirs();
        }
        return directoryPath;
    }
}
