package com.udit.aijiabao.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.udit.aijiabao.R;


/**
 * Created by maplejaw on 2015/8/27.
 */
public class ToastDialog {
    private Context context;
    private Dialog dialog;
    private LinearLayout lLayout_bg;
    private TextView tvTip2;

    private Display display;


    public ToastDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public ToastDialog builder() {
        // 获取Dialog布局
        View view = LayoutInflater.from(context).inflate(
                R.layout.toast_layout, null);

        // 获取自定义Dialog布局中的控件
        tvTip2 = (TextView) view.findViewById(R.id.tv_tip2);
        lLayout_bg = (LinearLayout) view.findViewById(R.id.lLayout_bg);

        // 定义Dialog布局和参数
        dialog = new Dialog(context, R.style.AlertDialogStyle);
        dialog.setContentView(view);

        // 调整dialog背景大小
        lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (display
                .getWidth() * 0.60), LinearLayout.LayoutParams.WRAP_CONTENT));

        return this;
    }


    public ToastDialog setTip2(CharSequence tip2) {

        tvTip2.setText(tip2);
        return this;
    }


    public ToastDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }


    private void setLayout() {
    }

    public void show() {
        dialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, 1500);

    }
}
