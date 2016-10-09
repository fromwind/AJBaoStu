package com.udit.aijiabao.activitys;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.udit.aijiabao.BaseActivity;
import com.udit.aijiabao.R;
import com.udit.aijiabao.User;
import com.udit.aijiabao.configs.Constants;
import com.udit.aijiabao.configs.UrlConfig;
import com.udit.aijiabao.entitys.RespBookInfo;
import com.udit.aijiabao.utils.PreferencesUtils;
import com.udit.aijiabao.utils.T;
import com.udit.aijiabao.widgets.TitleView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by Administrator on 2016/5/19.
 */

@ContentView(R.layout.activity_comment)
public class CommentActivity extends BaseActivity {

    private final String TAG = CommentActivity.class.getSimpleName();

    @ViewInject(R.id.titleView)
    private TitleView mtitleView;

    @ViewInject(R.id.jiaolian_name)
    private TextView coachName;

    @ViewInject(R.id.data)
    private TextView dateT;

    @ViewInject(R.id.time)
    private TextView timeT;


    @ViewInject(R.id.jxnl_rating)
    private RatingBar bar0;

    @ViewInject(R.id.fwtd_rating)
    private RatingBar bar1;

    @ViewInject(R.id.clzj_rating)
    private RatingBar bar2;

    @ViewInject(R.id.clzk_rating)
    private RatingBar bar3;

    @ViewInject(R.id.zt_rating)
    private RatingBar bar4;

    private String date;
    private String time;
    private String booking_id;

    private int jxnl,fwtd,cnzj,clzk,myd;

    @Override
    public void setContentView() {
        Bundle bundle = getIntent().getExtras();

        date=bundle.getString("data","");
        time=bundle.getString("time","");
        booking_id=bundle.getString("booking_id","");

    }

    @Override
    public void initView() {
        mtitleView.setTitleLayoutColor(getResources().getColor(R.color.blue_title));
        mtitleView.setTitleText("评价");
        mtitleView.hideOperate();
        mtitleView.showBack(true);
        mtitleView.setTitleTextColor(getResources().getColor(R.color.white));
    }

    @Override
    public void initData() {
        coachName.setText(PreferencesUtils.getString(Constants.TEACHER, ""));

        dateT.setText(date);

        timeT.setText(time);
    }

    @Event(value = R.id.btn_sub)
    private void click(View v){
        jxnl= (int) bar0.getRating();
        fwtd= (int) bar1.getRating();
        cnzj= (int) bar2.getRating();

        clzk= (int) bar3.getRating();
        myd= (int) bar4.getRating();
        comment(jxnl,fwtd,cnzj,clzk,myd);
    }

    private void comment(int jxnl, int fwtd, int cnzj, int clzk, int myd){

        RequestParams params=new RequestParams(UrlConfig.COMMENT_URL);

        params.addBodyParameter("method", "save");
        params.addBodyParameter("user_type", Constants.USER_TYPE);

        params.addBodyParameter("token", User.getAuthToken());
        params.addBodyParameter("userId", String.valueOf(User.getUserId()));

        params.addBodyParameter("user_name",User.getLocalUserName());
        params.addBodyParameter("booking_id",booking_id);
        params.addBodyParameter("corp_id", PreferencesUtils.getString(Constants.CORP_ID, ""));


        params.addBodyParameter("jxnl",String.valueOf(jxnl));
        params.addBodyParameter("fwtd",String.valueOf(fwtd));
        params.addBodyParameter("cnzj",String.valueOf(cnzj));
        params.addBodyParameter("clzk",String.valueOf(clzk));
        params.addBodyParameter("ztmyd",String.valueOf(myd));


        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG,"onSuccess"+result);

                RespBookInfo info=new Gson().fromJson(result,RespBookInfo.class);

                if(info.isResult()){
                    T.show(CommentActivity.this,info.getMessage());
                }else{
                    T.show(CommentActivity.this,info.getError());
                }

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
                onBackPressed();
            }
        });


    }


}
