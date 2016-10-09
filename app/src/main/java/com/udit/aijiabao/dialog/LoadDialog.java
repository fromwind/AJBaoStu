package com.udit.aijiabao.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.udit.aijiabao.R;


/**
 * 
 * @author ayke create at 2014年9月7日 上午12:23:31
 */
public class LoadDialog extends Dialog {
	private static final String TAG = "LoadDialog";
	/**
	 * @param context
	 * @param theme
	 */
	private static LoadDialog loadDialog;
	private static String text = null;

	private LoadDialog(Context context, int theme) {
		super(context, theme);
	}

	public static void showLoad(Context context, String content,
			OnCancelListener listener) {
		if (null != context) {

			close();
			text = content;
			loadDialog = new LoadDialog(context, R.style.Dialog);
			if (null == listener) {
				loadDialog.setCancelable(false);
			} else {
				loadDialog.setCancelable(true);
				loadDialog.setOnCancelListener(listener);
			}
			loadDialog.show();
		}
	}

	public static void close() {
		if (null != loadDialog) {
			loadDialog.dismiss();
			text = null;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_loading_progress);
		if (null != text)
			((TextView) findViewById(R.id.pb_loading_content)).setText(text);
	}
}
