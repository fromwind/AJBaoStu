package com.udit.aijiabao.activitys;

import android.text.TextUtils;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.udit.aijiabao.BaseActivity;
import com.udit.aijiabao.MainActivity;
import com.udit.aijiabao.R;
import com.udit.aijiabao.User;
import com.udit.aijiabao.configs.Constants;
import com.udit.aijiabao.configs.UrlConfig;
import com.udit.aijiabao.entitys.RespLogin;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by Administrator on 2016/5/23.
 */

@ContentView(R.layout.activity_wel)
public class WelActivity extends BaseActivity{

    private final String TAG = WelActivity.class.getSimpleName();

    @ViewInject(R.id.iv_welcome)
    private ImageView welcome;

    private AlphaAnimation alphaAnimation;

    private boolean isPause = false;

    private String name,pass;

    @Override
    public void setContentView() {

        name= User.getLocalUserName();

        pass=User.getLocalUserPass();
    }

    @Override
    public void initView() {
        alphaAnimation = new AlphaAnimation(0.3f, 1.0f);
        alphaAnimation.setDuration(2 * 1000);

        welcome.startAnimation(alphaAnimation);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (!TextUtils.isEmpty(name)&&!TextUtils.isEmpty(pass)) {
                    login(name,pass);
                } else {
                    enterHome();
                }
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isPause)
            enterHome();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPause = true;
    }

    private void enterHome() {
        startActivity(MainActivity.class);
        finish();
    }

    private void login(final String account, final String pwd) {

        RequestParams params = new RequestParams(UrlConfig.LOGIN_URL);

        params.addBodyParameter("user_type" , Constants.USER_TYPE);
        params.addBodyParameter("method", "login");
        params.addBodyParameter("mobile", account);
        params.addBodyParameter("password", pwd);

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess" + result);

                RespLogin login = new Gson().fromJson(result, RespLogin.class);

                Log.e(TAG, "" + login.toString());

                if (null != login) {
                    if (login.isResult()) {

                        User.clearUserInfo();

                        User.setLocalUserName(account);
                        User.setLocalUserPass(pwd);

                        User.saveUserInfo(login);

                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError" + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.e(TAG, "onCancelled");
            }

            @Override
            public void onFinished() {
                enterHome();
                Log.e(TAG, "onFinished");
            }
        });
    }

}
