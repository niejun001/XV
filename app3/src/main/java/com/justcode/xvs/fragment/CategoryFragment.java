package com.justcode.xvs.fragment;

import com.justcode.xvs.R;

/**
 * Created by niejun on 2017/10/1.
 */

public class CategoryFragment extends ListBaseFragment{

    @Override
    public int getLayout() {
        return R.layout.frag_category;
    }

    @Override
    public int getSmartRefreshLayoutId() {
        return R.id.category_refresh;
    }

    @Override
    public int getRecyclerViewId() {
        return R.id.category_rv;
    }

    @Override
    public int getProgressBarId() {
        return R.id.pb_category;
    }

    @Override
    public String getUrl() {
        return "";
    }

    @Override
    public String setType() {
        return "CATEGORY";
    }
}
