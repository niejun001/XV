package com.justcode.xvs.fragment;

import com.justcode.xvs.bean.Constants;

/**
 * Created by Administrator on 2018/2/8.
 */

public class FragmentFactory {
    public static BaseFragment getFragment(String title) {
        BaseFragment fragment = null;
        switch (title) {
            case Constants.HOME:
                fragment = new HomeFragment();
                break;
            case Constants.CATEGARY:
                fragment = new CategaryFragment();
                break;
            case Constants.FAVORITE:
                fragment = new FavoriteFragment();
                break;
            case Constants.COLLECTION:
                fragment = new CollectionFragment();
                break;
            default:
                break;
        }
        return fragment;
    }

}
