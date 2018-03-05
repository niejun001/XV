package com.justcode.xvs.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gyf.barlibrary.BarHide;
import com.justcode.xvs.R;
import com.justcode.xvs.bean.Contact;
import com.justcode.xvs.fragment.CategoryFragment;
import com.justcode.xvs.fragment.HomeFragment;
import com.justcode.xvs.fragment.MineFragment;
import com.justcode.xvs.fragment.SearchFragment;
import com.justcode.xvs.fragment.TrendsFragment;
import com.justcode.xvs.update.CustomsUpdateActivity;
import com.kcode.lib.UpdateWrapper;
import com.kcode.lib.bean.VersionModel;
import com.kcode.lib.log.L;
import com.kcode.lib.net.CheckUpdateTask;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    @InjectView(R.id.content)
    FrameLayout content;
    @InjectView(R.id.first_layout)
    RelativeLayout firstLayout;
    @InjectView(R.id.second_layout)
    RelativeLayout secondLayout;
    @InjectView(R.id.third_layout)
    RelativeLayout thirdLayout;
    @InjectView(R.id.four_layout)
    RelativeLayout fourLayout;
    @InjectView(R.id.five_layout)
    RelativeLayout fiveLayout;
    @InjectView(R.id.first_image)
    ImageView firstImage;
    @InjectView(R.id.first_text)
    TextView firstText;
    @InjectView(R.id.second_image)
    ImageView secondImage;
    @InjectView(R.id.second_text)
    TextView secondText;
    @InjectView(R.id.third_image)
    ImageView thirdImage;
    @InjectView(R.id.third_text)
    TextView thirdText;
    @InjectView(R.id.four_image)
    ImageView fourImage;
    @InjectView(R.id.four_text)
    TextView fourText;
    @InjectView(R.id.five_image)
    ImageView fiveImage;
    @InjectView(R.id.five_text)
    TextView fiveText;

    private FragmentManager fragmentManager;
    private HomeFragment fg1;
    private CategoryFragment fg2;
    private SearchFragment fg3;
    private TrendsFragment fg4;
    private MineFragment fg5;

    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);
        fragmentManager = getSupportFragmentManager();
        setChioceItem(0);

        //检查更新
        checkUpdate(0, CustomsUpdateActivity.class);
    }

    private void checkUpdate(final long time, final Class<? extends FragmentActivity> cls) {
        UpdateWrapper.Builder builder = new UpdateWrapper.Builder(getApplicationContext())
                .setTime(time)
                .setNotificationIcon(R.mipmap.ic_launcher_round)
                .setUrl(Contact.UPDATE)
                .setIsShowToast(false)
                .setCallback(new CheckUpdateTask.Callback() {
                    @Override
                    public void callBack(VersionModel versionModel) {
                        L.e("aaa","version info :" + versionModel.getVersionName() );
                    }
                });
        if (cls != null) {
            builder.setCustomsActivity(cls);
        }
        builder.build().start();
    }

    @OnClick({R.id.first_layout, R.id.second_layout, R.id.third_layout, R.id.four_layout, R.id.five_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.first_layout:
                setChioceItem(0);
                mImmersionBar.fitsSystemWindows(true).hideBar(BarHide.FLAG_HIDE_STATUS_BAR).init();
                break;
            case R.id.second_layout:
                setChioceItem(1);
                mImmersionBar.fitsSystemWindows(true).hideBar(BarHide.FLAG_HIDE_STATUS_BAR).init();
                break;
            case R.id.third_layout:
                setChioceItem(2);
                mImmersionBar.fitsSystemWindows(true).keyboardEnable(true).hideBar(BarHide.FLAG_HIDE_STATUS_BAR).init();
                break;
            case R.id.four_layout:
                setChioceItem(3);
                mImmersionBar.fitsSystemWindows(true).hideBar(BarHide.FLAG_HIDE_STATUS_BAR).init();
                break;
            case R.id.five_layout:
                setChioceItem(4);
                mImmersionBar.fitsSystemWindows(true).hideBar(BarHide.FLAG_HIDE_STATUS_BAR).init();
                break;
        }
    }

    private void setChioceItem(int index) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        clearChioce(); // 清空, 重置选项, 隐藏所有Fragment
        hideFragments(fragmentTransaction);

        switch (index) {
            case 0:
                firstLayout.setSelected(true);

                // 如果fg1为空，则创建一个并添加到界面上
                if (fg1 == null) {
                    fg1 = new HomeFragment();
                    fragmentTransaction.add(R.id.content, fg1);
                } else {
                    // 如果不为空，则直接将它显示出来
                    fragmentTransaction.show(fg1);
                }
                break;

            case 1:
                secondLayout.setSelected(true);

                if (fg2 == null) {
                    fg2 = new CategoryFragment();
                    fragmentTransaction.add(R.id.content, fg2);
                } else {
                    fragmentTransaction.show(fg2);
                }
                break;

            case 2:
                thirdLayout.setSelected(true);

                if (fg3 == null) {
                    fg3 = new SearchFragment();
                    fragmentTransaction.add(R.id.content, fg3);
                } else {
                    fragmentTransaction.show(fg3);
                }
                break;

            case 3:
                fourLayout.setSelected(true);

                if (fg4 == null) {
                    fg4 = new TrendsFragment();
                    fragmentTransaction.add(R.id.content, fg4);
                } else {
                    fragmentTransaction.show(fg4);
                }
                break;
            case 4:
                fiveLayout.setSelected(true);

                if (fg5 == null) {
                    fg5 = new MineFragment();
                    fragmentTransaction.add(R.id.content, fg5);
                } else {
                    fragmentTransaction.show(fg5);
                }
                break;
        }
        fragmentTransaction.commit();   // 提交
    }

    private void clearChioce() {
        firstLayout.setSelected(false);
        secondLayout.setSelected(false);
        thirdLayout.setSelected(false);
        fourLayout.setSelected(false);
        fiveLayout.setSelected(false);
    }

    private void hideFragments(FragmentTransaction fragmentTransaction) {
        if (fg1 != null) {
            fragmentTransaction.hide(fg1);
        }
        if (fg2 != null) {
            fragmentTransaction.hide(fg2);
        }
        if (fg3 != null) {
            fragmentTransaction.hide(fg3);
        }
        if (fg4 != null) {
            fragmentTransaction.hide(fg4);
        }
        if (fg5 != null) {
            fragmentTransaction.hide(fg5);
        }
    }

    @Override
    public void onBackPressed() {
        exit();
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            exit();
//            return false;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
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
