package com.justcode.xvs.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;

import com.justcode.xvs.App;
import com.justcode.xvs.R;
import com.justcode.xvs.adapter.BasePagerAdapter;
import com.justcode.xvs.adapter.VideoAdapter;
import com.justcode.xvs.bean.Constants;
import com.justcode.xvs.bean.Videolist;
import com.justcode.xvs.geturl.GetVideoDownRx;
import com.justcode.xvs.geturl.GetVideoListUrlRx;
import com.justcode.xvs.util.PreferncesUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/2/9.
 */

public class ListVideoActivity extends AppCompatActivity{

    private SwipeRefreshLayout refresh;
    private RecyclerView rv;
    private String urls;
    private GetVideoListUrlRx getVideoListUrlRx;
    private GetVideoDownRx getVideoDownRx;
    private String url;
    private View footer;
    private VideoAdapter adapter;
    public int mStart = 0;
    private Toolbar atv_toolbar;
    private String label;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listvideo);

        /**
         *启动Activity,设置主题
         */
        String nowtheme = PreferncesUtils.getString(this, Constants.PREF_KEY_THEME, "1");
        if (nowtheme.equals("1")) {
            setTheme(R.style.AppTheme);
        } else {
            setTheme(R.style.AppTheme_Light);
        }

        urls = getIntent().getStringExtra("urls");
        label = getIntent().getStringExtra("label");
        refresh = ((SwipeRefreshLayout) findViewById(R.id.pager_base_fresh));
        rv = ((RecyclerView) findViewById(R.id.pager_base_rv));
        atv_toolbar = (Toolbar) findViewById(R.id.atv_toolbar);

        atv_toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        atv_toolbar.setTitle(label);

        refresh.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent, R.color.colorPrimaryDark);
        refresh.setProgressViewOffset(false, 0, 48);
        refresh.setRefreshing(true);

        //获取视频列表链接
        getVideoListUrlRx = new GetVideoListUrlRx();
        //获取下一页
        getVideoDownRx = new GetVideoDownRx();

        initView();
        initListener();
    }

    private void initView() {
        url = urls;
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
                        rv.setLayoutManager(new GridLayoutManager(ListVideoActivity.this, 3));
                        footer = LayoutInflater.from(ListVideoActivity.this).inflate(R.layout.item_footer, rv, false);
                        adapter = new VideoAdapter(ListVideoActivity.this,videolists);
                        adapter.setFooterView(footer);
                        rv.setAdapter(adapter);

                        adapter.upDates(videolists);

                        adapter.setOnClickListener(new BasePagerAdapter.OnItemClickListener() {
                            @Override
                            public void ItemClickListener(String name, String imageUrl, String videoUrl) {
                                DownAndPlayActivity.toActivity(ListVideoActivity.this, name, imageUrl,videoUrl);
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
                if (!recyclerView.canScrollVertically(1) && App.isNetworkAvailable(ListVideoActivity.this)) {
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

    /**
     * 初始化监听
     */
    private void initListener() {
        atv_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
