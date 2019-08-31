package com.demo.yiman.utils;

import android.os.Environment;
import android.util.Log;

import com.demo.yiman.base.gson.BaseConverterFacory;
import com.demo.yiman.net.ApiConfig;
import com.demo.yiman.net.api.ApiServer;
import com.demo.yiman.ui.update.JsDownloadInterceptor;
import com.demo.yiman.ui.update.JsDownloadListener;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static org.litepal.LitePalApplication.getContext;

public class DownloadUtils {
    private static final String TAG = "DownloadUtils";
    private static final int DEFAULT_TIMEOUT = 10;

    private Thread mThread;//子线程进行io读写操作

    private Retrofit retrofit;
    private JsDownloadListener listener;
    private String baseUrl;
    private String downloadUrl;
    /**
     * 请求访问
     * response拦截器
     */
    public Interceptor interceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            long startTime = System.currentTimeMillis();
            Response response = chain.proceed(chain.request());
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            MediaType mediaType = response.body().contentType();
            String content = response.body().string();
            Log.e(TAG,"---------Request Start----------");
            Log.e(TAG,"|"+ request.toString()+request.headers().toString());
            Log.e(TAG,"|Response:"+ content);
            Log.e(TAG,"---------Request End:"+duration+"毫秒--------");
            return response.newBuilder().body(ResponseBody.create(mediaType,content)).build();
        }
    };
    public DownloadUtils(String baseUrl, JsDownloadListener listener){
        this.baseUrl = baseUrl;
        this.listener = listener;
        JsDownloadInterceptor mInterceptor = new JsDownloadInterceptor(listener);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .retryOnConnectionFailure(true)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT,TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(ApiConfig.BASE_URL)
                .client(httpClient)
                .addConverterFactory(BaseConverterFacory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    /**
     * 开始下载
     */
    public static final String PATH_CHALLENGE_APK = Environment.getExternalStorageDirectory() + "/DownloadFileTwo";
    private String mApkPath; //下载到本地的Apk路径
    private File mFile;
    public void downLoad(String url, final JsDownloadListener downloadUrl){
        String directoryPath="";
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ) {//判断外部存储是否可用
            directoryPath = getContext().getExternalFilesDir("apk").getAbsolutePath();
        }else{//没外部存储就使用内部存储
            directoryPath = getContext().getFilesDir()+File.separator+"apk";
        }
        mFile = new File(directoryPath);
        if (!mFile.exists() || !mFile.isDirectory()) {
            mFile.mkdirs();
        }
        //通过Url得到保存到本地的文件名
        String name = url;
        int index = name.lastIndexOf('/');//一定是找最后一个'/'出现的位置
        if (index != -1) {
            name = name.substring(index);
            mApkPath = directoryPath + name;
            Log.d(TAG,"mApkPath=" + mApkPath);
        }
        mFile = new File(mApkPath);
        ApiServer apiServer=retrofit.create(ApiServer.class);
        Observable<ResponseBody> observable = apiServer.download(url);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.e("dsd","dad11");
                    }

                    @Override
                    public void onNext(final ResponseBody responseBody) {
                        mThread = new Thread(){
                            @Override
                            public void run() {
                                super.run();
                                writeFileSDcard(responseBody,mFile,downloadUrl);
                            }
                        };
                        mThread.start();

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        Log.e("dsd","success");
                    }
                });
    }
    public void writeFileSDcard(ResponseBody responseBody, File mFile, JsDownloadListener downloadListener) {

        Log.d(TAG,"writeFileSDcard");
        long currentLength = 0;
        OutputStream os = null;
        InputStream is = responseBody.byteStream();
        long totalLength = responseBody.contentLength();
        Log.d(TAG,"totalLength=" + totalLength);
        try {
            os = new FileOutputStream(mFile);
            int len;
            byte[] buff = new byte[1024];
            while ((len = is.read(buff)) != -1) {
                os.write(buff, 0, len);
                currentLength += len;
                Log.d(TAG,"当前长度: " + currentLength);
                int progress = (int) (100 * currentLength / totalLength);
                Log.d(TAG,"当前进度: " + progress);
                downloadListener.onProgress(progress);
                if (progress == 100) {
                    //downloadListener.onFinish(mVideoPath);
                    downloadListener.onSuccess();
                }
            }
        } catch (FileNotFoundException e) {
            Log.d(TAG,"Exception=" + e.getMessage());
            downloadListener.onFail("未找到文件！");
            e.printStackTrace();
        } catch (IOException e) {
            Log.d(TAG,"Exception=" + e.getMessage());
            downloadListener.onFail("IO错误！");
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
