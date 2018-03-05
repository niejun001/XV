package com.justcode.xvs.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.justcode.xvs.R;
import com.justcode.xvs.bean.Contact;
import com.justcode.xvs.geturl.GetVideoUrlRx;
import com.umeng.analytics.MobclickAgent;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class DownAndPlayActivity extends BaseActivity implements View.OnClickListener {
    private String imgurl;
    private String name;
    private String videourl;
    private ImageView iv;
    private TextView tv;
    private TextView down;
    private TextView play;
    private GetVideoUrlRx getVideoUrlRx;
    private ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imgurl = getIntent().getStringExtra("imgurl");
        name = getIntent().getStringExtra("name");
        videourl = getIntent().getStringExtra("videourl");
        setContentView(R.layout.activity_down_and_play);

        getVideoUrlRx = new GetVideoUrlRx();
        iv = ((ImageView) findViewById(R.id.downandplay_iv));
        tv = ((TextView) findViewById(R.id.downandplay_tv));
        down = ((TextView) findViewById(R.id.downandplay_down));
        play = ((TextView) findViewById(R.id.downandplay_play));
        pb = ((ProgressBar) findViewById(R.id.pb));

        tv.setText(name);

        Glide.with(this).load(imgurl)
                .bitmapTransform(new RoundedCornersTransformation(this, 10, 0,
                        RoundedCornersTransformation.CornerType.ALL))
                .crossFade(500).into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        iv.setImageDrawable(resource);
                    }
        });
//        Log.e("DownAndPlayActivity", imgurl + ", " + name + ", " + videourl);
        down.setOnClickListener(DownAndPlayActivity.this);
        play.setOnClickListener(DownAndPlayActivity.this);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.downandplay_down:
                //设置滚动条可见
                pb.setVisibility(View.VISIBLE);
                getVideoUrlRx.getvideourl(Contact.HEAD + videourl)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<String>() {
                            @Override
                            public void onCompleted() {
                                //设置滚动条不可见
                                pb.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError(Throwable e) {
                            }

                            @Override
                            public void onNext(String s) {

                                //浏览器打开
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse(s));
                                startActivity(intent);

//                                Log.e("DownAndPlayActivity", s);
//                                Toast.makeText(DownAndPlayActivity.this, s, Toast.LENGTH_SHORT).show();
                            }
                        });
                break;
            case R.id.downandplay_play:
                //设置滚动条可见
                pb.setVisibility(View.VISIBLE);
                getVideoUrlRx.getvideourl(Contact.HEAD + videourl)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<String>() {
                            @Override
                            public void onCompleted() {
                                //设置滚动条不可见
                                pb.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError(Throwable e) {
                            }

                            @Override
                            public void onNext(String s) {
                                //播放器打开
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setDataAndType(Uri.parse(s), "video/mp4");
                                startActivity(intent);
                            }
                        });
                break;
            default:
                break;
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
