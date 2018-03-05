package com.justcode.xvs.geturl;

import com.justcode.xvs.bean.Contact;
import com.justcode.xvs.bean.Videolist;

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
 * Created by 聂军 on 2017/8/7.
 */

public class GetVideoListUrlRx {
    public GetVideoListUrlRx(){

    }

    public Observable<List<Videolist>> getdatas(final String url){
        return Observable.create(new Observable.OnSubscribe<List<Videolist>>() {
            @Override
            public void call(Subscriber<? super List<Videolist>> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    List<Videolist> datas = new ArrayList<Videolist>();
                    Connection conn = Jsoup.connect(url);
                    // 修改http包中的header,伪装成浏览器进行抓取
                    conn.header("User-Agent", Contact.USERAGENT);
                    Document doc = null;
                    try {
                        doc = conn.get();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //获取首页视频列表链接
                    Elements select1 = doc.select("div.thumb-block");
                    if (select1 != null && select1.size() > 0) {
                        for (Element element : select1) {
                            //标题
                            String name = element.select("p").select("a").attr("title");
                            //视频拼接地址
//                            Elements script1 = element.select("div.thumb-inside").select("div.thumb").select("script");
//                            if (script1.size() > 0) {
//                                String data = script1.get(0).data();
//                                String img = data.split("<img src=\"")[1].split("\" id=\"")[0];
//                                String videourl = data.split("<a href=\"")[1].split("\"><img src=\"")[0];
//                                datas.add(new Videolist(name,img,videourl));
//                            }
                            Elements a = element.select("div.thumb-inside").select("div.thumb").select("a");
                            String videourl = a.attr("href");
                            String text = a.text();
                            String img = a.select("img").attr("data-src");
//                            Log.e("GetVideoListUrlRx", img + "--->>>" + text );

                            datas.add(new Videolist(name,img,videourl));

                        }
                    }
                    subscriber.onNext(datas);
                    subscriber.onCompleted();
                }
            }
        });
    }
}
