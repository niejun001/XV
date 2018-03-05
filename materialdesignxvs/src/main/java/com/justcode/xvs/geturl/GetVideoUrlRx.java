package com.justcode.xvs.geturl;

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
 * Created by niejun on 2017/10/14.
 */

public class GetVideoUrlRx {
    public GetVideoUrlRx() {
    }

    public Observable<String> getvideourl(final String url) {
        return Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                Connection conn2 = Jsoup.connect(url);
                // 修改http包中的header,伪装成浏览器进行抓取
                conn2.header("User-Agent", Constants.USERAGENT);
                Document doc2 = null;
                try {
                    doc2 = conn2.get();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Element videoplayer = doc2.getElementById("video-player-bg");
                Elements script = videoplayer.getElementsByTag("script");
                String[] elScriptList = script.get(4).data().toString().split("var");

                String[] split = elScriptList[2].split("html5player.setVideoUrlHigh\\('");
                //highUrl地址
                String highUrl = split[1].split("'\\);")[0];
                subscriber.onNext(highUrl);
                subscriber.onCompleted();
            }
        });
    }
}
