package com.udit.aijiabao.activitys;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.udit.aijiabao.BaseActivity;
import com.udit.aijiabao.R;
import com.udit.aijiabao.User;
import com.udit.aijiabao.adapters.BaseAdapterInject;
import com.udit.aijiabao.adapters.ViewHolderInject;
import com.udit.aijiabao.configs.Constants;
import com.udit.aijiabao.configs.UrlConfig;
import com.udit.aijiabao.dialog.PromptDialog;
import com.udit.aijiabao.dialog.WarnDialog;
import com.udit.aijiabao.entitys.Details;
import com.udit.aijiabao.entitys.RespAppoint;
import com.udit.aijiabao.entitys.RespBookInfo;
import com.udit.aijiabao.utils.PreferencesUtils;
import com.udit.aijiabao.widgets.TitleView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/19.
 */

@ContentView(R.layout.activity_mine_appoint)
public class MineAppointActivty extends BaseActivity implements AdapterView.OnItemClickListener {

    private final String TAG = MineAppointActivty.class.getSimpleName();

    @ViewInject(R.id.titleView)
    private TitleView mtitleView;

    @ViewInject(R.id.left_listview)
    private ListView leftListView;

    @ViewInject(R.id.right_listview)
    private ListView rightListView;

    private LeftAdapter leftAdapter;

    List<RespAppoint> leftList;

    List<Details>rightList;

    private RightAdapter rightAdapter;

    private int selectedPositon=0;

    @Override
    public void setContentView() {
        mtitleView.setTitleLayoutColor(getResources().getColor(R.color.blue_title));
        mtitleView.setTitleText("我的预约");
        mtitleView.hideOperate();
        mtitleView.showBack(true);
        mtitleView.setTitleTextColor(getResources().getColor(R.color.white));
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        leftList=new ArrayList<>();
        leftAdapter=new LeftAdapter(this);
        leftListView.setAdapter(leftAdapter);

        rightList=new ArrayList<>();
        rightAdapter=new RightAdapter(this);
        rightListView.setAdapter(rightAdapter);

        leftListView.setOnItemClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        queryAppoint();
    }

    private void queryAppoint(){

        RequestParams params=new RequestParams(UrlConfig.HOME_BOOk_URL);

        params.addBodyParameter("method", "query");
        params.addBodyParameter("user_type", Constants.USER_TYPE);

        params.addBodyParameter("token", User.getAuthToken());
        params.addBodyParameter("userId", String.valueOf(User.getUserId()));

        params.addBodyParameter("member_id", User.getMemberId());
        params.addBodyParameter("corp_id", PreferencesUtils.getString(Constants.CORP_ID, ""));



        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG,"onSuccess"+result);

                List<RespAppoint> appointList = new Gson().fromJson(result, new TypeToken<List<RespAppoint>>() {
                }.getType());

                Log.e(TAG, "onSuccess" + appointList.size());

                leftList.clear();

                leftList.addAll(appointList);

                leftAdapter.setData(leftList);
                leftAdapter.notifyDataSetChanged();

                leftAdapter.setSelectItem(selectedPositon);

                onItemClick(null,leftListView,selectedPositon,0);
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

            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        selectedPositon=position;

        leftAdapter.setSelectItem(selectedPositon);
        rightList.clear();


        rightList.addAll(leftAdapter.getItem(position).getDetail());

        rightAdapter.setData(rightList);

        rightAdapter.notifyDataSetChanged();
    }


    private class LeftAdapter extends BaseAdapterInject<RespAppoint> {

        private int selectItem = -1;

        public void setSelectItem(int selectItem) {
            this.selectItem = selectItem;
            notifyDataSetChanged();
        }

        public LeftAdapter(Context context) {
            super(context);
        }

        @Override
        public int getConvertViewId(int position) {
            return R.layout.listview_appoint_left_item;
        }

        @Override
        public ViewHolderInject<RespAppoint> getNewHolder(int position) {
            return new ViewHolder();
        }

        class ViewHolder extends ViewHolderInject<RespAppoint> {

            @ViewInject(R.id.item_text_week)
            private TextView week;

            @ViewInject(R.id.item_text_date)
            private TextView date;

            @ViewInject(R.id.left_listview_item)
            private LinearLayout item;

            @Override
            public void loadData(RespAppoint data, int position) {

                week.setText(data.getName()+"("+data.getTimes()+"/"+data.getTotaltimes()+")");
                date.setText(data.getTraning_status());

                if (position == selectItem) {
                    item.setBackgroundColor(getResources().getColor(R.color.gray_10));
                } else {
                    item.setBackgroundColor(getResources().getColor(R.color.white));
                }
            }
        }
    }

    private class RightAdapter extends BaseAdapterInject<Details> {

        private Context context;

        public RightAdapter(Context context) {
            super(context);
            this.context=context;
        }

        @Override
        public int getConvertViewId(int position) {
            return R.layout.right_listview_item1;
        }

        @Override
        public ViewHolderInject<Details> getNewHolder(int position) {
            return new ViewHolder();
        }

        class ViewHolder extends ViewHolderInject<Details> {

            @ViewInject(R.id.year)
            private TextView year;

            @ViewInject(R.id.time)
            private TextView time;

            @ViewInject(R.id.status)
            private TextView status;

            @ViewInject(R.id.comment_btn)
            private TextView comment;

            @Override
            public void loadData(Details data, int position) {
                year.setText(data.getBooking_date());
                time.setText(data.getBooking_time());

                comment.setTag(data);
                switch (data.getStatus()){

                    case 0:
                        status.setText("未练习");
                        comment.setVisibility(View.VISIBLE);
                        comment.setText("取消");
                        comment.setClickable(true);
                        break;

                    case 2:
                        status.setText("已练习");
                        comment.setVisibility(View.VISIBLE);

                        if (data.getCommented().equals("0")) {
                            comment.setText("评价");
                            comment.setClickable(true);
                        } else {
                            comment.setText("已评价");
                            comment.setClickable(false);
                        }
                        break;

                    case 4:
                        status.setText("违约");
                        comment.setVisibility(View.GONE);
                        break;

                }

            }

            @Event(value = R.id.comment_btn,type = View.OnClickListener.class)
            private void click(View v){

                final Details data= (Details) comment.getTag();

                switch (data.getStatus()){

                    case 0:
                        PromptDialog.Builder pDialog = new PromptDialog.Builder(
                                context);
                        pDialog.setTitle("温馨提示");
                        pDialog.setMessage2(context.getString(R.string.reminder_info));
                        pDialog.setButton1("取消", new PromptDialog.OnClickListener() {
                            @Override
                            public void onClick(Dialog dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        pDialog.setButton2("确认", new PromptDialog.OnClickListener() {
                            @Override
                            public void onClick(Dialog dialog, int which) {
                                dialog.dismiss();
                                cancleAppoint(data);
                            }
                        });
                        pDialog.create().show();
                        break;

                    case 2:
                        status.setText("已练习");
                        comment.setVisibility(View.VISIBLE);

                        if (data.getCommented().equals("0")) {

                            Bundle bundle=new Bundle();

                            bundle.putString("data", data.getBooking_date());
                            bundle.putString("time", data.getBooking_time());
                            bundle.putString("booking_id", String.valueOf(data.getBooking_id()));

                            startActivity(CommentActivity.class,bundle);
                        }
                        break;
                }

            }

            private void cancleAppoint(Details details){

                RequestParams params=new RequestParams(UrlConfig.HOME_BOOk_URL);

                params.addBodyParameter("method", "cancel");
                params.addBodyParameter("user_type", Constants.USER_TYPE);
                params.addBodyParameter("token", User.getAuthToken());
                params.addBodyParameter("userId", String.valueOf(User.getUserId()));
                params.addBodyParameter("member_id",User.getMemberId());

                params.addBodyParameter("booking_date",details.getBooking_date());
                params.addBodyParameter("booking_time",details.getBooking_time());

                x.http().post(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Log.e(TAG, "" + result);


                        RespBookInfo info=new Gson().fromJson(result,RespBookInfo.class);


                        if(null!=info&&!info.isResult()){
                            new WarnDialog(MineAppointActivty.this)
                                    .builder()
                                    .setTip1("温馨提示")
                                    .setTip2(info.getMessage())
                                    .show();
                        }

                        queryAppoint();
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
    }

}
