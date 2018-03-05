package com.justcode.xvs.geturl;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/2/10.
 */

public class GetControllJson {
    public GetControllJson() {
    }

    public Observable<String> getConJson(final String url){
        return Observable.create(new Observable.OnSubscribe<String>(){

            @Override
            public void call(final Subscriber<? super String> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    OkHttpClient okHttpClient = new OkHttpClient();
                    Request request = new Request.Builder().url(url).get().build();
                    okHttpClient.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Request request, IOException e) {
                        }

                        @Override
                        public void onResponse(Response response) throws IOException {
                            String string = response.body().string();
                            /*try {
                                JSONObject jsonObject = new JSONObject(string);
                                int versionCode = jsonObject.getInt("versionCode");
                                subscriber.onNext(versionCode);
                                subscriber.onCompleted();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }*/
                            subscriber.onNext(string);
                            subscriber.onCompleted();
                        }
                    });

                }
            }
        });
    }

    public Observable<String> getConVerName(final String url){
        return Observable.create(new Observable.OnSubscribe<String>(){

            @Override
            public void call(final Subscriber<? super String> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    getConJson(url)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<String>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onNext(String s) {
                                    JSONObject jsonObject = null;
                                    try {
                                        jsonObject = new JSONObject(s);
                                        String versionName = jsonObject.getString("versionName");
                                        subscriber.onNext(versionName);
                                        subscriber.onCompleted();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                }
            }
        });
    }

    public Observable<Integer> getConVerCode(final String url){
        return Observable.create(new Observable.OnSubscribe<Integer>(){
            @Override
            public void call(final Subscriber<? super Integer> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    getConJson(url)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<String>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onNext(String s) {
                                    JSONObject jsonObject = null;
                                    try {
                                        jsonObject = new JSONObject(s);
                                        int versionCode = jsonObject.getInt("versionCode");
                                        subscriber.onNext(versionCode);
                                        subscriber.onCompleted();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                }
            }
        });
    }
}
