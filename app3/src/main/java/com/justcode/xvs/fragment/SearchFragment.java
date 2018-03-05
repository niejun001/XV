package com.justcode.xvs.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.justcode.xvs.R;
import com.justcode.xvs.SpacesItemDecoration;
import com.justcode.xvs.activity.DownAndPlayActivity;
import com.justcode.xvs.activity.RegesitActivity;
import com.justcode.xvs.adapter.VideoAdapter;
import com.justcode.xvs.bean.Contact;
import com.justcode.xvs.bean.Videolist;
import com.justcode.xvs.geturl.GetVideoDownRx;
import com.justcode.xvs.geturl.GetVideoListUrlRx;
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
 * Created by niejun on 2017/10/1.
 */

public class SearchFragment extends Fragment implements View.OnClickListener {
    private SmartRefreshLayout search_refresh;
    private RecyclerView search_rv;
    private ProgressBar pb_search;
    private EditText et_search;
    private TextView bt_search;
    private GetVideoListUrlRx getVideoListUrlRx;
    private VideoAdapter adapter;
    private GetVideoDownRx getVideoDownRx;
    private String url;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_search, container, false);
        et_search = ((EditText) view.findViewById(R.id.et_search));
        bt_search = ((TextView) view.findViewById(R.id.bt_search));
        search_refresh = ((SmartRefreshLayout) view.findViewById(R.id.search_refresh));
        search_rv = ((RecyclerView) view.findViewById(R.id.search_rv));
        pb_search = ((ProgressBar) view.findViewById(R.id.pb_search));

        //获取视频列表链接
        getVideoListUrlRx = new GetVideoListUrlRx();
        //获取下一页
        getVideoDownRx = new GetVideoDownRx();

        bt_search.setOnClickListener(this);

        init();
        return view;
    }

    private void init() {
        search_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(1000);
            }
        });

        search_refresh.setOnLoadmoreListener(new OnLoadmoreListener() {
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

    @Override
    public void onClick(View view) {
        String s = et_search.getText().toString();
        //加入判断
        //判断sp中是否有密码
        String password = SPUtils.getString(getContext(), "PAWD");
        if (!TextUtils.isEmpty(password)){
            //判断是否输入过正确密码
            boolean count = SPUtils.getBoolean(getContext(), "count");
            if (count){
                //搜索逻辑
                search(s);
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

    private void search(String s) {
        if (!s.isEmpty() && s != null) {
            pb_search.setVisibility(View.VISIBLE);

            url = "/?k=" + s;

            getVideoListUrlRx.getdatas(Contact.HEAD + url)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<List<Videolist>>() {
                        @Override
                        public void onCompleted() {
                            pb_search.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onNext(final List<Videolist> videolists) {
                            search_rv.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayout.VERTICAL));
                            //设置item之间的间隔
                            SpacesItemDecoration decoration = new SpacesItemDecoration(10);
                            search_rv.addItemDecoration(decoration);
                            adapter = new VideoAdapter(videolists,getActivity());
                            search_rv.setAdapter(adapter);
                            adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
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
        }
    }
}
