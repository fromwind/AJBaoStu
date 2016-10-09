package com.udit.aijiabao.widgets;

import android.annotation.TargetApi;
import android.app.Instrumentation;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.udit.aijiabao.R;

public class TitleView extends LinearLayout {

	private ImageView mBackView;
	private TextView mTitleView;
	private TextView mOperateView;
	private ImageView mOperateImgView;
	private RelativeLayout titleLayout;
//	private View v;

	public TitleView(Context context) {
		this(context, null);
	}

	public TitleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public TitleView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView(context);
	}

	private void initView(Context context) {
		View.inflate(context, R.layout.title_layout, this);
		if (isInEditMode())
			return;
		mBackView = (ImageView) findViewById(R.id.title_back);
		mBackView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new Thread() {
					public void run() {
						Instrumentation inst = new Instrumentation();
						inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
					}
				}.start();
			}
		});
		mTitleView = (TextView) findViewById(R.id.title_name);
		mOperateView = (TextView) findViewById(R.id.title_operate);
		mOperateImgView = (ImageView) findViewById(R.id.title_operate_img);
		
		titleLayout=(RelativeLayout) findViewById(R.id.title_layout);
//		v=findViewById(R.id.title_line);
	}

	/**
	 * 设置标题栏标题内容
	 */
	public void setTitleText(CharSequence text) {
		mTitleView.setText(text);
	}
	
	/**
	 * 设置标题栏标题内容颜色
	 */
	public void setTitleTextColor(int color) {
		mTitleView.setTextColor(color);
	}
	
	/**
	 * 设置标题栏标题颜色
	 */
	public void setTitleLayoutColor(int color) {
		titleLayout.setBackgroundColor(color);
	}
	
	public void setTitleLineGone() {
//		v.setVisibility(View.GONE);
	}

	/**
	 * 设置标题栏返回按钮监听事件，不设置则是back
	 */
	public void setBackOnClick(OnClickListener listener) {
		mBackView.setOnClickListener(listener);
	}

	/**
	 * 设置标题栏右侧按钮监听事件
	 */
	public void setOperateOnClick(OnClickListener listener) {
		mOperateImgView.setOnClickListener(listener);
		mOperateView.setOnClickListener(listener);
	}

	/**
	 * 设置是否显示返回按钮，默认显示
	 */
	public void showBack(boolean show) {
		mBackView.setVisibility(show ? VISIBLE : GONE);
	}

	/**
	 * 设置返回按钮图标
	 */
	public void setBackImage(int res) {
		mBackView.setImageResource(res);
	}

	/**
	 * 隐藏右侧操作按钮，默认显示
	 */
	public void hideOperate() {
		mOperateView.setVisibility(GONE);
		mOperateImgView.setVisibility(GONE);
	}

	/**
	 * 设置标题栏右侧按钮文本
	 */
	public void setOperateText(CharSequence text) {
		mOperateView.setBackgroundColor(Color.TRANSPARENT);
		mOperateView.setText(text);
		mOperateView.setVisibility(VISIBLE);
		mOperateImgView.setVisibility(GONE);
	}

	/**
	 * 设置标题栏右侧按钮监图标
	 */
	public void setOperateImage(int res) {
		mOperateView.setVisibility(GONE);
		mOperateImgView.setVisibility(VISIBLE);
		mOperateImgView.setImageResource(res);
	}

}
