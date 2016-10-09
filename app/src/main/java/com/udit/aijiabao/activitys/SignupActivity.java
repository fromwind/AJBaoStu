package com.udit.aijiabao.activitys;

import android.os.CountDownTimer;
import android.support.design.widget.TabLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.udit.aijiabao.ActivityCollector;
import com.udit.aijiabao.BaseActivity;
import com.udit.aijiabao.R;
import com.udit.aijiabao.User;
import com.udit.aijiabao.configs.Constants;
import com.udit.aijiabao.configs.UrlConfig;
import com.udit.aijiabao.dialog.LoadDialog;
import com.udit.aijiabao.dialog.ToastDialog;
import com.udit.aijiabao.entitys.RespBookInfo;
import com.udit.aijiabao.entitys.RespLogin;
import com.udit.aijiabao.entitys.RespSignInfo;
import com.udit.aijiabao.utils.T;
import com.udit.aijiabao.widgets.ClearEditText;
import com.udit.aijiabao.widgets.TitleView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by Administrator on 2016/5/18.
 */

@ContentView(R.layout.activity_signup)
public class SignupActivity extends BaseActivity {

    private final String TAG = SignupActivity.class.getSimpleName();

    @ViewInject(R.id.titleView)
    private TitleView mtitleView;

    @ViewInject(R.id.signup_text_phone)
    private ClearEditText phoneT;

    @ViewInject(R.id.signup_text_pwd)
    private ClearEditText passT;

    @ViewInject(R.id.signup_ms_authcode)
    private ClearEditText msCodeT;

    @ViewInject(R.id.signup_send_authcode)
    private TextView sendMS;

    private String phone;
    private String pass;
    private String msCode;


    @Override
    public void setContentView() {
        mtitleView.setTitleLayoutColor(getResources().getColor(R.color.blue_title));
        mtitleView.setTitleText("注册");
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

    @Event(value = {R.id.signup_send_authcode, R.id.signup_btn})
    private void click(View view) {
        switch (view.getId()) {
            case R.id.signup_send_authcode:
                verifyPhone();
                break;

            case R.id.signup_btn:
                verifySign();
                break;
        }
    }

    private void verifySign() {

        phone = phoneT.getText().toString().trim();
        pass = passT.getText().toString().trim();
        msCode = msCodeT.getText().toString().trim();

        if (TextUtils.isEmpty(phone)) {
            new ToastDialog(this).builder().setTip2("请先获取短信验证码！").show();
        } else if (TextUtils.isEmpty(pass)) {
            new ToastDialog(this).builder().setTip2("请输入密码！").show();
        } else if (pass.length() < 6) {
            new ToastDialog(this).builder().setTip2("密码长度至少为6！").show();
        } else if (TextUtils.isEmpty(msCode)) {
            new ToastDialog(this).builder().setTip2("请输入短信验证码！").show();
        } else {
            sign(phone, pass, msCode);
        }
    }

    private void sign(final String ph, final String pa, String mscode) {

        RequestParams params = new RequestParams(UrlConfig.SIGN_URL);

        params.addBodyParameter("mobile", ph);

        params.addBodyParameter("token", Constants.TOKEN);

        params.addBodyParameter("password", pa);
        params.addBodyParameter("verification_token", mscode);
        params.addBodyParameter("user_name",ph);

        LoadDialog.showLoad(this,"正在注册，请稍后！",null);

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess" + result);

                RespSignInfo signInfo=new Gson().fromJson(result,RespSignInfo.class);
                if(null!=signInfo) {
                    T.show(SignupActivity.this, "注册成功！");
                    User.clearUserInfo();
                    User.setLocalUserName(ph);
                    User.setLocalUserPass(pa);

                    RespLogin login = new RespLogin();

                    login.setAuth_token(signInfo.getAuth_token());
                    login.setId(signInfo.getId());

                    login.setUser_name(signInfo.getUser_name());

                    login.setMobile(signInfo.getMobile());

                    User.saveUserInfo(login);
                    LoadDialog.close();
                    finish();
                    ActivityCollector.finishActivity(LoginActivity.class);
                    overridePendingTransition(R.anim.push_left_to_right_in,
                            R.anim.push_left_to_right_out);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError:" + ex.getMessage());
                T.show(SignupActivity.this, "注册失败，可能您已经注册了！");
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

    private void verifyPhone() {

        phone = phoneT.getText().toString().trim();

        if (TextUtils.isEmpty(phone)) {
            new ToastDialog(this).builder().setTip2("请输入合法的手机号码！").show();
        } else {
            sendMS(phone);
        }
    }

    private void sendMS(String phone) {

        RequestParams params = new RequestParams(UrlConfig.SEND_MS_CODE);

        params.addBodyParameter("mobile", phone);

        params.addBodyParameter("token", Constants.TOKEN);

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess" + result);

                timeCount();

                RespBookInfo info = new Gson().fromJson(result, RespBookInfo.class);

                if (null != info) {
                    if (!TextUtils.isEmpty(info.getMessage())) {
                        T.show(SignupActivity.this, info.getMessage());
                    } else if (!TextUtils.isEmpty(info.getError())) {
                        T.show(SignupActivity.this, info.getError());
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError" + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    private void timeCount() {
        new CountDownTimer(60000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

                sendMS.setBackgroundResource(R.drawable.red_circle_frame);
                sendMS.setClickable(false);

                sendMS.setTextColor(getResources().getColor(R.color.main_red));
                sendMS.setText(millisUntilFinished / 1000 + "秒");

            }

            @Override
            public void onFinish() {
                sendMS.setClickable(true);
                sendMS.setTextColor(getResources().getColor(R.color.gray_40));
                sendMS.setText("重新发送");
                sendMS.setBackgroundResource(R.drawable.blue_circle_frame);
            }
        }.start();
    }

}
