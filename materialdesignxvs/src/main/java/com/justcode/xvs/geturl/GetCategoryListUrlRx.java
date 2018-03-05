package com.justcode.xvs.geturl;

import com.justcode.xvs.bean.CategoryList;
import com.justcode.xvs.bean.Constants;

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

public class GetCategoryListUrlRx {
    public GetCategoryListUrlRx() {
    }

    public Observable<List<CategoryList>> getdatas(final String url){
        return Observable.create(new Observable.OnSubscribe<List<CategoryList>>() {
            @Override
            public void call(Subscriber<? super List<CategoryList>> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    List<CategoryList> datas = new ArrayList<CategoryList>();
                    Connection conn = Jsoup.connect(url);
                    // 修改http包中的header,伪装成浏览器进行抓取
                    conn.header("User-Agent", Constants.USERAGENT);
                    Document doc = null;
                    try {
                        doc = conn.get();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

//                    //获取列表链接
//                    Elements select = doc.getElementsByClass("main-categories");
//                    Elements script = select.select("ul").get(0).child(2).getElementsByTag("script");
//                    String[] elScriptList = script.get(0).data().toString().split("write\\(");
//                    String s = elScriptList[1].split(", false,")[0];
//                    //解析json数组
//                    Type listType=new TypeToken<LinkedList<CategoryList>>(){}.getType();
//                    Gson gson=new Gson();
//                    LinkedList<CategoryList> users=gson.fromJson(s,listType);
//                    for(Iterator iterator = users.iterator(); iterator.hasNext();){
//                        CategoryList categoryList=(CategoryList)iterator.next();
//                        //...doSomething
//                        String label = categoryList.getLabel();
//                        String url1 = categoryList.getUrl();
//                        datas.add(new CategoryList(label,url1));
//                    }
                    Elements select1 = doc.select("li.dyn");
                    for (Element element : select1) {
                        String url1 = element.select("a").attr("href");
                        String label = element.select("a").text();
//                        Log.e("GetCategoryListUrlRx", url1 + "-->>" + label);
                        datas.add(new CategoryList(label,url1));
                    }
                    subscriber.onNext(datas);
                    subscriber.onCompleted();
                }
            }
        });
    }
}
