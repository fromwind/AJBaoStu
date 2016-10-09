package com.udit.aijiabao.activitys;

import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.udit.aijiabao.ActivityCollector;
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

/**
 * Created by Administrator on 2016/5/23.
 */

@ContentView(R.layout.activity_setpass)
public class SetPassActivity extends BaseActivity {

    private final String TAG = SetPassActivity.class.getSimpleName();

    @ViewInject(R.id.titleView)
    private TitleView mtitleView;

    @ViewInject(R.id.et_new_pwd)
    private ClearEditText et_new_pass;

    @ViewInject(R.id.et_confirm_new_pwd)
    private ClearEditText et_confirm_new_pass;

    private String phone,newPass,newPass2;


    @Override
    public void setContentView() {
        mtitleView.setTitleLayoutColor(getResources().getColor(R.color.blue_title));
        mtitleView.setTitleText("设置密码");
        mtitleView.hideOperate();
        mtitleView.showBack(true);
        mtitleView.setTitleTextColor(getResources().getColor(R.color.white));
        Bundle bundle = this.getIntent().getExtras();
        phone  = bundle.getString("phone");
    }

    @Override
    public void initView() {


    }

    @Override
    public void initData() {

    }

    @Event(value = R.id.confirm_btn)
    private void click(View view){
        verify();
    }

    private void verify(){
        newPass=et_new_pass.getText().toString().trim();
        newPass2=et_confirm_new_pass.getText().toString().trim();

        Spanned error;
        if (TextUtils.isEmpty(newPass)) {
            error = Html.fromHtml("<font color='red'>"
                    + "请输入新密码！" + "</font>");
            et_new_pass.setError(error);
        } else if(TextUtils.isEmpty(newPass2)){
            error = Html.fromHtml("<font color='red'>"
                    + "请确认新密码！" + "</font>");
            et_confirm_new_pass.setError(error);
        }
        else if (newPass.length() < 6) {
            error = Html.fromHtml("<font color='red'>"
                    + getString(R.string.login_user_pwd_short) + "</font>");
            et_new_pass.setError(error);
        } else if(!TextUtils.equals(newPass,newPass2)){
            new ToastDialog(this).builder().setTip2("请确保密码一致！").show();
        }else {
            Log.e("zoo","phone:"+phone);
            update(phone,newPass,newPass2);
        }
    }


    private void update(String phone, String newPass, String newPass2){

        RequestParams params=new RequestParams(UrlConfig.FIND_UPDATE_PASS);

        params.addBodyParameter("token", Constants.TOKEN);
        params.addBodyParameter("mobile",phone);
        params.addBodyParameter("password",newPass);
        params.addBodyParameter("password_confirmation",newPass2);

        LoadDialog.showLoad(this,"正在修改，请稍后！",null);

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess" + result);

                RespBookInfo info = new Gson().fromJson(result, RespBookInfo.class);

                if (null != info) {
                    if (!TextUtils.isEmpty(info.getMessage())) {
                        T.show(SetPassActivity.this, info.getMessage());
                    } else if (!TextUtils.isEmpty(info.getError())) {
                        T.show(SetPassActivity.this, info.getError());
                    }
                }
                finish();
                ActivityCollector.finishActivity(FindPwdActivity.class);
                overridePendingTransition(R.anim.push_left_to_right_in,
                        R.anim.push_left_to_right_out);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG,"onError"+ex.getMessage());
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
}
