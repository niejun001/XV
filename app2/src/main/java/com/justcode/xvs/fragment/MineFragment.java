package com.justcode.xvs.fragment;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.justcode.xvs.App;
import com.justcode.xvs.R;
import com.justcode.xvs.bean.Contact;
import com.justcode.xvs.update.CustomsUpdateActivity;
import com.justcode.xvs.util.APKVersionCodeUtils;
import com.kcode.lib.UpdateWrapper;
import com.kcode.lib.bean.VersionModel;
import com.kcode.lib.net.CheckUpdateTask;

/**
 * Created by niejun on 2017/10/1.
 */

public class MineFragment extends Fragment implements View.OnClickListener {
    private TextView virsencode;
    private TextView update;
    private TextView about;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_mine, container, false);
        virsencode = ((TextView) view.findViewById(R.id.virsencode));
        update = ((TextView) view.findViewById(R.id.update));
        about = ((TextView) view.findViewById(R.id.about));

        virsencode.setText("版本号：" + getVerName(App.getApplication()));
        update.setOnClickListener(this);
        about.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.update:
                checkUpdate(0, CustomsUpdateActivity.class);
                break;
            case R.id.about:
                Toast.makeText(getActivity(), "当前版本：" + APKVersionCodeUtils.getVerName(getActivity()), Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    /**
     * 获取版本号名称
     *
     * @param context 上下文
     * @return
     */
    public static String getVerName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().
                    getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }

    private void checkUpdate(final long time, final Class<? extends FragmentActivity> cls) {
        UpdateWrapper.Builder builder = new UpdateWrapper.Builder(App.getApplication())
                .setTime(time)
                .setNotificationIcon(R.mipmap.ic_launcher_round)
                .setUrl(Contact.UPDATE)
//                .setIsShowToast(false)
                .setToastMsg("已是最新版本")
                .setCallback(new CheckUpdateTask.Callback() {
                    @Override
                    public void callBack(VersionModel versionModel) {
                        Log.e("aaa","version info :" + versionModel.getVersionName() );
                    }
                });
        if (cls != null) {
            builder.setCustomsActivity(cls);
        }
        builder.build().start();
    }

}
