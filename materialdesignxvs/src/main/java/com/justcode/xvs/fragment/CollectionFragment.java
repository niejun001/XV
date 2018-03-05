package com.justcode.xvs.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.justcode.xvs.R;
import com.justcode.xvs.activity.DownAndPlayActivity;
import com.justcode.xvs.adapter.BaseCollectionAdapter;
import com.justcode.xvs.adapter.CollectionVideoAdapter;
import com.justcode.xvs.bean.VideoCollection;

import org.litepal.crud.DataSupport;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/2/9.
 */

public class CollectionFragment extends BaseFragment {

    private RecyclerView collection_rv;
    private Subscriber<List<VideoCollection>> mMovieSubscriber;
    private List<VideoCollection> moviedata;
    private LinearLayoutManager mLayoutManager;
    private CollectionVideoAdapter videoadapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collection, container, false);
        collection_rv = (RecyclerView) view.findViewById(R.id.collection_rv);
        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    /**
     * 异步读取video
     */
    private void initData() {
        mMovieSubscriber = new Subscriber<List<VideoCollection>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(mActivity, "读取数据失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(List<VideoCollection> list) {
//                if (!list.isEmpty()) {
                    moviedata = list;
                    initView();
//                }
            }
        };

        Observable.just(queryVideo())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mMovieSubscriber);
    }

    private void initView() {
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        videoadapter = new CollectionVideoAdapter(getActivity(), moviedata);
        collection_rv.setLayoutManager(mLayoutManager);
        collection_rv.setAdapter(videoadapter);
        videoadapter.setOnItemClickListener(new BaseCollectionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String name, String imguel, String videourl) {
                DownAndPlayActivity.toActivity(getActivity(),name,imguel,videourl);
            }
        });
    }

    private List<VideoCollection> queryVideo() {
        List<VideoCollection> all = DataSupport.findAll(VideoCollection.class);
        return all;
    }
}
