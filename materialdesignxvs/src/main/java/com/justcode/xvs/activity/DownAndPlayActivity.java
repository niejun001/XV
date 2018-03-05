package com.justcode.xvs.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.justcode.xvs.R;
import com.justcode.xvs.bean.Constants;
import com.justcode.xvs.bean.VideoCollection;
import com.justcode.xvs.geturl.GetVideoUrlRx;
import com.justcode.xvs.util.ImageUtils;
import com.justcode.xvs.util.PreferncesUtils;
import com.umeng.analytics.MobclickAgent;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.app.ActivityOptions.makeSceneTransitionAnimation;
import static com.justcode.xvs.R.drawable.collection_false;

/**
 * Created by Administrator on 2018/2/9.
 */

public class DownAndPlayActivity extends AppCompatActivity implements View.OnClickListener, AppBarLayout.OnOffsetChangedListener {
    private String imgurl;
    private String name;
    private String videourl;
    private SwipeRefreshLayout activitymdrefresh;
    private CoordinatorLayout activitymdcoorl;
    private NestedScrollView activitymovienested;
    private LinearLayout activitymdll;
    private AppBarLayout activitymdappbar;
    private CollapsingToolbarLayout activitymdcolltl;
    private Toolbar activitymdtoolbar;
    private ImageView activitymdiv;
    private TextView downandplay_tv;
    private TextView downandplay_down;
    private TextView downandplay_play;
    private GetVideoUrlRx getVideoUrlRx;
    private String url;
    private boolean lockCollection = false;
    private boolean isCollection = false;

    public static void toActivity(Activity activity, String name, String imageUrl, String videoUrl) {
        Intent intent = new Intent(activity, DownAndPlayActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("imgurl", imageUrl);
        intent.putExtra("videourl", videoUrl);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            activity.startActivity(intent,
                    makeSceneTransitionAnimation(activity).toBundle());
        } else {
            activity.startActivity(intent);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private Transition makeTransition() {
        TransitionSet transition = new TransitionSet();
        transition.addTransition(new Explode());
        transition.addTransition(new Fade());
        transition.setDuration(400);
        return transition;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
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
        setContentView(R.layout.activity_down_and_play);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(makeTransition());
        }

        imgurl = getIntent().getStringExtra("imgurl");
        name = getIntent().getStringExtra("name");
        videourl = getIntent().getStringExtra("videourl");

        init();
        initView();

        initListener();
    }

    private void init() {
        this.activitymdrefresh = (SwipeRefreshLayout) findViewById(R.id.activity_md_refresh);
        this.activitymdcoorl = (CoordinatorLayout) findViewById(R.id.activity_md_coorl);
        this.activitymovienested = (NestedScrollView) findViewById(R.id.activity_movie_nested);
        this.activitymdll = (LinearLayout) findViewById(R.id.activity_md_ll);
        this.activitymdappbar = (AppBarLayout) findViewById(R.id.activity_md_appbar);
        this.activitymdcolltl = (CollapsingToolbarLayout) findViewById(R.id.activity_md_colltl);
        this.activitymdtoolbar = (Toolbar) findViewById(R.id.activity_md_toolbar);
        this.activitymdiv = (ImageView) findViewById(R.id.activity_md_iv);
        this.downandplay_tv = (TextView) findViewById(R.id.downandplay_tv);
        this.downandplay_down = (TextView) findViewById(R.id.downandplay_down);
        this.downandplay_play = (TextView) findViewById(R.id.downandplay_play);
        getVideoUrlRx = new GetVideoUrlRx();
        activitymdrefresh.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent, R.color.colorPrimaryDark);
        activitymdrefresh.setProgressViewOffset(false, 0, 48);
        activitymdtoolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        activitymdtoolbar.inflateMenu(R.menu.menu_moviedetails_toolbar);

        activitymdrefresh.setRefreshing(true);
        //初始化Menu
        Menu menu = activitymdtoolbar.getMenu();
        if (query()) {
            menu.getItem(0).setIcon(R.drawable.collection_true);
            isCollection = true;
        } else {
            menu.getItem(0).setIcon(R.drawable.collection_false);
            isCollection = false;
        }
    }

    private boolean query() {
        List<VideoCollection> videoCollections = DataSupport.where("name = ? and imgurl = ?", name,imgurl).find(VideoCollection.class);
        if (videoCollections != null && videoCollections.size() > 0) {
            return true;
        }else {
            return false;
        }
    }

    private void initView() {
        downandplay_tv.setText(name);
        Glide.with(this)
                .load(imgurl)
                .asBitmap()
                .listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        int color = ImageUtils.getColor(resource);
                        activitymdcolltl.setContentScrimColor(color);
                        activitymdcolltl.setBackgroundColor(color);

                        return false;
                    }
                })
                .into(activitymdiv);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.downandplay_down:
                if (url != null) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                }

                break;
            case R.id.downandplay_play:
                if (url != null){
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(url), "video/mp4");
                    try {
                        startActivity(intent);
                    } catch (Exception e) {
                        Toast.makeText(this, "当前手机没有默认播放器，请选择浏览器打开", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
    }

    private void initListener() {

        downandplay_down.setOnClickListener(DownAndPlayActivity.this);
        downandplay_play.setOnClickListener(DownAndPlayActivity.this);

        activitymdrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                activitymdrefresh.setRefreshing(false);
            }
        });

        activitymdappbar.addOnOffsetChangedListener(this);

        activitymdtoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getVideoUrlRx.getvideourl(Constants.HEAD + videourl)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        //设置滚动条不可见
                        activitymdrefresh.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        activitymdrefresh.setRefreshing(false);
                        lockCollection = false;
                    }

                    @Override
                    public void onNext(String s) {
                        if (s != null){
                            url = s;
                            lockCollection = true;
                        }else {
                            url = null;
                            lockCollection = false;
                        }
                    }
                });

        activitymdtoolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (lockCollection) {
                    if (isCollection) {
                        item.setIcon(collection_false);
                        isCollection = false;
                        delCollection();
                        Toast.makeText(DownAndPlayActivity.this, "取消收藏成功", Toast.LENGTH_SHORT).show();
                    } else {
                        item.setIcon(R.drawable.collection_true);
                        isCollection = true;
                        boolean b = collectionVideo();
                        if (b) {
                            Toast.makeText(DownAndPlayActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(DownAndPlayActivity.this, "收藏失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    private void delCollection() {
        DataSupport.deleteAll(VideoCollection.class, "name = ? and imgurl = ?", name, imgurl);
    }

    private boolean collectionVideo() {
        VideoCollection videoCollection = new VideoCollection();
        videoCollection.setName(name);
        videoCollection.setImgurl(imgurl);
        videoCollection.setVideourl(videourl);
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String time = dateFormat.format(now);
        videoCollection.setTime(time);
        videoCollection.save();
        if (videoCollection.save()) {
            return true;
        }else {
            return false;
        }
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        //当Appbar完全显示时才启用SwipeRefreshLayout
        activitymdrefresh.setEnabled(verticalOffset == 0);
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
