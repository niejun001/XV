package com.justcode.xvs.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.justcode.xvs.App;
import com.justcode.xvs.R;
import com.justcode.xvs.activity.RegesitActivity;
import com.justcode.xvs.bean.Contact;
import com.justcode.xvs.update.CustomsUpdateActivity;
import com.justcode.xvs.util.SPUtils;
import com.kcode.lib.UpdateWrapper;
import com.kcode.lib.bean.VersionModel;
import com.kcode.lib.net.CheckUpdateTask;

/**
 * Created by niejun on 2017/10/1.
 */

public class MineFragment extends Fragment implements View.OnClickListener {
    private TextView userid;
    private TextView update;
    private TextView about;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_mine, container, false);
        userid = ((TextView) view.findViewById(R.id.userid));
        update = ((TextView) view.findViewById(R.id.update));
        about = ((TextView) view.findViewById(R.id.about));

        initUser(App.getApplication());
        update.setOnClickListener(this);
        about.setOnClickListener(this);
        userid.setOnClickListener(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initUser(App.getApplication());
    }

    private void initUser(Context context) {
        String pawd = SPUtils.getString(context, "PAWD");
        if (!TextUtils.isEmpty(pawd)){
            //判断是否输入过正确密码
            boolean count = SPUtils.getBoolean(getContext(), "count");
            if (count){
                userid.setText(pawd);
                userid.setTextColor(getResources().getColor(R.color.colorAccent));
            }else {
                userid.setText(R.string.regesit);
                userid.setTextColor(getResources().getColor(R.color.colorAccent));
            }
        }else {
            userid.setText(R.string.regesit);
            userid.setTextColor(getResources().getColor(R.color.colorAccent));
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.update:
                checkUpdate(0, CustomsUpdateActivity.class);
                break;
            case R.id.about:
                Toast.makeText(getActivity(), "当前版本：" + getResources().getString(R.string.app_name) + getVerName(getActivity()), Toast.LENGTH_LONG).show();
                break;
            case R.id.userid:
                if (userid.getText().toString().contains("请注册")){
                    Intent intent = new Intent(getContext(), RegesitActivity.class);
                    startActivity(intent);
                }
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
