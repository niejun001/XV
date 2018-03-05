package com.justcode.xvs.geturl;

import android.util.Log;

import com.justcode.xvs.bean.Constants;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by niejun on 2017/8/8.
 */

public class GetVideoDownRx {
    public GetVideoDownRx() {
    }

    public Observable<String> getvideodown(final String url){
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    Connection conn = Jsoup.connect(url);
                    // 修改http包中的header,伪装成浏览器进行抓取
                    conn.header("User-Agent", Constants.USERAGENT);
                    Document doc = null;
                    try {
                        doc = conn.get();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //获取视频下一页
                    Elements li = doc.select("div.pagination ").select("ul").select("li");
                    for (Element element : li) {
                        Elements a = element.select("a");
                        String text = a.text();
                        if (text.equals("Next")) {
                            String pagedown = a.attr("href");
                            Log.e("Main4Activity", pagedown);
                            subscriber.onNext(pagedown);
                        }
                    }
                    subscriber.onCompleted();
                }
            }
        });
    }
}
