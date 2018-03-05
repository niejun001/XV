package com.justcode.xvs.activity;

import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.justcode.xvs.R;
import com.justcode.xvs.util.MD5Utils;
import com.justcode.xvs.util.SPUtils;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;

public class RegesitActivity extends BaseActivity implements View.OnClickListener {
    private EditText et_user;
    private TextView bt_save;
    private EditText et_code;
    private TextView bt_main;
    private TextView bt_see;
    private TextView bt_get;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regesit);
        initView();
    }

    private void initView() {
        et_user = ((EditText) findViewById(R.id.et_user));
        et_code = ((EditText) findViewById(R.id.et_code));
        bt_save = ((TextView) findViewById(R.id.bt_save));
        bt_main = ((TextView) findViewById(R.id.bt_main));
        bt_get = ((TextView) findViewById(R.id.bt_get));
        bt_see = ((TextView) findViewById(R.id.bt_see));

        bt_save.setOnClickListener(this);
        bt_main.setOnClickListener(this);
        bt_see.setOnClickListener(this);
        bt_get.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        String pawd = SPUtils.getString(this, "PAWD");
        switch (view.getId()){
            case R.id.bt_save:
                //注册极光IM，注册成功保存，不成功不保存
                //极光注册支持小写字母，大写字母，数字，.，@,-
                final String user = et_user.getText().toString();
                String encode = MD5Utils.encode(user+"nj");
                if (!TextUtils.isEmpty(pawd)) {
                    if (user.equals(pawd)) {
                        SPUtils.put(RegesitActivity.this,"PAWD", user);
                        Toast.makeText(this, "用户有效，请获取注册码", Toast.LENGTH_SHORT).show();
                    }else {
                        register(user,encode);
                    }
                }else {
                    register(user,encode);
                }
                break;
            case R.id.bt_main:
                if (TextUtils.isEmpty(pawd)) {
                    Toast.makeText(this, "请先输入用户名", Toast.LENGTH_SHORT).show();
                }else {
                    String encode1 = MD5Utils.encode(pawd + "nj");
                    if (et_code.getText().toString().equals(encode1)) {
                        JMessageClient.login(pawd, encode1, new BasicCallback() {
                            @Override
                            public void gotResult(int responseCode, String LoginDesc) {
                                if (responseCode == 0) {
                                    //登录成功
                                    //进入主页并记录
                                    SPUtils.put(RegesitActivity.this,"count",true);
                                    Intent intent = new Intent(RegesitActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    //登录失败
                                    SPUtils.put(RegesitActivity.this,"count",false);
                                    Toast.makeText(RegesitActivity.this, "出错了，联系管理员解决", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }else {
                        SPUtils.put(this,"count",false);
                        Toast.makeText(this, "您输入的注册码不正确，请联系管理员获取", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.bt_see:
                Intent intent = new Intent(this, ImageWatchActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_get:
                showTiShiDialog();
                break;
            default:
                break;
        }
    }

    private void register(final String user, String encode) {
        if (user.length() > 20 || user.length() < 4) {
            Toast.makeText(this, "用户名长度应该在4到20之间", Toast.LENGTH_SHORT).show();
        }else {
            JMessageClient.register(user, encode, new BasicCallback() {
                @Override
                public void gotResult(int responseCode, String registerDesc) {
                    if (responseCode == 0) {
                        //注册成功
                        SPUtils.put(RegesitActivity.this,"PAWD", user);
                        Toast.makeText(RegesitActivity.this, "用户名有效，联系管理员获取注册码", Toast.LENGTH_SHORT).show();
                    }else {
                        //注册失败
                        Toast.makeText(RegesitActivity.this, "用户名已被使用，请重新输入", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void showTiShiDialog() {
        final AlertDialog.Builder tishiDialog = new AlertDialog.Builder(this);
        tishiDialog.setIcon(R.mipmap.ic_launcher);
        tishiDialog.setTitle("使用说明");
        tishiDialog.setMessage(R.string.copy);
        tishiDialog.setPositiveButton("复制QQ",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        cm.setText("2761215609");
                        Toast.makeText(RegesitActivity.this, "QQ复制成功", Toast.LENGTH_SHORT).show();
                    }
                });
        tishiDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                        dialog.dismiss();
                    }
                });
        // 显示
        tishiDialog.show();
    }

}
