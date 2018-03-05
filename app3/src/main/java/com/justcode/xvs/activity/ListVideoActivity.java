package com.justcode.xvs.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.justcode.xvs.R;
import com.justcode.xvs.SpacesItemDecoration;
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

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ListVideoActivity extends BaseActivity {
    @InjectView(R.id.pb_listvideo)
    ProgressBar pb_listvideo;
    @InjectView(R.id.listvideo_refresh)
    SmartRefreshLayout listvideo_refresh;
    @InjectView(R.id.listvideo_rv)
    RecyclerView listvideo_rv;
    private GetVideoListUrlRx getVideoListUrlRx;
    private GetVideoDownRx getVideoDownRx;
    private String urls;
    private String url;
    private VideoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_video);
        ButterKnife.inject(this);

        urls = getIntent().getStringExtra("urls");
        pb_listvideo.setVisibility(View.VISIBLE);

        //获取视频列表链接
        getVideoListUrlRx = new GetVideoListUrlRx();
        //获取下一页
        getVideoDownRx = new GetVideoDownRx();

        initView();
    }

    private void initView() {
        url = urls;
        getVideoListUrlRx.getdatas(Contact.HEAD + url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Videolist>>() {
                    @Override
                    public void onCompleted() {
                        pb_listvideo.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(final List<Videolist> videolists) {
                        listvideo_rv.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayout.VERTICAL));
                        //设置item之间的间隔
                        SpacesItemDecoration decoration = new SpacesItemDecoration(10);
                        listvideo_rv.addItemDecoration(decoration);
                        adapter = new VideoAdapter(videolists, ListVideoActivity.this);
                        listvideo_rv.setAdapter(adapter);
                        adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
                        adapter.setNewData(videolists);

                        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                            @SuppressLint("NewApi")
                            @Override
                            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                                Intent intent = new Intent(ListVideoActivity.this, DownAndPlayActivity.class);
                                intent.putExtra("imgurl",videolists.get(position).getImgurl());
                                intent.putExtra("name",videolists.get(position).getName());
                                intent.putExtra("videourl",videolists.get(position).getVideourl());
                                ListVideoActivity.this.startActivity(intent);
                            }
                        });
                    }
                });

        listvideo_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(1000);
            }
        });
        listvideo_refresh.setOnLoadmoreListener(new OnLoadmoreListener() {
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
