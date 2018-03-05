package com.justcode.xvs.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.gyf.barlibrary.BarHide;
import com.gyf.barlibrary.ImmersionBar;

/**
 * Created by niejun on 2018/2/4.
 */

public class BaseActivity extends AppCompatActivity {
    protected ImmersionBar mImmersionBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ImmersionBar.with(this)
//                .hideBar(BarHide.FLAG_HIDE_STATUS_BAR) //隐藏状态栏或导航栏或两者，不写默认不隐藏 .hideBar(BarHide.FLAG_HIDE_BAR)
//                .barAlpha(0.0f)  //状态栏和导航栏透明度，不写默认0.0f
//                .init(); //初始化，默认透明状态栏和黑色导航栏
        //初始化沉浸式
        if (isImmersionBarEnabled())
            initImmersionBar();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        ImmersionBar.with(this).destroy(); //不调用该方法，如果界面bar发生改变，在不关闭app的情况下，退出此界面再进入将记忆最后一次bar改变的状态
        if (mImmersionBar != null)
            mImmersionBar.destroy();  //在BaseActivity里销毁
    }

    protected void initImmersionBar() {
        //在BaseActivity里初始化
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.keyboardEnable(true).hideBar(BarHide.FLAG_HIDE_STATUS_BAR).init();
    }

    /**
     * 是否可以使用沉浸式
     * Is immersion bar enabled boolean.
     *
     * @return the boolean
     */
    protected boolean isImmersionBarEnabled() {
        return true;
    }
}
