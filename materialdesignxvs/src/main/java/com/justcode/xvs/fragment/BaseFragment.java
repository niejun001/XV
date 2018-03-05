package com.justcode.xvs.fragment;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

/**
 * Created by Administrator on 2018/2/8.
 */

public class BaseFragment extends Fragment {
    public Activity mActivity;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity)context;
    }
}
