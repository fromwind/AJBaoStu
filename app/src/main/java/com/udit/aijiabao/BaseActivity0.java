package com.udit.aijiabao;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;

import org.xutils.x;

public abstract class BaseActivity0 extends Activity {
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
		overridePendingTransition(R.anim.bottom_,
				R.anim.bottom_close);
	}

	public void startActivity(Class Class){
		Intent intent=new Intent(this,Class);
		startActivity(intent);
		overridePendingTransition(R.anim.push_right_to_left_in,
				R.anim.push_right_to_left_out);
	}

	public void startActivity(Class Class,Bundle bundle){
		Intent intent=new Intent(this,Class);
		intent.putExtra("bundle",bundle);
		startActivity(intent);
		overridePendingTransition(R.anim.push_right_to_left_in,
				R.anim.push_right_to_left_out);
	}

}
