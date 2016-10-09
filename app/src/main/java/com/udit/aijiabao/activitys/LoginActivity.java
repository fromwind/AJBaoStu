package com.udit.aijiabao.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.udit.aijiabao.BaseActivity;
import com.udit.aijiabao.R;
import com.udit.aijiabao.User;
import com.udit.aijiabao.configs.Constants;
import com.udit.aijiabao.configs.UrlConfig;
import com.udit.aijiabao.dialog.LoadDialog;
import com.udit.aijiabao.entitys.RespLogin;
import com.udit.aijiabao.widgets.ClearEditText;
import com.udit.aijiabao.widgets.TitleView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cc.zhipu.library.utils.T;

/**
 * Created by Administrator on 2016/5/12.
 */

@ContentView(R.layout.activity_login)
public class LoginActivity extends BaseActivity {

    private static final String TAG=LoginActivity.class.getSimpleName();

    @ViewInject(R.id.et_phone)
    private ClearEditText etPhone;
    @ViewInject(R.id.et_pwd)
    private ClearEditText etPwd;

    private String account;
    private String pwd;

    @Override
    public void setContentView() {
        View v1 = findViewById(R.id.login_Linelayout2);//找到你要设透明背景的layout 的id
        v1.getBackground().setAlpha(100);//0~255透明度值
        View v = findViewById(R.id.login_Linelayout3);
        v.getBackground().setAlpha(100);
    }

    @Override
    public void initView() {
        etPhone.setInputType(InputType.TYPE_CLASS_NUMBER);
    }

    @Override
    public void initData() {
        if (!TextUtils.isEmpty(User.getLocalUserName())) {
            etPhone.setText(User.getLocalUserName());
        }
    }

    @Event(value = {R.id.mLogin_btn,R.id.text_login_find_pwd, R.id.text_login_sign_up}, type = View.OnClickListener.class)
    private void click(View v) {
        switch (v.getId()) {
            case R.id.mLogin_btn:
                verify();
                break;

            case R.id.text_login_find_pwd:
                startActivity(FindPwdActivity.class);
                break;

            case R.id.text_login_sign_up:
                startActivity(SignupActivity.class);
                break;
        }
    }

    /**
     * 判断是否为手机号
     *
     * @param num
     * @return
     */
    public boolean isPhoneNum(String num) {
        String reg = "^1[34758][0-9]\\d{8}$";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(num);
        if (matcher.find()) {
            return true;
        }
        return false;
    }

    /**
     * 验证
     */
    private void verify() {
        account = etPhone.getText().toString().trim();
        pwd = etPwd.getText().toString().trim();
        Spanned error;
        if (TextUtils.isEmpty(account)) {
            error = Html.fromHtml("<font color='red'>"
                    + getString(R.string.login_user_name_is_null) + "</font>");
            etPhone.setError(error);
        } else if (TextUtils.isEmpty(pwd)) {
            error = Html.fromHtml("<font color='red'>"
                    + getString(R.string.login_user_pwd_is_null) + "</font>");
            etPwd.setError(error);
        } else if (pwd.length() < 6) {
            error = Html.fromHtml("<font color='red'>"
                    + getString(R.string.login_user_pwd_short) + "</font>");
            etPwd.setError(error);
        } else {
            login();
        }
    }

    /**
     * 登陆操作
     */
    private void login() {

        LoadDialog.showLoad(this, "正在登录，请稍后！", null);

        RequestParams params = new RequestParams(UrlConfig.LOGIN_URL);

        params.addBodyParameter("user_type" , Constants.USER_TYPE);
        params.addBodyParameter("method", "login");
        params.addBodyParameter("mobile", account);
        params.addBodyParameter("password", pwd);

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("CR", "onSuccess" + result);

                RespLogin login = new Gson().fromJson(result, RespLogin.class);

                Log.e("CR", "" + login.toString());

                if (null != login) {
                    if (login.isResult()) {
                        T.show(LoginActivity.this, "登录成功！");

                        User.clearUserInfo();

                        User.setLocalUserName(account);
                        User.setLocalUserPass(pwd);

                        User.saveUserInfo(login);
                        setResult(Constants.LOGIN_RESP);
                        finish();
                    } else {
                        T.show(LoginActivity.this, login.getMessage());
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("CR", "onError" + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.e("CR", "onCancelled");
            }

            @Override
            public void onFinished() {
                LoadDialog.close();
                Log.e("CR", "onFinished");
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy");
    }
}
