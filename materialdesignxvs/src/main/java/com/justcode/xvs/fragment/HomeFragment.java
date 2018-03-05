package com.justcode.xvs.fragment;

import com.justcode.xvs.R;

/**
 * Created by Administrator on 2018/2/8.
 */

public class HomeFragment extends VideoBaseFragment {

    @Override
    public void init() {

    }

    @Override
    public int getLayout() {
        return R.layout.pagerfragment_base;
    }

    @Override
    public int getSwipeRefreshLayoutId() {
        return R.id.pager_base_fresh;
    }

    @Override
    public int getRecyclerViewId() {
        return R.id.pager_base_rv;
    }

    @Override
    public String getUrl() {
        return "";
    }
}
