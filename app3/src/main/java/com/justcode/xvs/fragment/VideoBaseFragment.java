package com.justcode.xvs.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.justcode.xvs.SpacesItemDecoration;
import com.justcode.xvs.activity.DownAndPlayActivity;
import com.justcode.xvs.adapter.VideoAdapter;
import com.justcode.xvs.bean.Contact;
import com.justcode.xvs.bean.Videolist;
import com.justcode.xvs.geturl.GetVideoDownRx;
import com.justcode.xvs.geturl.GetVideoListUrlRx;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by niejun on 2017/10/1.
 */

public abstract class VideoBaseFragment extends Fragment {
    private SmartRefreshLayout refresh;
    private RecyclerView rv;
    private ProgressBar pb;
    private GetVideoListUrlRx getVideoListUrlRx;
    private String url ;
    private VideoAdapter adapter;
    private GetVideoDownRx getVideoDownRx;
//    private VideoAdapter2 adapter2;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayout(), container, false);

        refresh = ((SmartRefreshLayout) view.findViewById(getSmartRefreshLayoutId()));
        rv = ((RecyclerView) view.findViewById(getRecyclerViewId()));
        pb = ((ProgressBar) view.findViewById(getProgressBarId()));

        pb.setVisibility(View.VISIBLE);

        //获取视频列表链接
        getVideoListUrlRx = new GetVideoListUrlRx();
        //获取下一页
        getVideoDownRx = new GetVideoDownRx();

        initView();

        return view;
    }

    public abstract void init();

    public abstract int getLayout();

    public abstract int getSmartRefreshLayoutId();

    public abstract int getRecyclerViewId();

    public abstract int getProgressBarId();

    public abstract String getUrl();

    private void initView() {
        url = getUrl();
        getVideoListUrlRx.getdatas(Contact.HEAD + url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Videolist>>() {
                    @Override
                    public void onCompleted() {
                        pb.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(final List<Videolist> videolists) {
                        rv.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayout.VERTICAL));
                        //设置item之间的间隔
                        SpacesItemDecoration decoration = new SpacesItemDecoration(10);
                        rv.addItemDecoration(decoration);
                        adapter = new VideoAdapter(videolists,getActivity());
//                        adapter2 = new VideoAdapter2(getActivity(),videolists);
                        rv.setAdapter(adapter);
//                        adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
                        adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);

                        adapter.setNewData(videolists);

                        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                            @SuppressLint("NewApi")
                            @Override
                            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                                Intent intent = new Intent(getActivity(), DownAndPlayActivity.class);
                                intent.putExtra("imgurl",videolists.get(position).getImgurl());
                                intent.putExtra("name",videolists.get(position).getName());
                                intent.putExtra("videourl",videolists.get(position).getVideourl());
                                getActivity().startActivity(intent);
                            }
                        });
                    }
                });

        refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(1000);
            }
        });
        refresh.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(final RefreshLayout refreshlayout) {
                getVideoDownRx.getvideodown(Contact.HEAD + url)
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
                                url = s;
                                getVideoListUrlRx.getdatas(Contact.HEAD + s)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Subscriber<List<Videolist>>() {
                                            @Override
                                            public void onCompleted() {
                                                adapter.loadMoreComplete();
                                                refreshlayout.finishLoadmore();
                                            }

                                            @Override
                                            public void onError(Throwable e) {
                                            }

                                            @Override
                                            public void onNext(List<Videolist> videolists) {
                                                adapter.addData(videolists);
                                            }
                                        });
                            }
                        });

            }
        });
    }

}
