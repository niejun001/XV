package com.justcode.xvs.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.justcode.xvs.App;
import com.justcode.xvs.R;
import com.justcode.xvs.activity.DownAndPlayActivity;
import com.justcode.xvs.adapter.BasePagerAdapter;
import com.justcode.xvs.adapter.VideoAdapter;
import com.justcode.xvs.bean.Constants;
import com.justcode.xvs.bean.Videolist;
import com.justcode.xvs.geturl.GetVideoDownRx;
import com.justcode.xvs.geturl.GetVideoListUrlRx;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/2/8.
 */

public abstract class VideoBaseFragment extends BaseFragment {
    private SwipeRefreshLayout refresh;
    private RecyclerView rv;
    private GetVideoListUrlRx getVideoListUrlRx;
    private String url ;
    private VideoAdapter adapter;
    private GetVideoDownRx getVideoDownRx;
    public View footer;
    public int mStart = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayout(), container, false);

        refresh = ((SwipeRefreshLayout) view.findViewById(getSwipeRefreshLayoutId()));
        rv = ((RecyclerView) view.findViewById(getRecyclerViewId()));

        refresh.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent, R.color.colorPrimaryDark);
        refresh.setProgressViewOffset(false, 0, 48);

        //获取视频列表链接
        getVideoListUrlRx = new GetVideoListUrlRx();
        //获取下一页
        getVideoDownRx = new GetVideoDownRx();

        refresh.setRefreshing(true);
        initView();

        return view;
    }

    public abstract void init();

    public abstract int getLayout();

    public abstract int getSwipeRefreshLayoutId();

    public abstract int getRecyclerViewId();

    public abstract String getUrl();

    private void initView() {
        url = getUrl();
        getVideoListUrlRx.getdatas(Constants.HEAD + url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Videolist>>() {
                    @Override
                    public void onCompleted() {
                        refresh.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(final List<Videolist> videolists) {
                        rv.setLayoutManager(new GridLayoutManager(getContext(), 3));
                        footer = LayoutInflater.from(getContext()).inflate(R.layout.item_footer, rv, false);
                        adapter = new VideoAdapter(getActivity(),videolists);
                        adapter.setFooterView(footer);
                        rv.setAdapter(adapter);

                        adapter.upDates(videolists);

                        adapter.setOnClickListener(new BasePagerAdapter.OnItemClickListener() {
                            @Override
                            public void ItemClickListener(String name, String imageUrl, String videoUrl) {
                                DownAndPlayActivity.toActivity(getActivity(), name, imageUrl,videoUrl);
                            }

                            @Override
                            public void ItemListClickListener(String url,String label) {

                            }

                        });

                    }
                });

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh.setRefreshing(false);
            }
        });

        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(1) && App.isNetworkAvailable(getActivity())) {
                    updateMovieData();
                    footer.setVisibility(View.VISIBLE);
                    rv.scrollToPosition(adapter.getItemCount() - 1);

                }
            }
        });

    }

    private void updateMovieData() {
        if (adapter.getStart() == mStart) return;
        mStart = adapter.getStart();
        refresh.setRefreshing(true);
        getVideoDownRx.getvideodown(Constants.HEAD + url)
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
                        getVideoListUrlRx.getdatas(Constants.HEAD + s)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Subscriber<List<Videolist>>() {
                                    @Override
                                    public void onCompleted() {
                                        refresh.setRefreshing(false);
                                        footer.setVisibility(View.GONE);

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        footer.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onNext(List<Videolist> videolists) {
                                        if (!videolists.isEmpty()) {
                                            adapter.addDatas(videolists);
                                        }
                                    }
                                });
                    }
                });
    }
}
