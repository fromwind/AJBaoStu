package com.udit.aijiabao.activitys;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.udit.aijiabao.BaseActivity;
import com.udit.aijiabao.R;
import com.udit.aijiabao.User;
import com.udit.aijiabao.configs.Constants;
import com.udit.aijiabao.configs.UrlConfig;
import com.udit.aijiabao.entitys.RespBookInfo;
import com.udit.aijiabao.utils.PreferencesUtils;
import com.udit.aijiabao.widgets.TitleView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import cc.zhipu.library.event.EventBus;

/**
 * Created by Administrator on 2016/5/17.
 */
@ContentView(R.layout.activity_confirm_appoint)
public class ConfirmAppointActivity extends BaseActivity {

    private final String TAG = ConfirmAppointActivity.class.getSimpleName();

    @ViewInject(R.id.titleView)
    private TitleView mtitleView;

    @ViewInject(R.id.make_schllo_name)
    private TextView schoolname;

    @ViewInject(R.id.make_coach_name)
    private TextView teacherName;

    @ViewInject(R.id.make_training_time)
    private TextView trainedTime;


    private String date;
    private String period;


    @Override
    public void setContentView() {

    }

    @Override
    public void initView() {

        Bundle bundle = getIntent().getBundleExtra("bundle");

        date = bundle.getString("DATE", "");

        period = bundle.getString("PERIOD", "");
    }

    @Override
    public void initData() {

        mtitleView.setTitleLayoutColor(getResources().getColor(R.color.blue_title));
        mtitleView.setTitleText("预约");
        mtitleView.hideOperate();
        mtitleView.showBack(true);
        mtitleView.setTitleTextColor(getResources().getColor(R.color.white));

        schoolname.setText(PreferencesUtils.getString(Constants.SCHOOL, ""));
        teacherName.setText(PreferencesUtils.getString(Constants.TEACHER, ""));

        trainedTime.setText("练习时间：" + date + " " + period);

    }

    @Event(value = {R.id.commit_yuyue, R.id.cancel_yuyue}, type = View.OnClickListener.class)
    private void clicks(View v) {

        switch (v.getId()) {

            case R.id.commit_yuyue:
                confirm();
                break;

            case R.id.cancel_yuyue:
                onBackPressed();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG,"onDestroy");
    }

    private void confirm() {
        RequestParams params = new RequestParams(UrlConfig.HOME_BOOk_URL);
        params.addBodyParameter("method", "save");
        params.addBodyParameter("userId", String.valueOf(User.getUserId()));
        params.addBodyParameter("token", User.getAuthToken());
        params.addBodyParameter("user_type", Constants.USER_TYPE);
        params.addBodyParameter("member_id", User.getMemberId());
        params.addBodyParameter("training_item_id", PreferencesUtils.getString(Constants.TRAINED_ITEM_ID, ""));
        params.addBodyParameter("coach_id", PreferencesUtils.getString(Constants.COACH_ID, ""));
        params.addBodyParameter("booking_date", date);
        params.addBodyParameter("booking_period", period);

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                Log.e(TAG, result);

                RespBookInfo info=new Gson().fromJson(result,RespBookInfo.class);

                EventBus.getDefault().post(info);
                onBackPressed();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

}
