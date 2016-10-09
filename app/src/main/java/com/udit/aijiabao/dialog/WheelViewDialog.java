package com.udit.aijiabao.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.udit.aijiabao.R;
import com.udit.aijiabao.widgets.WheelView;

import java.util.Arrays;

/**
 * Created by Administrator on 2016/5/19.
 */
public class WheelViewDialog {

    public static Dialog showDialog(Context mContext,String[] PLANETS, final DialogItemClickListener listener){
        final Dialog dialog = new Dialog(mContext, R.style.DialogStyle);

        View view = LayoutInflater.from(mContext).inflate(
                R.layout.dialog_wheel, null);

        dialog.setContentView(view);

        WheelView wv = (WheelView) view.findViewById(R.id.wheel_view_wv);

        wv.setOffset(2);

        wv.setItems(Arrays.asList(PLANETS));

        wv.setSeletion(3);

        wv.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                listener.confirm(item);
            }
        });

        Window mWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = mWindow.getAttributes();
        lp.width = getScreenWidth(mContext);
        mWindow.setGravity(Gravity.BOTTOM);
        // 添加动画
        mWindow.setWindowAnimations(R.style.dialogAnim);
        mWindow.setAttributes(lp);
        dialog.show();

        return dialog;
    }

    public static Dialog showDialog0(Context mContext,String[] PLANETS, final DialogItemClickListener listener){

        View view = LayoutInflater.from(mContext).inflate(
                R.layout.dialog_wheel, null);


        WheelView wv = (WheelView) view.findViewById(R.id.wheel_view_wv);
        wv.setOffset(2);
        wv.setItems(Arrays.asList(PLANETS));

        wv.setSeletion(3);
        wv.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, final String item) {



            }
        });

        AlertDialog dialog= new AlertDialog.Builder(mContext)
                .setView(view)
                .setPositiveButton("完成", null)
                .show();

        Window mWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = mWindow.getAttributes();
        lp.width = getScreenWidth(mContext);
        mWindow.setGravity(Gravity.BOTTOM);
        // 添加动画
        mWindow.setWindowAnimations(R.style.dialogAnim);
        mWindow.setAttributes(lp);

        return dialog;
    }


    /**
     * 获取屏幕分辨率宽
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay()
                .getMetrics(dm);
        return dm.widthPixels;
    }

    public interface DialogItemClickListener {
        public abstract void confirm(String result);
    }

}
