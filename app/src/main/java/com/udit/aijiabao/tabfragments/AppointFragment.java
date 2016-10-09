package com.udit.aijiabao.tabfragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.udit.aijiabao.R;
import com.udit.aijiabao.User;
import com.udit.aijiabao.activitys.ConfirmAppointActivity;
import com.udit.aijiabao.activitys.LoginActivity;
import com.udit.aijiabao.adapters.BaseAdapterInject;
import com.udit.aijiabao.adapters.ViewHolderInject;
import com.udit.aijiabao.configs.Constants;
import com.udit.aijiabao.configs.UrlConfig;
import com.udit.aijiabao.dialog.LoadDialog;
import com.udit.aijiabao.dialog.PromptDialog;
import com.udit.aijiabao.dialog.WarnDialog;
import com.udit.aijiabao.entitys.BookingsBean;
import com.udit.aijiabao.entitys.Events;
import com.udit.aijiabao.entitys.ItemsBean;
import com.udit.aijiabao.entitys.RespAppointInfo;
import com.udit.aijiabao.entitys.RespBookInfo;
import com.udit.aijiabao.tabfragments.callbacks.TabCallBack;
import com.udit.aijiabao.utils.NetWorkUtils;
import com.udit.aijiabao.utils.PreferencesUtils;
import com.udit.aijiabao.utils.T;
import com.udit.aijiabao.widgets.TitleView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import cc.zhipu.library.event.EventBus;

/**
 * Created by Administrator on 2016/5/11.
 */
@SuppressLint("ValidFragment")
@ContentView(R.layout.appointfragment_layout)
public class AppointFragment extends Fragment implements AdapterView.OnItemClickListener {

    private final String TAG = AppointFragment.class.getSimpleName();

    private static AppointFragment appointFragment;

    private TabCallBack back;

    @ViewInject(R.id.apschool_name)
    private TextView schoolName;

    @ViewInject(R.id.subject)
    private TextView subjectName;

    @ViewInject(R.id.apcoach_name)
    private TextView apcoachName;

    @ViewInject(R.id.apcar_no)
    private TextView carNum;

    @ViewInject(R.id.trained_count)
    private TextView trainedNum;

    @ViewInject(R.id.aptotal_count)
    private TextView training_count;

    @ViewInject(R.id.left_listview)
    private ListView leftListView;

    @ViewInject(R.id.right_listview)
    private ListView rightListView;

    @ViewInject(R.id.emptyview)
    private TextView emptyView;

    private LeftAdapter leftAdapter;

    private RightAdapter rightAdapter;

    private List<BookingsBean> lList;

    private List<ItemsBean> rList;

    private int selectedPositon=0;

    public static AppointFragment newInstance(TabCallBack back) {
        if (null == appointFragment) {
            appointFragment = new AppointFragment(back);
        }
        return appointFragment;
    }

    public AppointFragment(TabCallBack back) {
        this.back = back;

    }

    public AppointFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = x.view().inject(this, inflater, container);
        EventBus.getDefault().register(this);
        Log.e("Bum","appointRun__________");

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(Events.Refresh2 refresh) {
        if (refresh.refresh) {
            Log.e(TAG, "onEventMainThread=" + refresh.refresh);
            initDatas();
            initAppoiontDatas();
        }
    }

    public void onEventMainThread(RespBookInfo info){

        if(null!=info){

            if(info.isResult()){

            }else{

                new WarnDialog(getActivity())
                        .builder()
                        .setTip1("温馨提示")
                        .setTip2(TextUtils.isEmpty(info.getMessage())?info.getError():info.getMessage())
                        .setBack(new WarnDialog.BtnCallBack() {
                            @Override
                            public void clickView() {

                            }
                        })
                        .show();
            }
            initDatas();
            initAppoiontDatas();
        }

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        leftAdapter = new LeftAdapter(getContext());

        leftListView.setAdapter(leftAdapter);

        rightAdapter = new RightAdapter(getContext());

        rightListView.setAdapter(rightAdapter);

        if(!TextUtils.isEmpty(PreferencesUtils.getString(Constants.SCHOOL, ""))){
            rightListView.setEmptyView(emptyView);
        }

        leftListView.setOnItemClickListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            SystemBarTintManager tintManager = new SystemBarTintManager(getActivity());
            // 激活状态栏
            tintManager.setStatusBarTintEnabled(true);
            // enable navigation bar tint 激活导航栏
            tintManager.setNavigationBarTintEnabled(true);
            //设置系统栏设置颜色
            //tintManager.setTintColor(R.color.red);
            //给状态栏设置颜色
            tintManager.setStatusBarTintResource(R.color.blue_title);
            //Apply the specified drawable or color resource to the system navigation bar.
            //给导航栏设置资源
            tintManager.setNavigationBarTintResource(R.color.blue_title);
        }
    }

    private void initRightListView(int position) {
        if (null == rList) {
            rList = new ArrayList<ItemsBean>();
        }
        rList.clear();

        BookingsBean bookingsBean=leftAdapter.getItem(position);

        if(bookingsBean.isEnable()){
            rList.addAll(bookingsBean.getItems());
            rightAdapter.setBean(bookingsBean);
        }

        rightAdapter.setData(rList);
        rightAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("YANG", "预约onResume");

            if (!TextUtils.isEmpty(User.getAuthToken())) {
                if (TextUtils.isEmpty(PreferencesUtils.getString(Constants.SCHOOL, ""))) {

                    new WarnDialog(getActivity())
                            .builder()
                            .setTip1("温馨提示")
                            .setTip2("亲，驾校还没有您的信息,请先报名哦！")
                            .setBack(new WarnDialog.BtnCallBack() {
                                @Override
                                public void clickView() {
                                    back.clickView(1);
                                }
                            })
                            .show();
                } else {
                    //有绑定驾校
                    initDatas();
                    initAppoiontDatas();

                }
            } else {
                back.clickView(1);
                startActivityForResult(new Intent(getActivity(), LoginActivity.class), Constants.LOGIN_RESQ);
                getActivity().overridePendingTransition(R.anim.push_right_to_left_in,
                        R.anim.push_right_to_left_out);
            }
        }

    private void initDatas() {
        schoolName.setText(PreferencesUtils.getString(Constants.SCHOOL, ""));
        subjectName.setText(PreferencesUtils.getString(Constants.SUBJECT, ""));
        apcoachName.setText(PreferencesUtils.getString(Constants.TEACHER, ""));
        carNum.setText(PreferencesUtils.getString(Constants.CARNUM, ""));
        trainedNum.setText(PreferencesUtils.getString(Constants.TRAINED, ""));
        training_count.setText(PreferencesUtils.getString(Constants.NO_TRAINED, ""));
    }

    @Event(value = {R.id.call_image,R.id.appoint_refresh}, type = View.OnClickListener.class)
    private void click(View v) {
        switch (v.getId()){
            case R.id.appoint_refresh:
                initDatas();
                initAppoiontDatas();
                break;
            case R.id.call_image:
                call();
                break;
        }
    }

    private void initAppoiontDatas() {
        RequestParams params = new RequestParams(UrlConfig.HOME_BOOk_URL);

        params.addBodyParameter("method", "queryAll");
        params.addBodyParameter("user_type", Constants.USER_TYPE);

        params.addBodyParameter("corp_id", PreferencesUtils.getString(Constants.CORP_ID));
        params.addBodyParameter("coach_id", PreferencesUtils.getString(Constants.COACH_ID));
        params.addBodyParameter("member_id", PreferencesUtils.getString(Constants.MEMBER_ID));

        LoadDialog.showLoad(getContext(), "正在加载中，请稍等！", null);

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                if (null == lList) {
                    lList = new ArrayList<>();
                }

                Log.e(TAG, "onSuccess" + result);

                if (!TextUtils.isEmpty(result)) {
                    RespAppointInfo respAppointInfo = new Gson().fromJson(result, RespAppointInfo.class);

                    if (null != respAppointInfo.getBookings() && respAppointInfo.getBookings().size() > 0) {
                        lList.clear();
                        lList.addAll(respAppointInfo.getBookings());
                        leftAdapter.setData(lList);
                        leftAdapter.notifyDataSetChanged();
                        leftAdapter.setSelectItem(selectedPositon);

                        onItemClick(null, leftListView, selectedPositon, 0);
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("initAppoiontDatas", "onError" + ex.getMessage());
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

    private void call() {

        final String phone = PreferencesUtils.getString(Constants.TEACHER_PHONE, "");

        if (!TextUtils.isEmpty(phone)) {
            PromptDialog.Builder dialog = new PromptDialog.Builder(getContext());
            dialog.setTitle("拨打电话");
            dialog.setMessage(phone.trim());
            dialog.setMessageColor(getResources().getColor(R.color.main_red));
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
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    getContext().startActivity(intent);
                }
            });
            dialog.show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        selectedPositon=position;

        leftAdapter.setSelectItem(position);

        initRightListView(position);
    }

    private class LeftAdapter extends BaseAdapterInject<BookingsBean> {

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
        public ViewHolderInject<BookingsBean> getNewHolder(int position) {
            return new ViewHolder();
        }

        class ViewHolder extends ViewHolderInject<BookingsBean> {

            @ViewInject(R.id.item_text_week)
            private TextView week;

            @ViewInject(R.id.item_text_date)
            private TextView date;

            @ViewInject(R.id.left_listview_item)
            private LinearLayout item;

            @Override
            public void loadData(BookingsBean data, int position) {
                week.setText(data.getWeek());
                date.setText(data.getDate());

                if (position == selectItem) {
                    item.setBackgroundColor(getResources().getColor(R.color.gray_10));
                } else {
                    item.setBackgroundColor(getResources().getColor(R.color.white));
                }
            }
        }
    }

    private class RightAdapter extends BaseAdapterInject<ItemsBean> {

        private Context context;

        private BookingsBean bean;

        public void setBean(BookingsBean bean) {
            this.bean = bean;
        }

        public RightAdapter(Context context) {
            super(context);
            this.context=context;
        }

        @Override
        public int getConvertViewId(int position) {
            return R.layout.right_listview_item;
        }

        @Override
        public ViewHolderInject<ItemsBean> getNewHolder(int position) {
            return new ViewHolder();
        }

        class ViewHolder extends ViewHolderInject<ItemsBean> {

            @ViewInject(R.id.item_time_slot)
            private TextView timeSlot;

            @ViewInject(R.id.item_appoint_state)
            private TextView appointState;

            @ViewInject(R.id.item_surplus)
            private TextView surplus;

            @ViewInject(R.id.item_btn)
            private TextView item_btn;

            @ViewInject(R.id.item_info_layout)
            private LinearLayout item_layout;

            @ViewInject(R.id.item_empty)
            private TextView itemEmpty;

            @Override
            public void loadData(ItemsBean data, int position) {

                timeSlot.setText(data.getPeriod());

                if(!data.isEnable()){
                    appointState.setText("未开启");
                    itemEmpty.setVisibility(View.VISIBLE);
                    item_layout.setVisibility(View.GONE);

                }else {
                    itemEmpty.setVisibility(View.GONE);
                    item_layout.setVisibility(View.VISIBLE);
                    appointState.setText(data.isBooked() ? "已预约" : "未预约");
                    surplus.setText((data.getAvailable_count() == 0) ? "已满" : "剩余" + data.getAvailable_count() + "人");

                    item_btn.setText(data.isBooked() ? "取消" : "预约");
                    item_btn.setTextColor(data.isBooked() ? getResources().getColor(R.color.white) : getResources().getColor(R.color.gray_80));
                    item_btn.setBackgroundResource(data.isBooked() ? R.drawable.btn_cancle_bg : R.drawable.btn_confirm_bg);
                    if (data.getAvailable_count()!=0){
                        item_btn.setClickable(true);
                    }else {
                        if (data.isBooked()==true)item_btn.setClickable(true);
                        else {
                            item_btn.setTextColor(getResources().getColor(R.color.gray_40));
                            item_btn.setClickable(false);
                        }
                    }
                    item_btn.setTag(data);
                }
            }

            @Event(value = {R.id.item_btn}, type = View.OnClickListener.class)
            private void click(View v) {
                EventBus.getDefault().post(new Events.Refresh(true));
                final ItemsBean itemsBean = (ItemsBean) v.getTag();

                if(null!=itemsBean){

                    if(itemsBean.isBooked()){
                        //取消预约操作
                        Log.e("Bum","点击取消预约操作");
                        PromptDialog.Builder pDialog = new PromptDialog.Builder(
                                getContext());
                        pDialog.setTitle("温馨提示");
                        pDialog.setMessage(context.getString(R.string.reminder_info));
                        pDialog.setButton1("返回", new PromptDialog.OnClickListener() {
                            @Override
                            public void onClick(Dialog dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        pDialog.setButton2("确认", new PromptDialog.OnClickListener() {
                            @Override
                            public void onClick(Dialog dialog, int which) {
                                dialog.dismiss();

                                Log.e("Bum",""+bean.getDate()+"/"+itemsBean.getPeriod());
                                cancleAppoint(itemsBean);
                            }
                        });
                        pDialog.create().show();

                    }else{
                        //预约操作
                        Log.e("Bum","点击预约操作");
                        Bundle bundle=new Bundle();
                        bundle.putString("DATE",bean.getDate());
                        bundle.putString("PERIOD", itemsBean.getPeriod());
                        Intent intent=new Intent(getActivity(), ConfirmAppointActivity.class);
                        intent.putExtra("bundle", bundle);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.push_right_to_left_in,
                                R.anim.push_right_to_left_out);
                    }

                }
            }

            private void cancleAppoint(ItemsBean itemsBean){

                RequestParams params=new RequestParams(UrlConfig.HOME_BOOk_URL);

                params.addBodyParameter("method", "cancel");
                params.addBodyParameter("user_type", Constants.USER_TYPE);
                params.addBodyParameter("token", User.getAuthToken());
                params.addBodyParameter("userId", String.valueOf(User.getUserId()));
                params.addBodyParameter("member_id",User.getMemberId());

                params.addBodyParameter("booking_date",bean.getDate());
                params.addBodyParameter("booking_time",itemsBean.getPeriod());

                x.http().post(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Log.e(TAG, "" + result);

                        RespBookInfo info=new Gson().fromJson(result,RespBookInfo.class);

                        if(null!=info&&!info.isResult()){
                            new WarnDialog(getActivity())
                                    .builder()
                                    .setTip1("温馨提示")
                                    .setTip2(info.getMessage())
                                    .show();
                        }
                        EventBus.getDefault().post(new Events.Refresh(true));

                        initDatas();
                        initAppoiontDatas();
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
