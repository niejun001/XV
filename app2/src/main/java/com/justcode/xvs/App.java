package com.justcode.xvs;

import android.app.Application;

import com.umeng.commonsdk.UMConfigure;

import cn.jpush.im.android.api.JMessageClient;

/**
 * Created by DELL on 2017/11/9.
 */

public class App extends Application {

    private static App instance;

    public static App getApplication() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        /**
         * 初始化common库
         * 参数1:上下文，不能为空
         * 参数2:友盟 app key
         * 参数3:友盟 channel
         * 参数4:设备类型，UMConfigure.DEVICE_TYPE_PHONE为手机、UMConfigure.DEVICE_TYPE_BOX为盒子，默认为手机
         * 参数5:Push推送业务的secret
         */
        UMConfigure.init(this,
                "5a0eaf7fa40fa34cf20000ac",
                "Umeng",
                UMConfigure.DEVICE_TYPE_PHONE,
                null);

        JMessageClient.setDebugMode(true);
        JMessageClient.init(this);
    }
}
