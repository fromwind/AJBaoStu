package com.udit.aijiabao;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;

public abstract class FBaseActivity extends FragmentActivity {

	private static final String TAG = "BActivity";
	InputMethodManager manager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
		intent.putExtra("bundle",bundle);
		startActivity(intent);
		overridePendingTransition(R.anim.push_right_to_left_in,
				R.anim.push_right_to_left_out);
	}
}
