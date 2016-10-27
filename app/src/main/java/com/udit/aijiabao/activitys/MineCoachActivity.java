package com.udit.aijiabao.activitys;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.udit.aijiabao.BaseActivity;
import com.udit.aijiabao.R;
import com.udit.aijiabao.User;
import com.udit.aijiabao.configs.Constants;
import com.udit.aijiabao.configs.UrlConfig;
import com.udit.aijiabao.dialog.LoadDialog;
import com.udit.aijiabao.dialog.PromptDialog;
import com.udit.aijiabao.entitys.RespCoachInfo;
import com.udit.aijiabao.utils.PreferencesUtils;
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

@ContentView(R.layout.activity_mine_coach)
public class MineCoachActivity extends BaseActivity {

    private final String TAG = MineCoachActivity.class.getSimpleName();

    @ViewInject(R.id.titleView)
    private TitleView mtitleView;

    @ViewInject(R.id.coach_img)
    private ImageView coach_img;

    @ViewInject(R.id.coach_name)
    private TextView coachName;

    @ViewInject(R.id.phone_num)
    private TextView phone;

    @ViewInject(R.id.school_name)
    private TextView schoolName;

    @ViewInject(R.id.car_number)
    private TextView carNum;

    @ViewInject(R.id.subject)
    private TextView subject;

    @ViewInject(R.id.ratingbar)
    private RatingBar ratingBar;

    private RespCoachInfo coachInfo;

    @Override
    public void setContentView() {

    }

    @Override
    public void initView() {
        mtitleView.setTitleLayoutColor(getResources().getColor(R.color.blue_title));
        mtitleView.setTitleText("我的教练");
        mtitleView.hideOperate();
        mtitleView.showBack(true);
        mtitleView.setTitleTextColor(getResources().getColor(R.color.white));
    }

    @Override
    public void initData() {
        queryCoach();
    }

    @Event(value = {R.id.phone_img}, type = View.OnClickListener.class)
    private void clicks(View v) {
        if(null!=coachInfo)
        call(coachInfo.getTel());
    }

    private void queryCoach() {

        RequestParams params = new RequestParams(UrlConfig.QUERY_COACH_URL);

        LoadDialog.showLoad(this,"正在加载页面中，请稍等！",null);

        params.addBodyParameter("member_id", User.getMemberId());
        params.addBodyParameter("coach_id", PreferencesUtils.getString(Constants.COACH_ID, ""));

        params.addBodyParameter("method", "query");
        params.addBodyParameter("user_type", Constants.USER_TYPE);
        params.addBodyParameter("token", User.getAuthToken());
        params.addBodyParameter("userId", String.valueOf(User.getUserId()));

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess" + result);

                coachInfo=new Gson().fromJson(result,RespCoachInfo.class);
                bindDatas(coachInfo);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "ex" + ex.getMessage());
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

    private void bindDatas(RespCoachInfo coachInfo){
        if(null!=coachInfo){
            coachName.setText(coachInfo.getName());

            phone.setText(coachInfo.getTel());

            schoolName.setText(PreferencesUtils.getString(Constants.SCHOOL, ""));

            carNum.setText(coachInfo.getCarno());

            subject.setText(PreferencesUtils.getString(Constants.SUBJECT, ""));

            if (!TextUtils.isEmpty(coachInfo.getStars())) {
                ratingBar.setRating(Float.parseFloat(coachInfo.getStars()));
            }else {
                ratingBar.setRating(5);
            }
        }
    }

    private void call(final String phone) {


        if (!TextUtils.isEmpty(phone)) {
            PromptDialog.Builder dialog = new PromptDialog.Builder(this);
            dialog.setTitle("拨打电话");
            dialog.setMessage2(phone.trim());
            dialog.setMessage2Color(getResources().getColor(R.color.main_red));
            dialog.setButton1(R.string.cancel,
                    new PromptDialog.OnClickListener() {
                        @Override
                        public void onClick(Dialog dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            dialog.setButton2(R.string.ok, new PromptDialog.OnClickListener() {
                @Override
                public void onClick(Dialog dialog, int which) {
                    dialog.dismiss();
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri
                            .parse("tel:" + phone.trim()));
                    if (ActivityCompat.checkSelfPermission(MineCoachActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    startActivity(intent);
                }
            });
            dialog.show();
        }
    }
}
