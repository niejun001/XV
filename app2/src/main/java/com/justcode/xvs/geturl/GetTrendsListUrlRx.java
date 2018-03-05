package com.justcode.xvs.geturl;

import android.util.Log;

import com.justcode.xvs.bean.Contact;
import com.justcode.xvs.bean.CategoryList;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by niejun on 2017/10/14.
 */

public class GetTrendsListUrlRx {
    public GetTrendsListUrlRx() {
    }

    public Observable<List<CategoryList>> getdatas(final String url) {
        return Observable.create(new Observable.OnSubscribe<List<CategoryList>>() {
            @Override
            public void call(Subscriber<? super List<CategoryList>> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    List<CategoryList> datas = new ArrayList<CategoryList>();
                    Connection conn = Jsoup.connect(url);
                    // 修改http包中的header,伪装成浏览器进行抓取
                    conn.header("User-Agent", Contact.USERAGENT);
                    Document doc = null;
                    try {
                        doc = conn.get();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //获取列表链接
                    Elements a = doc.select("div.mobile-hide").select("a");
                    for (Element element : a) {
                        String text = element.text();
                        String href = element.attr("href");
                        Log.e("Main4Activity", text + "  ,,,  " + href);
                        if (text != null && !text.equals("")
                                && href != null && !href.equals("")
                                && !text.equals("Pornstars")
                                && !text.equals("Channels")
                                && !text.equals("Profiles")
                                && !href.equals("/tags")
                                && !text.equals("Porn Pics")) {
                            datas.add(new CategoryList(text, href));
                        }
                    }
                    subscriber.onNext(datas);
                    subscriber.onCompleted();
                }
            }
        });
    }
}
