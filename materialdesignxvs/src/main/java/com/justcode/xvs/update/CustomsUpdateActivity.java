package com.justcode.xvs.update;

import android.support.v4.app.Fragment;

import com.kcode.lib.dialog.UpdateActivity;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by DELL on 2017/11/8.
 */

public class CustomsUpdateActivity extends UpdateActivity {
    @Override
    protected Fragment getUpdateDialogFragment() {
        return CustomsUpdateFragment.newInstance(mModel,"当前已经是最新版本");
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
