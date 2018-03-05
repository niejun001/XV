package com.justcode.xvs.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;

import com.justcode.xvs.App;
import com.justcode.xvs.R;
import com.justcode.xvs.activity.MainActivity;
import com.justcode.xvs.bean.Constants;
import com.justcode.xvs.geturl.GetControllJson;
import com.justcode.xvs.update.CustomsUpdateActivity;
import com.justcode.xvs.util.PreferncesUtils;
import com.kcode.lib.UpdateWrapper;
import com.kcode.lib.bean.VersionModel;
import com.kcode.lib.net.CheckUpdateTask;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/2/8.
 */

public class SettingFragment  extends PreferenceFragmentCompat implements Preference.OnPreferenceClickListener {
    public static final String PREF_KEY_THEME = "pref_key_theme";
    public static final String PREF_KEY_PROTOCOL = "pref_key_protocol";
    public static final String PREF_KEY_FEEDBACK = "pref_key_feedback";

    private ListPreference ThemePreference;
    private Preference Feedback;
    private Preference Protocol;
    private final Preference.OnPreferenceChangeListener listener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object o) {

            String key = preference.getKey();
            switch (key) {
                case PREF_KEY_THEME:
                    getActivity().finish();
                    startActivity(new Intent(getActivity(), MainActivity.class));
                    break;
                default:
                    break;
            }
            return true;
        }
    };
    private String conVerName;

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.preferences);
        ThemePreference = (ListPreference) findPreference(PREF_KEY_THEME);
        ThemePreference.setOnPreferenceChangeListener(listener);

        Feedback = findPreference(PREF_KEY_FEEDBACK);
        Feedback.setSummary(getVerName(getContext()));
        Feedback.setOnPreferenceClickListener(this);

        Protocol = findPreference(PREF_KEY_PROTOCOL);
        Protocol.setOnPreferenceClickListener(this);

        GetControllJson getControllJson = new GetControllJson();
        getControllJson.getConVerName(Constants.UPDATE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onError(Throwable e) {
                    }
                    @Override
                    public void onNext(String s) {
                        Log.e("获取版本名", s);
                        conVerName = s;
                    }
                });
        getControllJson.getConVerCode(Constants.UPDATE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onError(Throwable e) {
                    }
                    @Override
                    public void onNext(Integer integer) {
                        Log.e("获取版本号", "integer:" + integer);
                        if (integer > getVercode(getContext())){
                            Protocol.setSummary("最新版本"+conVerName);
                        }else {
                            Protocol.setSummary("已是最新版本");
                        }
                    }
                });

        initPreferences();
    }

    public int getVercode(Context context){
        int verCode = 0;
        try {
            verCode = context.getPackageManager().
                    getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verCode;
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

    private void initPreferences() {
        String nowtheme = PreferncesUtils.getString(getActivity(), Constants.PREF_KEY_THEME, "1");
        if (nowtheme.equals("1")) {
            ThemePreference.setSummary("白天");
        } else {
            ThemePreference.setSummary("晚上");
        }
    }


    @Override
    public boolean onPreferenceClick(Preference preference) {
        switch (preference.getKey()) {
            case PREF_KEY_PROTOCOL:
                //检查更新
                checkUpdate(0, CustomsUpdateActivity.class);
                break;
            default:
                break;
        }
        return true;
    }

    private void checkUpdate(final long time, final Class<? extends FragmentActivity> cls) {
        UpdateWrapper.Builder builder = new UpdateWrapper.Builder(App.getApplication())
                .setTime(time)
                .setNotificationIcon(R.mipmap.ic_launcher_round)
                .setUrl(Constants.UPDATE)
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
