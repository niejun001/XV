package com.justcode.xvs.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
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
import com.justcode.xvs.util.SPUtils;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/2/8.
 */

public class SearchActivity extends AppCompatActivity{

    private SearchView search;
    private SwipeRefreshLayout search_refresh;
    private RecyclerView search_rv;
    private GetVideoListUrlRx getVideoListUrlRx;
    private GetVideoDownRx getVideoDownRx;
    public View footer;
    private VideoAdapter adapter;
    private String url;
    private LinearLayoutManager mLayoutManager;
    public int mStart = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        /**
         *启动Activity,设置主题
         */
        String nowtheme = PreferncesUtils.getString(this, Constants.PREF_KEY_THEME, "1");
        if (nowtheme.equals("1")) {
            setTheme(R.style.AppTheme);
        } else {
            setTheme(R.style.AppTheme_Light);
        }

        search = ((SearchView) findViewById(R.id.search));
        search_refresh = ((SwipeRefreshLayout) findViewById(R.id.search_refresh));
        search_rv = ((RecyclerView) findViewById(R.id.search_rv));

        //获取视频列表链接
        getVideoListUrlRx = new GetVideoListUrlRx();
        //获取下一页
        getVideoDownRx = new GetVideoDownRx();


        initView();
        initListener();
    }

    private void initView() {
        SearchManager mSearchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        search.setSearchableInfo(mSearchManager.getSearchableInfo(getComponentName()));
        //开启输入文字的清除与查询按钮
        search.setSubmitButtonEnabled(true);

        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        search_rv.setLayoutManager(mLayoutManager);
        footer = LayoutInflater.from(this).inflate(R.layout.item_footer, search_rv, false);
    }

    private void initListener() {
        search_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                search_refresh.setRefreshing(false);
            }
        });

        search_rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(1) && App.isNetworkAvailable(SearchActivity.this)) {
                    updateMovieData();
                    footer.setVisibility(View.VISIBLE);
                    search_rv.scrollToPosition(adapter.getItemCount() - 1);

                }
            }
        });

        //查询监听
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //点击查询按钮
            @Override
            public boolean onQueryTextSubmit(String query) {

                //加入判断
                //判断sp中是否有密码
                String password = SPUtils.getString(SearchActivity.this, "PAWD");
                if (!TextUtils.isEmpty(password)){
                    //判断是否输入过正确密码
                    boolean count = SPUtils.getBoolean(SearchActivity.this, "count");
                    if (count){
                        //搜索逻辑
                        query(query);
                    }else {
                        //去注册页
                        Intent intent = new Intent(SearchActivity.this, RegesitActivity.class);
                        startActivity(intent);
                    }
                }else {
                    //去注册界面
                    Intent intent = new Intent(SearchActivity.this, RegesitActivity.class);
                    startActivity(intent);
                }
                return true;
            }

            //查询框文字发送发生变化
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void updateMovieData() {
        if (adapter.getStart() == mStart) return;
        mStart = adapter.getStart();
        search_refresh.setRefreshing(true);
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
                                        search_refresh.setRefreshing(false);
                                        footer.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        footer.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onNext(List<Videolist> videolists) {
                                        adapter.addDatas(videolists);
                                    }
                                });
                    }
                });
    }

    private void query(String query) {
        if (!query.isEmpty() && query != null) {
            search_refresh.setRefreshing(true);

            url = "/?k=" + query;

            getVideoListUrlRx.getdatas(Constants.HEAD + url)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<List<Videolist>>() {
                        @Override
                        public void onCompleted() {
                            search_refresh.setRefreshing(false);
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onNext(final List<Videolist> videolists) {
                            search_rv.setLayoutManager(new GridLayoutManager(SearchActivity.this, 3));
                            footer = LayoutInflater.from(SearchActivity.this).inflate(R.layout.item_footer, search_rv, false);
                            adapter = new VideoAdapter(SearchActivity.this,videolists);
                            adapter.setFooterView(footer);
                            search_rv.setAdapter(adapter);
                            adapter.upDates(videolists);

                            adapter.setOnClickListener(new BasePagerAdapter.OnItemClickListener() {
                                @Override
                                public void ItemClickListener(String name, String imageUrl, String videoUrl) {
                                    DownAndPlayActivity.toActivity(SearchActivity.this, name, imageUrl,videoUrl);
                                }

                                @Override
                                public void ItemListClickListener(String url,String label) {
                                }

                            });
                        }
                    });
        }

        search.setQuery("", false);
        search.onActionViewCollapsed();
    }
}
