package com.justcode.xvs.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.justcode.xvs.R;
import com.justcode.xvs.activity.ListVideoActivity;
import com.justcode.xvs.activity.RegesitActivity;
import com.justcode.xvs.adapter.BasePagerAdapter;
import com.justcode.xvs.adapter.ListAdapter;
import com.justcode.xvs.bean.CategoryList;
import com.justcode.xvs.bean.Constants;
import com.justcode.xvs.geturl.GetCategoryListUrlRx;
import com.justcode.xvs.geturl.GetTrendsListUrlRx;
import com.justcode.xvs.util.SPUtils;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/2/8.
 */

public abstract class ListBaseFragment extends BaseFragment {

    private SwipeRefreshLayout refresh;
    private RecyclerView rv;
    private GetCategoryListUrlRx getCategoryListUrlRx;
    private String url = getUrl();
    private ListAdapter adapter;
    private GetTrendsListUrlRx getTrendsListUrlRx;
    public View footer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayout(), container, false);

        refresh = (SwipeRefreshLayout)view.findViewById(getSwipeRefreshLayoutId());
        rv = (RecyclerView)view.findViewById(getRecyclerViewId());

        refresh.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent, R.color.colorPrimaryDark);
        refresh.setProgressViewOffset(false, 0, 48);

        refresh.setRefreshing(true);

        getCategoryListUrlRx = new GetCategoryListUrlRx();
        getTrendsListUrlRx = new GetTrendsListUrlRx();
        initView();

        return view;
    }

    public abstract int getLayout();

    public abstract int getSwipeRefreshLayoutId();

    public abstract int getRecyclerViewId();

    public abstract String getUrl();

    public abstract String setType();

    private void initView() {
        if (setType().equals("CATEGORY")) {
            getCategoryListUrlRx.getdatas(Constants.HEAD + url)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<List<CategoryList>>() {
                        @Override
                        public void onCompleted() {
                            refresh.setRefreshing(false);
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onNext(final List<CategoryList> categoryLists) {
                            rv.setLayoutManager(new LinearLayoutManager(getActivity()));
                            footer = LayoutInflater.from(getContext()).inflate(R.layout.item_footer, rv, false);

                            adapter = new ListAdapter(getContext(),categoryLists);
                            adapter.setFooterView(footer);
                            rv.setAdapter(adapter);
                            adapter.upDates(categoryLists);

                            adapter.setOnClickListener(new BasePagerAdapter.OnItemClickListener() {
                                @Override
                                public void ItemClickListener(String name, String imageUrl, String videoUrl) {
                                }

                                @Override
                                public void ItemListClickListener(String url,String label) {
                                    clip(url,label);
                                }
                            });
                        }
                    });
        }else if (setType().equals("TRENDS")){

            getTrendsListUrlRx.getdatas(Constants.HEAD + url)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<List<CategoryList>>() {

                        @Override
                        public void onCompleted() {
                            refresh.setRefreshing(false);
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(final List<CategoryList> categoryLists) {
                            rv.setLayoutManager(new LinearLayoutManager(getActivity()));
                            footer = LayoutInflater.from(getContext()).inflate(R.layout.item_footer, rv, false);

                            adapter = new ListAdapter(getContext(),categoryLists);
                            adapter.setFooterView(footer);
                            rv.setAdapter(adapter);
                            adapter.upDates(categoryLists);

                            adapter.setOnClickListener(new BasePagerAdapter.OnItemClickListener() {
                                @Override
                                public void ItemClickListener(String name, String imageUrl, String videoUrl) {
                                }

                                @Override
                                public void ItemListClickListener(String url,String label) {
                                    clip(url,label);
                                }
                            });
                        }
                    });
        }


        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh.setRefreshing(false);
            }
        });

    }

    private void clip(String url,String label) {
        //加入判断
        //判断sp中是否有密码
        String password = SPUtils.getString(getContext(), "PAWD");
        if (!TextUtils.isEmpty(password)){
            //判断是否输入过正确密码
            boolean count = SPUtils.getBoolean(getContext(), "count");
            if (count){
                //去视频列表
                Intent intent = new Intent(getContext(), ListVideoActivity.class);
                intent.putExtra("urls", url);
                intent.putExtra("label",label);
                startActivity(intent);
            }else {
                //去注册页
                Intent intent = new Intent(getContext(), RegesitActivity.class);
                startActivity(intent);
            }
        }else {
            //去注册界面
            Intent intent = new Intent(getContext(), RegesitActivity.class);
            startActivity(intent);
        }
    }

}
