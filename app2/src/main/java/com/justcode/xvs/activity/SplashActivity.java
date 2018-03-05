package com.justcode.xvs.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.justcode.xvs.PermissionHelper;
import com.justcode.xvs.R;
import com.justcode.xvs.util.MD5Utils;
import com.justcode.xvs.util.SPUtils;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

/**
 * 开屏广告演示窗口
 *
 * @author Alian Lee
 * @since 2016-11-25
 */
public class SplashActivity extends Activity {
	private static final int GO_MAIN = 0;//去主页
	private static final int GO_REGESIT = 1;//去注册页
	private PermissionHelper mPermissionHelper;
	/**
	 * 跳转判断
	 */
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case GO_MAIN://去主页
					UserInfo myInfo = JMessageClient.getMyInfo();
					if (myInfo != null) {
						Intent intent = new Intent(SplashActivity.this, MainActivity.class);
						startActivity(intent);
						finish();
					} else {
						String pawd = SPUtils.getString(SplashActivity.this, "PAWD");
						JMessageClient.login(pawd, MD5Utils.encode(pawd+"nj"), new BasicCallback() {

							@Override
							public void gotResult(int responseCode, String LoginDesc) {
								if (responseCode == 0) {
									Log.e("SplashActivity", "走3");
									Intent intent = new Intent(SplashActivity.this, MainActivity.class);
									startActivity(intent);
									finish();
								}
							}
						});
					}
					break;
				case GO_REGESIT://去注册页
					Intent intent2 = new Intent(SplashActivity.this, RegesitActivity.class);
					startActivity(intent2);
					finish();
					break;
				default:
					break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		// 设置全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// 移除标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);
		
		// 当系统为6.0以上时，需要申请权限
		mPermissionHelper = new PermissionHelper(this);
		mPermissionHelper.setOnApplyPermissionListener(new PermissionHelper.OnApplyPermissionListener() {
			@Override
			public void onAfterApplyAllPermission() {
				runApp();
			}
		});
		if (Build.VERSION.SDK_INT < 23) {
			// 如果系统版本低于23，直接跑应用的逻辑
			runApp();
		} else {
			// 如果权限全部申请了，那就直接跑应用逻辑
			if (mPermissionHelper.isAllRequestedPermissionGranted()) {
				runApp();
			} else {
				// 如果还有权限为申请，而且系统版本大于23，执行申请权限逻辑
				mPermissionHelper.applyPermissions();
			}
		}
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		mPermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		mPermissionHelper.onActivityResult(requestCode, resultCode, data);
	}
	
	/**
	 * 跑应用的逻辑
	 */
	private void runApp() {
//		Intent intent = new Intent(this, MainActivity.class);
//		startActivity(intent);

		//判断sp中是否有密码
		String password = SPUtils.getString(this, "PAWD");

		if (!TextUtils.isEmpty(password)){
			//判断是否输入过正确密码
			boolean count = SPUtils.getBoolean(this, "count");
			if (count){
				//去主页
				mHandler.sendEmptyMessageDelayed(GO_MAIN, 2000);
			}else {
				//去注册页
				mHandler.sendEmptyMessageAtTime(GO_REGESIT, 2000);
			}
		}else {
			//去注册界面
			mHandler.sendEmptyMessageAtTime(GO_REGESIT, 2000);
		}
	}

}
