package com.udit.aijiabao.activitys;

import android.text.Html;
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
import com.udit.aijiabao.dialog.ToastDialog;
import com.udit.aijiabao.entitys.RespUpdate;
import com.udit.aijiabao.utils.T;
import com.udit.aijiabao.widgets.ClearEditText;
import com.udit.aijiabao.widgets.TitleView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by Administrator on 2016/5/18.
 */

@ContentView(R.layout.activity_updatepass)
public class UpdatePassActivty extends BaseActivity {

    private final String TAG = UpdatePassActivty.class.getSimpleName();

    @ViewInject(R.id.titleView)
    private TitleView mtitleView;

    @ViewInject(R.id.old_pass_edit)
    private ClearEditText oldPassEdit;

    @ViewInject(R.id.new_pass_edit)
    private ClearEditText newPassEdit;

    @ViewInject(R.id.confirm_pass_edit)
    private ClearEditText confirmNewPassEdit;

    private String oldPass;

    private String newPass;

    private String CnewPass;


    @Override
    public void setContentView() {

    }

    @Override
    public void initView() {
        mtitleView.setTitleLayoutColor(getResources().getColor(R.color.blue_title));
        mtitleView.setTitleText("修改密码");
        mtitleView.showBack(true);
        mtitleView.setTitleTextColor(getResources().getColor(R.color.white));
        mtitleView.setOperateText("确定");
        mtitleView.setOperateOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verify();
            }
        });
    }

    @Override
    public void initData() {

    }

    /**
     * 验证
     */
    private void verify() {
        oldPass = oldPassEdit.getText().toString().trim();
        newPass = newPassEdit.getText().toString().trim();
        CnewPass = confirmNewPassEdit.getText().toString().trim();

        Spanned error;
        if (TextUtils.isEmpty(oldPass)) {
            error = Html.fromHtml("<font color='red'>"
                    + "旧密码不能为空！" + "</font>");
            oldPassEdit.setError(error);
        } else if (TextUtils.isEmpty(newPass)) {
            error = Html.fromHtml("<font color='red'>"
                    + "新密码不能为空！" + "</font>");
            newPassEdit.setError(error);
        } else if (TextUtils.isEmpty(CnewPass)) {
            error = Html.fromHtml("<font color='red'>"
                    + "请确认新密码！" + "</font>");
            confirmNewPassEdit.setError(error);
        } else if (newPass.length() < 6) {
            error = Html.fromHtml("<font color='red'>"
                    + getString(R.string.login_user_pwd_short) + "</font>");
            newPassEdit.setError(error);
        } else {
            if (!TextUtils.equals(oldPass, User.getLocalUserPass())) {
                new ToastDialog(this).builder().setTip2("旧密码不正确").show();
            } else if (!TextUtils.equals(newPass, CnewPass)) {
                new ToastDialog(this).builder().setTip2("新密码不一致！").show();
            } else {
                updatePass(newPass, CnewPass);
            }
        }
    }

    private void updatePass(final String newPass, String cnewPass) {

        RequestParams params = new RequestParams(UrlConfig.UPDATE_PASS_URL);

        Log.e(TAG, "params" + newPass);
        Log.e(TAG, "params" + cnewPass);
        Log.e(TAG, "params" + User.getPhoneNumber());

        LoadDialog.showLoad(this,"正在修改，请稍等！",null);

        params.addBodyParameter("mobile", User.getPhoneNumber());

        params.addBodyParameter("password", newPass);

        params.addBodyParameter("password_confirmation", cnewPass);

        params.addBodyParameter("token", Constants.TOKEN);

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "result:" + result);

                RespUpdate update=new Gson().fromJson(result,RespUpdate.class);

                User.setLocalUserPass(newPass);

                T.show(UpdatePassActivty.this,update.getMessage());

                onBackPressed();
                finish();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "ex:" + ex.getMessage());
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
