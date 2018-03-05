package com.justcode.xvs.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.justcode.xvs.activity.ListVideoActivity;
import com.justcode.xvs.activity.RegesitActivity;
import com.justcode.xvs.adapter.ListAdapter;
import com.justcode.xvs.bean.CategoryList;
import com.justcode.xvs.bean.Contact;
import com.justcode.xvs.geturl.GetCategoryListUrlRx;
import com.justcode.xvs.geturl.GetTrendsListUrlRx;
import com.justcode.xvs.util.SPUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by niejun on 2017/10/2.
 */

public abstract class ListBaseFragment extends Fragment {
    private SmartRefreshLayout refresh;
    private RecyclerView rv;
    private ProgressBar pb;
    private GetCategoryListUrlRx getCategoryListUrlRx;
    private String url = getUrl();
    private ListAdapter adapter;
    private GetTrendsListUrlRx getTrendsListUrlRx;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayout(), container, false);

        refresh = (SmartRefreshLayout)view.findViewById(getSmartRefreshLayoutId());
        rv = (RecyclerView)view.findViewById(getRecyclerViewId());
        pb = ((ProgressBar) view.findViewById(getProgressBarId()));

        pb.setVisibility(View.VISIBLE);

        getCategoryListUrlRx = new GetCategoryListUrlRx();
        getTrendsListUrlRx = new GetTrendsListUrlRx();
        initView();


        return view;
    }

    public abstract int getLayout();

    public abstract int getSmartRefreshLayoutId();

    public abstract int getRecyclerViewId();

    public abstract int getProgressBarId();

    public abstract String getUrl();

    public abstract String setType();

    private void initView() {
        if (setType().equals("CATEGORY")) {
            getCategoryListUrlRx.getdatas(Contact.HEAD + url)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<List<CategoryList>>() {
                        @Override
                        public void onCompleted() {
                            pb.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onNext(final List<CategoryList> categoryLists) {
                            rv.setLayoutManager(new LinearLayoutManager(getActivity()));
                            adapter = new ListAdapter(categoryLists);
                            rv.setAdapter(adapter);
                            adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
                            adapter.setNewData(categoryLists);
                            adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                                @SuppressLint("NewApi")
                                @Override
                                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                                    clip(categoryLists,position);

                                }
                            });
                        }
                    });
        }else if (setType().equals("TRENDS")){

            getTrendsListUrlRx.getdatas(Contact.HEAD + url)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<List<CategoryList>>() {

                        @Override
                        public void onCompleted() {
                            pb.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(final List<CategoryList> categoryLists) {
                            rv.setLayoutManager(new LinearLayoutManager(getActivity()));
                            adapter = new ListAdapter(categoryLists);
                            rv.setAdapter(adapter);
                            adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
                            adapter.setNewData(categoryLists);
                            adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                                @SuppressLint("NewApi")
                                @Override
                                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                                    clip(categoryLists,position);
                                }
                            });
                        }
                    });
        }


        refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(1000);
            }
        });
        refresh.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(final RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore();
            }
        });
    }

    private void clip(List<CategoryList> categoryLists, int position) {
        String urls = categoryLists.get(position).getUrl();
        Log.e("ListBaseFragment", urls);

        //加入判断
        //判断sp中是否有密码
        String password = SPUtils.getString(getContext(), "PAWD");
        if (!TextUtils.isEmpty(password)){
            //判断是否输入过正确密码
            boolean count = SPUtils.getBoolean(getContext(), "count");
            if (count){
                //去视频列表
                Intent intent = new Intent(getContext(), ListVideoActivity.class);
                intent.putExtra("urls", urls);
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
