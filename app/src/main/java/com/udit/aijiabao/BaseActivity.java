package com.udit.aijiabao;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.xutils.x;

public abstract class BaseActivity extends Activity {
	private static final String TAG = "BActivity";
	InputMethodManager manager;
	protected boolean isRunning;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		x.view().inject(this);
		isRunning = true;
		ActivityCollector.addActivity(this);
		manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		setContentView();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			//透明状态栏
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			//透明导航栏
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
			SystemBarTintManager tintManager = new SystemBarTintManager(this);
			// 激活状态栏
			tintManager.setStatusBarTintEnabled(true);
			// enable navigation bar tint 激活导航栏
			tintManager.setNavigationBarTintEnabled(true);
			//设置系统栏设置颜色
			//tintManager.setTintColor(R.color.red);
			//给状态栏设置颜色
			tintManager.setStatusBarTintResource(R.color.blue_title);
			//Apply the specified drawable or color resource to the system navigation bar.
			//给导航栏设置资源
			tintManager.setNavigationBarTintResource(R.color.blue_title);
		}
		initView();
		initData();
	}

	public abstract void setContentView();

	public abstract void initView();

	public abstract void initData();

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (getCurrentFocus() != null
					&& getCurrentFocus().getWindowToken() != null) {
				manager.hideSoftInputFromWindow(getCurrentFocus()
						.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}
		return super.onTouchEvent(event);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		isRunning = false;
		ActivityCollector.removeActivity(this);
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.push_left_to_right_in,
				R.anim.push_left_to_right_out);
	}

	public void startActivity(Class Class){
		Intent intent=new Intent(this,Class);
		startActivity(intent);
		overridePendingTransition(R.anim.push_right_to_left_in,
				R.anim.push_right_to_left_out);
	}

	public void startActivity(Class Class,Bundle bundle){
		Intent intent=new Intent(this,Class);
		intent.putExtras(bundle);
		startActivity(intent);
		overridePendingTransition(R.anim.push_right_to_left_in,
				R.anim.push_right_to_left_out);
	}

}
