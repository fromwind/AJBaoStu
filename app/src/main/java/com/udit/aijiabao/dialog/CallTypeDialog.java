package com.udit.aijiabao.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.udit.aijiabao.R;


/**
 * 置业顾问点击 客户电话，弹出此窗口，选择系统拨号、销帮省钱电话拨号
 * 
 * @author Administrator
 *
 */
public class CallTypeDialog extends Dialog implements View.OnClickListener {
	private Context context;
	private TextView systemBtn;
	private TextView callBtn;

	private String phone;

	public CallTypeDialog(Context context, int theme, String phone) {
		super(context, theme);
		this.context = context;
		this.phone = phone;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_calltype);
		systemBtn = (TextView) findViewById(R.id.call_type_system);
		callBtn = (TextView) findViewById(R.id.call_type_sheng);

		systemBtn.setOnClickListener(this);
		callBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.call_type_system:// 系统拨号
			Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
					+ phone));
			context.startActivity(intent);
			dismiss();
			break;
		case R.id.call_type_sheng:// 省钱电话拨号
			dismiss();
			break;
		default:
			break;
		}
	}

}
