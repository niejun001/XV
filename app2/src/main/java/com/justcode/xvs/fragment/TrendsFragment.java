package com.justcode.xvs.fragment;

import com.justcode.xvs.R;

/**
 * Created by niejun on 2017/10/1.
 */

public class TrendsFragment extends ListBaseFragment{

    @Override
    public int getLayout() {
        return R.layout.frag_trends;
    }

    @Override
    public int getSmartRefreshLayoutId() {
        return R.id.trends_refresh;
    }

    @Override
    public int getRecyclerViewId() {
        return R.id.trends_rv;
    }

    @Override
    public int getProgressBarId() {
        return R.id.pb_trends;
    }

    @Override
    public String getUrl() {
        return "";
    }

    @Override
    public String setType() {
        return "TRENDS";
    }
}
