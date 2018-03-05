package com.justcode.xvs.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.justcode.xvs.R;
import com.justcode.xvs.bean.Constants;
import com.justcode.xvs.fragment.FragmentFactory;
import com.justcode.xvs.fragment.HomeFragment;
import com.justcode.xvs.fragment.SettingFragment;
import com.justcode.xvs.update.CustomsUpdateActivity;
import com.justcode.xvs.util.PreferncesUtils;
import com.justcode.xvs.util.SPUtils;
import com.justcode.xvs.util.ToastUtils;
import com.justcode.xvs.util.UIUtils;
import com.kcode.lib.UpdateWrapper;
import com.kcode.lib.bean.VersionModel;
import com.kcode.lib.log.L;
import com.kcode.lib.net.CheckUpdateTask;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static org.litepal.LitePalApplication.getContext;

public class MainActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener, View.OnClickListener {
    private android.support.design.widget.NavigationView mainnav;
    private android.support.v4.widget.DrawerLayout maindrawerlayout;
    private RelativeLayout main_container;
    private Toolbar main_toolbar;
    private View headerView;
    private ImageView nav_header_img;
    private TextView nav_header_title;
    private static final String SAVE_STATE_TITLE = "title";
    private String title;
    private FragmentManager mFragmentManager;
    private Fragment DefaultFragment;
    private long mExitTime = 0;
    private TextView tv_header;
    private CircleImageView iv_header;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         *启动Activity,设置主题
         */
        String nowtheme = PreferncesUtils.getString(this, Constants.PREF_KEY_THEME, "1");
        if (nowtheme.equals("1")) {
            setTheme(R.style.AppTheme);
        } else {
            setTheme(R.style.AppTheme_Light);
        }

        setContentView(R.layout.activity_main);

        initView();
        initToolbar();
        setupDrawerContent();
        initFragment(savedInstanceState);

        //检查更新
        checkUpdate(0, CustomsUpdateActivity.class);
    }



    private void checkUpdate(final long time, final Class<? extends FragmentActivity> cls) {
        UpdateWrapper.Builder builder = new UpdateWrapper.Builder(getApplicationContext())
                .setTime(time)
                .setNotificationIcon(R.mipmap.ic_launcher_round)
                .setUrl(Constants.UPDATE)
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

    private void initView() {
        this.maindrawerlayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        this.mainnav = (NavigationView) findViewById(R.id.main_nav);
        this.main_toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        this.main_container = (RelativeLayout) findViewById(R.id.main_container);

        //默认打开
        maindrawerlayout.openDrawer(GravityCompat.START);

        main_toolbar.inflateMenu(R.menu.menu_toolbar);
        main_toolbar.setOnMenuItemClickListener(this);
        main_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maindrawerlayout.openDrawer(GravityCompat.START);
            }
        });
        headerView = mainnav.getHeaderView(0);
        nav_header_img = (ImageView) headerView.findViewById(R.id.nav_header_img);
        iv_header = (CircleImageView) headerView.findViewById(R.id.iv_header);
        nav_header_title = (TextView) headerView.findViewById(R.id.nav_header_title);
        tv_header = (TextView) headerView.findViewById(R.id.tv_header);
        tv_header.setOnClickListener(this);

        nav_header_img.setImageDrawable(UIUtils.getDrawable(this,R.mipmap.nav_bg));
        nav_header_title.setText("车速太快，请系好安全带！");
    }

    private void initUser(Context context) {
        String pawd = SPUtils.getString(context, "PAWD");
        if (!TextUtils.isEmpty(pawd)){
            //判断是否输入过正确密码
            boolean count = SPUtils.getBoolean(getContext(), "count");
            if (count){
                tv_header.setText(pawd);
                tv_header.setTextColor(getResources().getColor(R.color.colorAccent));
                iv_header.setImageDrawable(getResources().getDrawable(R.mipmap.header2));
            }else {
                tv_header.setText(R.string.regesit);
                tv_header.setTextColor(getResources().getColor(R.color.colorAccent));
                iv_header.setImageDrawable(getResources().getDrawable(R.mipmap.header1));
            }
        }else {
            tv_header.setText(R.string.regesit);
            tv_header.setTextColor(getResources().getColor(R.color.colorAccent));
            iv_header.setImageDrawable(getResources().getDrawable(R.mipmap.header1));
        }
    }

    /**
     * 初始化Toolbar
     */
    private void initToolbar() {
        if (main_toolbar.getTitle() == null) {
            main_toolbar.setTitle(UIUtils.getString(this, R.string.nav_menu_home));
            mainnav.getMenu().getItem(0).setChecked(true);
        }
    }

    /**
     * nav menu点击事件
     */
    private void setupDrawerContent() {
        mainnav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                maindrawerlayout.closeDrawers();
                String title = (String) menuItem.getTitle();
                main_toolbar.setTitle(title);

                //根据menu的Title开启Fragment
                switchFragment(title);
                return true;
            }
        });
    }

    /**
     * 初始化Fragment
     */
    private void initFragment(Bundle savedInstanceState) {
        SPUtils.put(this, "istheme", true);
        //根据title创建Fragment
        if (savedInstanceState != null) {
            title = savedInstanceState.getString(SAVE_STATE_TITLE);
        }
        if (title == null) {
            title = UIUtils.getString(this, R.string.nav_menu_home);
        }


        mFragmentManager = getSupportFragmentManager();
        DefaultFragment = mFragmentManager.findFragmentByTag(title);
        if (DefaultFragment == null) {
            Fragment homeFragment = new HomeFragment();
            mFragmentManager.beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .add(R.id.main_container, homeFragment, title)
                    .commit();
            DefaultFragment = homeFragment;
        }
    }

    /**
     * 根据nav的menu开启Fragment
     *
     * @param title
     */
    private void switchFragment(String title) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (title.equals(UIUtils.getString(this, R.string.nav_menu_home)) && Constants.CHANGELABEL_HOME) {
            Fragment movieFragment = createFragmentByTitle(title);
            transaction.hide(DefaultFragment);
            transaction.replace(R.id.main_container, movieFragment, title).commit();
            DefaultFragment = movieFragment;
        } else if (title.equals(UIUtils.getString(this, R.string.nav_menu_categray)) && Constants.CHANGELABEL_CATEGARY) {
            Fragment categaryFragment = createFragmentByTitle(title);
            transaction.hide(DefaultFragment);
            transaction.replace(R.id.main_container, categaryFragment, title).commit();
            DefaultFragment = categaryFragment;
        } else {
            //根据Tag判断是否已经开启了Fragment，如果开启了就直接复用，没开启就创建
            Fragment fragment = mFragmentManager.findFragmentByTag(title);
            if (fragment == null) {
                transaction.hide(DefaultFragment);
                fragment = createFragmentByTitle(title);
                transaction.add(R.id.main_container, fragment, title);
                DefaultFragment = fragment;
            } else if (fragment != null) {
                transaction.hide(DefaultFragment).show(fragment);
                DefaultFragment = fragment;
            }
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).
                    commit();
        }
    }

    private Fragment createFragmentByTitle(String title) {
        if (title.equals(Constants.SETTING)) {
            SettingFragment mSettingFragment = new SettingFragment();
            return mSettingFragment;
        } else {
            Fragment fragment = FragmentFactory.getFragment(title);
            return fragment;
        }
    }

    /**
     * Toolbar menu点击事件
     *
     * @param item
     * @return
     */
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_menu_search:
                Intent mIntent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(mIntent);
                break;
        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!maindrawerlayout.isDrawerOpen(GravityCompat.START)) {
                if ((System.currentTimeMillis() - mExitTime) > 2000) {
                    ToastUtils.show(MainActivity.this, "再按一次退出程序");
                    mExitTime = System.currentTimeMillis();
                } else {
                    ToastUtils.cancel();
                    finish();
                }
            } else {
                maindrawerlayout.closeDrawers();
            }
        }
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outPersistentState.putString(SAVE_STATE_TITLE, title);
    }

    /**
     * 清空其他Fragment
     */
    public void removeFragment(String title) {
        mFragmentManager = getSupportFragmentManager();
        List<Fragment> fragments = mFragmentManager.getFragments();
        if (fragments == null) {
            return;
        }

        //保留title的Fragment
        for (Fragment fragment : fragments) {
            if (fragment == null || fragment.getTag().equals(title))
                continue;
            mFragmentManager.beginTransaction().remove(fragment).commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initUser(this);
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public void onClick(View view) {
        if (tv_header.getText().toString().contains("请注册")){
            Intent intent = new Intent(getContext(), RegesitActivity.class);
            startActivity(intent);
        }
    }
}
