package com.framework.http.utils;

import com.framework.utils.CloseUtils;
import com.framework.utils.LogUtils;
import com.framework.utils.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by lichen on 2018/5/31.
 */

public class DownloadUtils {
    private static DownloadUtils downloadUtils;
    private OkHttpClient client;
    private Map<String, Call> requests = new HashMap<>();

    private File fileDir;
    private final String DIR_NAME = "download";


    private DownloadUtils() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.writeTimeout(30, TimeUnit.SECONDS);
        builder.readTimeout(30, TimeUnit.SECONDS);
        client = builder.build();
        fileDir = Utils.getApp().getExternalFilesDir(DIR_NAME);
    }

    public static DownloadUtils getInstance() {
        synchronized (DownloadUtils.class) {
            if (null == downloadUtils) {
                downloadUtils = new DownloadUtils();
            }
        }
        return downloadUtils;
    }

    public void cancelRequest(String tag) {
        Call call = requests.get(tag);
        if (null != call && call.isExecuted()) {
            call.cancel();
        }
        requests.remove(tag);
    }

    //单片段下载代码
    public void downloadFile(final String tag, final String link, final String fileName, final OnDownloadListener listener) {

        Observable observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(final ObservableEmitter<Integer> emitter) throws Exception {

                Call call = client.newCall(new Request.Builder()
                        .url(link)
                        .build());
                requests.put(tag, call);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        LogUtils.e("网络异常" + link);
                        emitter.onError(e);
                        requests.remove(tag);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            InputStream is = null;
                            byte[] buf = new byte[2048];
                            int len = 0;
                            long total = response.body().contentLength();
                            FileOutputStream fos = null;
                            File file = new File(fileDir, fileName);

                            if (file.exists()) {
                                file.delete();
                                file.createNewFile();
                            }
                            try {
                                is = response.body()
                                        .byteStream();
                                fos = new FileOutputStream(file, true);
                                long sum = 0;
                                while ((len = is.read(buf)) != -1) {
                                    if (!call.isCanceled()) {
                                        fos.write(buf, 0, len);
                                        sum += len;
                                        int progress = (int) (sum * 100 / total);
                                        // 下载中
                                        emitter.onNext(progress);
                                    }
                                }
                                fos.flush();
                                emitter.onComplete();
                            } catch (Exception e) {
                                if (!call.isCanceled()) {
                                    emitter.onError(e);
                                }
                                LogUtils.d("文件下载失败");
                                e.printStackTrace();
                            } finally {
                                requests.remove(tag);
                                CloseUtils.closeIO(is);
                                CloseUtils.closeIO(fos);
                            }
                        } else {
                            emitter.onError(new Exception(response.code() + "::" + response.message()));
                        }
                    }
                });
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        observable.subscribe(listener);
    }

    public static abstract class OnDownloadListener implements Observer<Integer> {
        @Override
        public void onSubscribe(Disposable d) {

        }

        @Override
        public abstract void onNext(Integer value);

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onComplete() {
        }
    }
}
