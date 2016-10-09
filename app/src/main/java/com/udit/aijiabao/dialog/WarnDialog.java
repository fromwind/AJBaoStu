package com.udit.aijiabao.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.udit.aijiabao.R;


/**
 * Created by maplejaw on 2015/8/27.
 */
public class WarnDialog {
	private Context context;
	private Dialog dialog;

	private BtnCallBack back;

	private LinearLayout lLayout_bg;
	private TextView tvTip1, tvTip2;


	private Display display;
	private TextView btn;

	public WarnDialog setBack(BtnCallBack back) {
		this.back = back;
		return this;
	}

	public WarnDialog(Context context) {
		this.context = context;
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		display = windowManager.getDefaultDisplay();
	}

	public WarnDialog builder() {
		// 获取Dialog布局
		View view = LayoutInflater.from(context).inflate(
				R.layout.save_error_dialog, null);

		// 获取自定义Dialog布局中的控件
		tvTip1 = (TextView) view.findViewById(R.id.tv_tip1);
		tvTip2 = (TextView) view.findViewById(R.id.tv_tip2);

		btn = (TextView) view.findViewById(R.id.btn_dialog_ok);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				if(null!=back)
				back.clickView();
			}
		});

		lLayout_bg = (LinearLayout) view.findViewById(R.id.lLayout_bg);

		// 定义Dialog布局和参数
		dialog = new Dialog(context,
				R.style.AlertDialogStyle);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setContentView(view);

		// 调整dialog背景大小
		lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (display
				.getWidth() * 0.80), LinearLayout.LayoutParams.WRAP_CONTENT));

		return this;
	}

	public WarnDialog setTip1(CharSequence tip1) {

		tvTip1.setText(tip1);
		return this;
	}

	public WarnDialog setTip2(CharSequence tip2) {

		tvTip2.setText(tip2);
		return this;
	}

	public WarnDialog setCancelable(boolean cancel) {
		dialog.setCancelable(cancel);
		return this;
	}

	public void show() {
		dialog.show();
		// new Handler().postDelayed(new Runnable() {
		// @Override
		// public void run() {
		// dialog.dismiss();
		// }
		// }, 1500);
	}

	public interface BtnCallBack {

		void clickView();

	}
}
