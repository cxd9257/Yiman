package com.demo.yiman.base.baseMVP;

import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.text.ParseException;

import io.reactivex.observers.DisposableObserver;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

public abstract class BaseObserver<T> extends DisposableObserver<T> {
    protected BaseView view;
    private boolean isShowDialog;
    /**
     * 解析数据失败
     */
    public static final int PARSE_ERROR = 1001;
    /**
     * 网络问题
     */
    public static final int BAD_NETWORK = 1002;
    /**
     * 连接错误
     */
    public static final int CONNECT_ERROR = 1003;
    /**
     * 连接超时
     */
    public static final int CONNECT_TIMEOUT = 1004;
    public BaseObserver(BaseView view){
        this.view = view;
    }

    @Override
    protected void onStart() {
        super.onStart();
//        if (view !=null){
//            view.showLoading();
//        }
    }

    /**
     * 理论onNext方法应该做服务器返回判断
     * 因调用了不同API
     * 返回字段不统一，故直接传入完整返回
     * @param t
     */
    @Override
    public void onNext(T t) {
//        BaseModel model = (BaseModel)t;
//        try {
//            if (model.getErrcode() == 1){
                onSuccess(t);
//            }else{
//                if (view != null){
//                    view.onErrorCode(model);
//                }
//            }
//        }catch (Exception e){
//
//        }
    }

    protected abstract void onSuccess(T t);

    /**
     * 做的错误判断
     * 在正式商业跟需求改动
     * @param e
     */
    @Override
    public void onError(Throwable e) {
//        if (view != null && isShowDialog) {
//            view.hideLoading();
//        }
        BaseException be = null;
//        ResponseBody responseBody = ((HttpException) e).response().errorBody();
//        try{
//            if (responseBody!=null){
//                responseBody.string();
//            }
//        }catch (IOException e1){
//            e1.printStackTrace();
//        }
        if (e != null) {

            if (e instanceof BaseException) {
                be = (BaseException) e;

                //回调到view层 处理 或者根据项目情况处理
                if (view != null) {
                    view.onErrorCode(new BaseModel(be.getErrorCode(), be.getErrorMsg()));
                } else {
                    onError(be.getErrorMsg());
                }

            } else {
                if (e instanceof HttpException) {
                    //   HTTP错误
                    be = new BaseException(BaseException.BAD_NETWORK_MSG, e, BaseException.BAD_NETWORK);
                } else if (e instanceof ConnectException
                        || e instanceof UnknownHostException) {
                    //   连接错误
                    be = new BaseException(BaseException.CONNECT_ERROR_MSG, e, BaseException.CONNECT_ERROR);
                } else if (e instanceof InterruptedIOException) {
                    //  连接超时
                    be = new BaseException(BaseException.CONNECT_TIMEOUT_MSG, e, BaseException.CONNECT_TIMEOUT);
                } else if (e instanceof JsonParseException
                        || e instanceof JSONException
                        || e instanceof ParseException) {
                    //  解析错误
                    be = new BaseException(BaseException.PARSE_ERROR_MSG, e, BaseException.PARSE_ERROR);
                } else {
                    be = new BaseException(BaseException.OTHER_MSG, e, BaseException.OTHER);
                }
            }
        } else {
            be = new BaseException(BaseException.OTHER_MSG, e, BaseException.OTHER);
        }

        onError(be.getErrorMsg());
    }

    private void onException(int parseError){

        switch (parseError) {
            case CONNECT_ERROR:
                onError("连接错误");
                break;

            case CONNECT_TIMEOUT:
                onError("连接超时");
                break;

            case BAD_NETWORK:
                onError("网络问题");
                break;

            case PARSE_ERROR:
                onError("解析数据失败");
                break;

            default:
                break;
        }

    };

    @Override
    public void onComplete() {

    }
    public abstract void onError(String msg);
}
