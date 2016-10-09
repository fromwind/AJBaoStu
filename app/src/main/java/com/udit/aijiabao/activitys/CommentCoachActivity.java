package com.udit.aijiabao.activitys;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;

import com.google.gson.Gson;
import com.udit.aijiabao.BaseActivity;
import com.udit.aijiabao.R;
import com.udit.aijiabao.User;
import com.udit.aijiabao.configs.Constants;
import com.udit.aijiabao.configs.UrlConfig;
import com.udit.aijiabao.dialog.LoadDialog;
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
 * Created by Administrator on 2016/5/30.
 */

@ContentView(R.layout.activity_comment_coach)
public class CommentCoachActivity extends BaseActivity {

    private final String TAG = CommentCoachActivity.class.getSimpleName();

    @ViewInject(R.id.titleView)
    private TitleView mtitleView;

    @ViewInject(R.id.fw_rating)
    private RatingBar fw;

    @ViewInject(R.id.jx_rating)
    private RatingBar jx;

    @ViewInject(R.id.sf_rating)
    private RatingBar sf;

    @ViewInject(R.id.cd_rating)
    private RatingBar cd;

    @ViewInject(R.id.edit_content)
    private EditText contentEd;

    private int jxnl,fwtd,jxsf,jxcd;

    private String content;

    @Override
    public void setContentView() {

    }

    @Override
    public void initView() {
        mtitleView.setTitleLayoutColor(getResources().getColor(R.color.blue_title));
        mtitleView.setTitleText("评论");
        mtitleView.hideOperate();
        mtitleView.showBack(true);
        mtitleView.setTitleTextColor(getResources().getColor(R.color.white));
    }

    @Override
    public void initData() {

    }

    @Event(value = R.id.submit_btn)
    private void click(View v){

        fwtd= (int) fw.getRating();
        jxnl= (int) jx.getRating();
        jxsf= (int) sf.getRating();
        jxcd= (int) cd.getRating();
        content=contentEd.getText().toString().trim();


        if(TextUtils.isEmpty(content)){
            content="亲,说点什么吧。。。";
        }

        RequestParams params=new RequestParams(UrlConfig.COMMENT_URL);

        params.addBodyParameter("method", "addComment");//
        params.addBodyParameter("user_type", Constants.USER_TYPE);

        params.addBodyParameter("token", User.getAuthToken());//--
        params.addBodyParameter("userId", String.valueOf(User.getUserId()));//--

        params.addBodyParameter("userName",PreferencesUtils.getString(Constants.NICKNAME,""));//--

        params.addBodyParameter("corpId", PreferencesUtils.getString(Constants.CORP_ID, ""));//--

        params.addBodyParameter("jxnl",String.valueOf(jxnl));//
        params.addBodyParameter("fwtd",String.valueOf(fwtd));//
        params.addBodyParameter("jxsf",String.valueOf(jxsf));
        params.addBodyParameter("jxcd",String.valueOf(jxcd));
        params.addBodyParameter("ztmyd",String.valueOf((jxnl+fwtd+jxsf+jxcd)/4));
        params.addBodyParameter("content",content);

        LoadDialog.showLoad(this,"正在提交中,请稍后!",null);

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                Log.e(TAG,"onSuccess"+result);

                RespBookInfo info=new Gson().fromJson(result,RespBookInfo.class);

                if(null!=info&&info.isResult()){
                    T.show(CommentCoachActivity.this,info.getMessage());
                }else {
                    T.show(CommentCoachActivity.this,info.getError());
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
                LoadDialog.close();

                finish();
            }
        });


    }
}
