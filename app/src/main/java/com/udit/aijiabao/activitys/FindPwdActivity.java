package com.udit.aijiabao.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.udit.aijiabao.BaseActivity;
import com.udit.aijiabao.R;
import com.udit.aijiabao.configs.Constants;
import com.udit.aijiabao.configs.UrlConfig;
import com.udit.aijiabao.dialog.LoadDialog;
import com.udit.aijiabao.dialog.ToastDialog;
import com.udit.aijiabao.entitys.RespBookInfo;
import com.udit.aijiabao.utils.T;
import com.udit.aijiabao.widgets.ClearEditText;
import com.udit.aijiabao.widgets.TitleView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/5/18.
 */

@ContentView(R.layout.activity_findpass)
public class FindPwdActivity extends BaseActivity {

    private final String TAG = FindPwdActivity.class.getSimpleName();

    @ViewInject(R.id.titleView)
    private TitleView mtitleView;

    @ViewInject(R.id.timer)
    private TextView timeTxt;

    @ViewInject(R.id.btn_send_ms)
    private TextView btnSend;

    @ViewInject(R.id.et_phone)
    private ClearEditText etPhone;

    @ViewInject(R.id.code_edit)
    private ClearEditText etCode;

    private CountDownTimer mTimer;

    protected boolean isRunning;

    private String account;

    private String msCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isRunning = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isRunning = false;
    }


    @Override
    public void setContentView() {
        mtitleView.setTitleLayoutColor(getResources().getColor(R.color.blue_title));
        mtitleView.setTitleText("密码找回");
        mtitleView.hideOperate();
        mtitleView.showBack(true);
        mtitleView.setTitleTextColor(getResources().getColor(R.color.white));
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Event(value = {R.id.btn_send_ms, R.id.finish_btn})
    private void clicks(View view) {
        switch (view.getId()) {
            case R.id.btn_send_ms:
                verify();
                break;

            case R.id.finish_btn:
                verifySign();
                break;

        }

    }

    private void verifySign() {

        account = etPhone.getText().toString().trim();
        msCode = etCode.getText().toString().trim();

        if (TextUtils.isEmpty(account)) {
            new ToastDialog(this).builder().setTip2("请先获取短信验证码！").show();
        } else if (TextUtils.isEmpty(msCode)) {
            new ToastDialog(this).builder().setTip2("请输入短信验证码！").show();
        } else {
            sign(account, msCode);
        }
    }

    private void sign(final String account, String msCode){
        RequestParams params=new RequestParams(UrlConfig.VERIFY_PHONE_CODE_URL);
        params.addBodyParameter("token",Constants.TOKEN);
        params.addBodyParameter("mobile",account);
        params.addBodyParameter("password_token",msCode);

        LoadDialog.showLoad(this,"正在验证,请稍后!",null);

        x.http().post(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Log.e(TAG,"onSuccess"+result);
                Bundle bundle=new Bundle();
                bundle.putString("phone",account);
                startActivity(SetPassActivity.class,bundle);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG,"onError"+ex.getMessage());
                T.show(FindPwdActivity.this,"请检查手机号和验证码是否正确！");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                LoadDialog.close();
            }
        });


    }


    private void verify() {
        account = etPhone.getText().toString().trim();

        Spanned error;
        if (TextUtils.isEmpty(account)) {
            error = Html.fromHtml("<font color='red'>"
                    + "请输入您绑定的手机号！" + "</font>");
            etPhone.setError(error);
        }  else if (account.length() < 11) {
            error = Html.fromHtml("<font color='red'>"
                    + "手机号码不正确！" + "</font>");
            etPhone.setError(error);
        } else {
            sendVerifyCode(account);
        }
    }


    /**
     * 发送验证码
     */

    private void sendVerifyCode(String phone){

        RequestParams params=new RequestParams(UrlConfig.FINGPASS_SEND_MS_URL);

        params.addBodyParameter("token", Constants.TOKEN);

        params.addBodyParameter("mobile",phone);

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG,"onSuccess"+result);
                timeDown();

                RespBookInfo info = new Gson().fromJson(result, RespBookInfo.class);

                if (null != info) {
                    if (!TextUtils.isEmpty(info.getMessage())) {
                        T.show(FindPwdActivity.this, info.getMessage());
                    } else if (!TextUtils.isEmpty(info.getError())) {
                        T.show(FindPwdActivity.this, info.getError());
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG,"onError"+ex.getMessage());
                T.show(FindPwdActivity.this,"号码不存在！");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    private void timeDown() {

        mTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (!isRunning) {
                    return;
                } else {
                    btnSend.setBackgroundResource(R.drawable.red_circle_frame);
                    btnSend.setClickable(false);
                    timeTxt.setText(millisUntilFinished / 1000 + "秒");
                }
            }

            @Override
            public void onFinish() {
                if (!isRunning) {
                    return;
                } else {
                    btnSend.setClickable(true);
                    timeTxt.setTextColor(getResources().getColor(R.color.gray_40));
                    timeTxt.setText("60秒");
                    btnSend.setBackgroundResource(R.drawable.blue_circle_frame);
                    mTimer.cancel();
                    mTimer = null;
                }
            }
        }.start();

    }

}
