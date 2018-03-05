package com.justcode.xvs.fragment;

import com.justcode.xvs.R;

/**
 * Created by niejun on 2017/10/1.
 */

public class HomeFragment extends VideoBaseFragment{

    @Override
    public void init() {
    }

    @Override
    public int getLayout() {
        return R.layout.frag_home;
    }

    @Override
    public int getSmartRefreshLayoutId() {
        return R.id.videohome_refresh;
    }

    @Override
    public int getRecyclerViewId() {
        return R.id.videohome_rv;
    }

    @Override
    public int getProgressBarId() {
        return R.id.pb_home;
    }

    @Override
    public String getUrl() {
        return "";
    }
}
