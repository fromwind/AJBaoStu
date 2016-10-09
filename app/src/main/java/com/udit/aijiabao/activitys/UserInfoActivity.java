package com.udit.aijiabao.activitys;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.udit.aijiabao.BaseActivity;
import com.udit.aijiabao.R;
import com.udit.aijiabao.User;
import com.udit.aijiabao.configs.Constants;
import com.udit.aijiabao.configs.UrlConfig;
import com.udit.aijiabao.dialog.JDialog;
import com.udit.aijiabao.dialog.LoadDialog;
import com.udit.aijiabao.dialog.WheelViewDialog;
import com.udit.aijiabao.entitys.RespBookInfo;
import com.udit.aijiabao.entitys.RespUserInfo;
import com.udit.aijiabao.utils.PreferencesUtils;
import com.udit.aijiabao.utils.T;
import com.udit.aijiabao.widgets.ClearEditText;
import com.udit.aijiabao.widgets.TitleView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/5/18.
 */
@ContentView(R.layout.activity_personinfo)
public class UserInfoActivity extends BaseActivity {

    private final String TAG = UserInfoActivity.class.getSimpleName();

    @ViewInject(R.id.titleView)
    private TitleView mtitleView;

    @ViewInject(R.id.name)
    private TextView name;

    @ViewInject(R.id.sex)
    private TextView sex;

    @ViewInject(R.id.nicheng)
    private ClearEditText nicheng;

    @ViewInject(R.id.huiyuan)
    private TextView huiyuan;

    @ViewInject(R.id.phone)
    private TextView phone;

    @ViewInject(R.id.id_card)
    private ClearEditText idCard;

    @ViewInject(R.id.weixin)
    private ClearEditText weixin;

    @ViewInject(R.id.email)
    private ClearEditText email;

    @ViewInject(R.id.adress)
    private ClearEditText adress;

    @ViewInject(R.id.jiazhaoleixin)
    private TextView carType;

    @ViewInject(R.id.baomRiqi)
    private TextView data;

    @Override
    public void setContentView() {

    }

    @Override
    public void initView() {
        mtitleView.setTitleLayoutColor(getResources().getColor(R.color.blue_title));
        mtitleView.setTitleText("我的资料");
        mtitleView.setOperateText("完成");
        mtitleView.setOperateOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInfo(name.getText().toString().trim(),
                        sex.getText().toString().trim(),
                        phone.getText().toString().trim(),
                        nicheng.getText().toString().trim()
                        ,email.getText().toString().trim(),
                        idCard.getText().toString()
                        ,weixin.getText().toString()
                        ,adress.getText().toString().trim()
                        ,carType.getText().toString()
                        ,data.getText().toString());
            }
        });
        mtitleView.showBack(true);
        mtitleView.setTitleTextColor(getResources().getColor(R.color.white));
    }

    @Override
    public void initData() {

    }

    @Event(value = {R.id.sex_layout, R.id.cartypr_layout})
    private void clicks(View view) {

        switch (view.getId()){

            case R.id.sex_layout:

                JDialog.showDialog(this, "选择性别", new String[]{"女", "男"}, new JDialog.DialogItemClickListener() {
                    @Override
                    public void confirm(String result) {
                        sex.setText(result);
                    }
                });

                break;

            case R.id.cartypr_layout:

                WheelViewDialog.showDialog(this, new String[]{"B1","B2","C1","C2","D1"}, new WheelViewDialog.DialogItemClickListener() {
                    @Override
                    public void confirm(String result) {
                        carType.setText(result);
                    }
                });


                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        getInfo();
    }

    private void getInfo() {

        RequestParams params = new RequestParams(UrlConfig.HOME_STUFFINFO_URL);
        params.addBodyParameter("method", "query");
        params.addBodyParameter("user_type", Constants.USER_TYPE);

        params.addBodyParameter("token", User.getAuthToken());
        params.addBodyParameter("userId", String.valueOf(User.getUserId()));
        Log.e(TAG, "params_Token:" + User.getAuthToken() + "---UserId:" + User.getUserId());

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "getInfo_onSuccess:" + result);
                RespUserInfo info = new Gson().fromJson(result, RespUserInfo.class);
                bindDatas(info);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "getInfo_onError:" + ex.getMessage());
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

    private void bindDatas(RespUserInfo info) {

        if (null != info) {
            Log.e("zoo","info:"+info.toString());
            name.setText(info.getName());

            sex.setText((info.getSex() == 1) ? "男" : "女");

            nicheng.setText(info.getNickname());

            huiyuan.setText("普通会员");

            phone.setText(info.getTel());

            idCard.setText(info.getCard_code());
            weixin.setText("");

            email.setText(info.getEmail());

            adress.setText(info.getLink_address());

            carType.setText(info.getLevel());

            data.setText(info.getCreated_at());
        }
    }

    private void saveInfo(String name,String sex,String tel,String nickname,String email,String card_code,String openid,String link_address,String level,String created_at){

        RequestParams params = new RequestParams(UrlConfig.HOME_STUFFINFO_URL);

        params.addBodyParameter("method", "save");
        params.addBodyParameter("user_type", Constants.USER_TYPE);

        params.addBodyParameter("member_id", User.getMemberId());
        params.addBodyParameter("corp_id", PreferencesUtils.getString(Constants.CORP_ID, ""));

        params.addBodyParameter("token", User.getAuthToken());
        params.addBodyParameter("userId", String.valueOf(User.getUserId()));


        params.addBodyParameter("name",name);
        params.addBodyParameter("sex",TextUtils.equals(sex,"男")?"1":"2");
        params.addBodyParameter("tel",tel);
        params.addBodyParameter("nickname",nickname);
        params.addBodyParameter("email",email);
        params.addBodyParameter("card_code",card_code);
        params.addBodyParameter("openid",openid);
        params.addBodyParameter("link_address",link_address);
        params.addBodyParameter("level",level);
        params.addBodyParameter("created_at",created_at);

        LoadDialog.showLoad(UserInfoActivity.this,"正在提交,请稍后！",null);

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "saveInfo_onSuccess:" + result);

                if(!TextUtils.isEmpty(result)){
                    RespBookInfo info=new Gson().fromJson(result,RespBookInfo.class);

                    T.show(UserInfoActivity.this,"修改成功！");

                    if(info.isResult()){
                        finish();
                    }

                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "saveInfo_onError" + ex.getMessage());
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
